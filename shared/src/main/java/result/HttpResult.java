package result;

import dataobjects.GameData;
import java.util.List;

public record HttpResult(boolean isOk, String message, String authToken, List<GameData> games) {
    public HttpResult(boolean isOk, String message) {
        this(isOk, message, null, null);
    }
    public HttpResult(boolean isOk, String message, List<GameData> games) {
        this(isOk, message, null, games);
    }
    public HttpResult(boolean isOk, String message, String authToken) {
        this(isOk, message, authToken, null);
    }

}
