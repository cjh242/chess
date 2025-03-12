package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.IGameDAO;
import dataobjects.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import result.Result;

public class GameService {
    private final IGameDAO gameDao;
    private final AuthService authService;

    public GameService(IGameDAO gameDao, AuthService authService){
        this.gameDao = gameDao;
        this.authService = authService;
    }

    public GameService(AuthService authService) {
        this.gameDao = new GameDAO();
        this.authService = authService;
    }

    public Result joinGame(JoinGameRequest join) {
        try {
            var game = findGameByID(join.gameID());
            var auth = authService.getAuthByID(join.authToken());
            if(game == null){
                return new Result(400);
            }
            else if(authService.isAuthValid(auth)){
                if(join.playerColor() == ChessGame.TeamColor.BLACK){
                    if(game.blackUsername() == null) {
                        game = game.addBlackUsername(auth.username());
                    } else {
                        return new Result(403);
                    }
                }
                else if(join.playerColor() == ChessGame.TeamColor.WHITE){
                    if(game.whiteUsername() == null) {
                        game = game.addWhiteUsername(auth.username());
                    } else {
                        return new Result(403);
                    }
                }
                else {
                    return new Result(400);
                }
                update(game);
                return new Result(200);
            }
            else {
                return new Result(401);
            }
        } catch (Exception ex) {
            return new Result(500);
        }

    }

    public ListGamesResult listGames(String authToken) {
        try {
            var auth = authService.getAuthByID(authToken);
            if(authService.isAuthValid(auth)){
                return new ListGamesResult(gameDao.listGames(), 200);
            }
            return new ListGamesResult(null, 401);
        } catch (Exception ex){
            return new ListGamesResult(null, 500);
        }
    }

    public CreateGameResult addGame(CreateGameRequest game) {
        try {
            var auth = authService.getAuthByID(game.authToken());
            if(authService.isAuthValid(auth)){
                var createdGame = gameDao.addGame(game.gameName());
                return new CreateGameResult(createdGame.gameID(), 200);
            }
        } catch (Exception ex) {
            return new CreateGameResult(0, 500);
        }
        return new CreateGameResult(0, 401);
    }

    public Result deleteAllGames() {
        try {
            gameDao.deleteAllGames();
            return new Result(200);
        } catch (Exception ex) {
            return new Result(500);
        }
    }

    //private helpers
    private GameData findGameByID(int gameID) throws DataAccessException {
        return gameDao.findByID(gameID);
    }

    private void update(GameData game) throws DataAccessException {
        gameDao.update(game);
    }
}
