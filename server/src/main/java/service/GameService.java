package service;

import dataaccess.IGameDataDAO;
import dataobjects.GameData;
import exception.ResponseException;
import result.ListGamesResult;

import java.util.Collection;

public class GameService {
    private final IGameDataDAO gameDao;

    public GameService(IGameDataDAO gameDao){
        this.gameDao = gameDao;
    }

    public Collection<GameData> listGames() throws ResponseException {
        return gameDao.listGames();
    }
}
