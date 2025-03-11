package dataaccess;

import dataobjects.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class GameDAO implements IGameDAO{
    private final DatabaseStartup dbStarter;

    public GameDAO(DatabaseStartup dbStarter) throws DataAccessException {
        this.dbStarter = dbStarter;
        dbStarter.configureDatabase();
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    public GameData addGame(String gameName) throws DataAccessException {
        return null;
    }

    public void deleteAllGames() throws DataAccessException {

    }

    public GameData findByID(int gameID) throws DataAccessException {
        return null;
    }

    public GameData update(GameData game) {
        return null;
    }
}
