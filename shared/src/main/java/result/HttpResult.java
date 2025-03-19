package result;

public record HttpResult(boolean isOk, String message, String authToken) {
    public HttpResult(boolean isOk, String message) {
        this(isOk, message, null);
    }
}
