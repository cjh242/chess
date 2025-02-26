package service;

import dataaccess.IGameDAO;
import dataobjects.GameData;
import exception.ResponseException;
import request.CreateGameRequest;

import java.util.Collection;

public class GameService {
    private final IGameDAO gameDao;
    private final AuthService authService;

    public GameService(IGameDAO gameDao, AuthService authService){
        this.gameDao = gameDao;
        this.authService = authService;
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
