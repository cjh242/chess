package dataaccess;

import dataobjects.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements IGameDAO {
    private int nextId = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    public Collection<GameData> listGames() {
        return games.values();
    }

    public GameData findByID(int gameID) {
        return games.get(gameID);
    }

    public GameData update(GameData game){
        games.put(game.gameID(), game);
        return game;
    }

    public GameData addGame(String gameName) {
        var game = new GameData(nextId++, null, null, gameName);

        games.put(game.gameID(), game);
        return game;
    }

    public void deleteAllGames() {
        games.clear();
    }
}
