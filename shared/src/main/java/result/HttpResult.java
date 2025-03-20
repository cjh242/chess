package result;

import dataobjects.GameData;

import java.util.Collection;

public record HttpResult(boolean isOk, String message, String authToken, Collection<GameData> games) {
    public HttpResult(boolean isOk, String message) {
        this(isOk, message, null, null);
    }
    public HttpResult(boolean isOk, String message, Collection<GameData> games) {
        this(isOk, message, null, games);
    }
    public HttpResult(boolean isOk, String message, String authToken) {
        this(isOk, message, authToken, null);
    }

}
