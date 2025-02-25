package dataaccess;

import dataobjects.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDataDAO  implements IGameDataDAO{
    private int nextId = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    public Collection<GameData> listGames() {
        return games.values();
    }
}
