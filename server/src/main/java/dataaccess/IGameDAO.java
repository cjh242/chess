package dataaccess;

import dataobjects.GameData;
import exception.ResponseException;

import java.util.Collection;

public interface IGameDAO {

    Collection<GameData> listGames() throws ResponseException;
    GameData addGame(String gameName) throws ResponseException;
    void deleteAllGames() throws ResponseException;
}
