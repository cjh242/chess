package service;

import chess.ChessGame;
import dataaccess.IGameDAO;
import dataobjects.GameData;
import exception.ResponseException;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.Result;

import java.util.Collection;

public class GameService {
    private final IGameDAO gameDao;
    private final AuthService authService;

    public GameService(IGameDAO gameDao, AuthService authService){
        this.gameDao = gameDao;
        this.authService = authService;
    }

    public GameData joinGame(JoinGameRequest join) throws ResponseException {
        var game = findGameByID(join.gameID());
        var auth = authService.getAuthByID(join.authToken());
        if(join.playerColor() == ChessGame.TeamColor.BLACK){
            game = game.addBlackUsername(auth.username());
        }
        else{
            game = game.addWhiteUsername(auth.username());
        }
        return update(game);
    }

    private GameData findGameByID(int gameID) throws ResponseException {
        return gameDao.findByID(gameID);
    }

    private GameData update(GameData game) throws ResponseException {
        return gameDao.update(game);
    }

    public Collection<GameData> listGames(String authToken) throws ResponseException {
        if(authService.isAuthValid(authToken)){
            return gameDao.listGames();
        }
        return null;
    }

    public CreateGameResult addGame(CreateGameRequest game) {
        try {
            if(authService.isAuthValid(game.authToken())){
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
}
