package dataaccess;

import dataobjects.GameData;
import exception.ResponseException;

import java.util.Collection;

public interface IGameDAO {

    Collection<GameData> listGames() throws ResponseException;
    GameData addGame(GameData game) throws ResponseException;
    void deleteAllGames() throws ResponseException;
}
