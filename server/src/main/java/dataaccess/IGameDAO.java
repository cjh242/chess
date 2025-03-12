package dataaccess;

import dataobjects.GameData;

import java.util.Collection;

public interface IGameDAO {

    Collection<GameData> listGames() throws DataAccessException;
    GameData addGame(String gameName) throws DataAccessException;
    void deleteAllGames() throws DataAccessException;
    GameData findByID(int gameID) throws DataAccessException;
    GameData update(GameData game) throws DataAccessException;
}
