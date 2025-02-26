package service;

import dataaccess.IGameDAO;
import dataobjects.GameData;
import exception.ResponseException;

import java.util.Collection;

public class GameService {
    private final IGameDAO gameDao;

    public GameService(IGameDAO gameDao){
        this.gameDao = gameDao;
    }

    public Collection<GameData> listGames() throws ResponseException {
        return gameDao.listGames();
    }

    public GameData addGame(GameData game) throws ResponseException {
        return gameDao.addGame(game);
    }

    public void deleteAllGames() throws ResponseException {
        gameDao.deleteAllGames();
    }
}
