package dataaccess;

import dataobjects.GameData;
import exception.ResponseException;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements IGameDAO {
    private int nextId = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    public Collection<GameData> listGames() throws ResponseException {
        return games.values();
    }

    public GameData addGame(GameData game) throws ResponseException {
        game = new GameData(nextId++, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());

        games.put(game.gameId(), game);
        return game;
    }

    public void deleteAllGames() throws ResponseException {
        games.clear();
    }
}
