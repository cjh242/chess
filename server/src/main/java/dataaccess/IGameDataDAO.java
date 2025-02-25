package dataaccess;

import dataobjects.GameData;

import java.util.Collection;

public interface IGameDataDAO {

    Collection<GameData> listGames();
}
