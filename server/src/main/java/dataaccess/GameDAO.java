package dataaccess;

import dataobjects.GameData;

import java.util.Collection;
import java.util.List;

public class GameDAO implements IGameDAO{
    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public GameData addGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAllGames() throws DataAccessException {

    }

    @Override
    public GameData findByID(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData update(GameData game) {
        return null;
    }
}
