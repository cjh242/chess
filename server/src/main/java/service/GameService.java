package service;

import chess.ChessGame;
import dataaccess.IGameDAO;
import dataobjects.GameData;
import exception.ResponseException;
import request.CreateGameRequest;
import request.JoinGameRequest;

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

    public GameData findGameByID(int gameID) throws ResponseException {
        return gameDao.findByID(gameID);
    }

    public GameData update(GameData game) throws ResponseException {
        return gameDao.update(game);
    }

    public Collection<GameData> listGames(String authToken) throws ResponseException {
        if(authService.isAuthValid(authToken)){
            return gameDao.listGames();
        }
        return null;
    }

    public GameData addGame(CreateGameRequest game) throws ResponseException {
        if(authService.isAuthValid(game.authToken())){
            return gameDao.addGame(game.gameName());
        }
        return null;
    }

    public void deleteAllGames() throws ResponseException {
        gameDao.deleteAllGames();
    }
}
