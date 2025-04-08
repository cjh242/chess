package websocket.messages;
import static websocket.messages.ServerMessage.ServerMessageType.ERROR;

public class ErrorMessage extends ServerMessage{

    private final String error;

    public ErrorMessage(String error) {
        super(ERROR);
        this.error = error;
    }

    public String getError(){
        return error;
    }
}
