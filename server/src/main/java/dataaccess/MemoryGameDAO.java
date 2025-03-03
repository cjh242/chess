package dataaccess;

import dataobjects.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements IGameDAO {
    private int nextId = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    public GameData findByID(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    public GameData update(GameData game){
        games.put(game.gameID(), game);
        return game;
    }

    public GameData addGame(String gameName) throws DataAccessException {
        var game = new GameData(nextId++, null, null, gameName);

        games.put(game.gameID(), game);
        return game;
    }

    public void deleteAllGames() throws DataAccessException {
        games.clear();
    }
}
