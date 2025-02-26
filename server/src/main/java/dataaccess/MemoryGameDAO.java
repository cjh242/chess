package dataaccess;

import chess.ChessGame;
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

    public GameData addGame(String gameName) throws ResponseException {
        var game = new GameData(nextId++, null, null, gameName, new ChessGame());

        games.put(game.gameId(), game);
        return game;
    }

    public void deleteAllGames() throws ResponseException {
        games.clear();
    }
}
