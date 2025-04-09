package websocket.messages;
import static websocket.messages.ServerMessage.ServerMessageType.ERROR;

public class ErrorMessage extends ServerMessage{

    private final String errorMessage;

    public ErrorMessage(String error) {
        super(ERROR);
        this.errorMessage = error;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
