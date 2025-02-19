package result;
import dataobjects.GameData;
import java.util.Collection;

public record ListGamesResult(Collection<GameData> games) {
}
