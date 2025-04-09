package client.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import service.URLBuilder;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static websocket.commands.UserGameCommand.CommandType.*;

public class WebSocketFacade extends Endpoint {

    NotificationHandler notificationHandler;
    Session session;

    public WebSocketFacade(int port, NotificationHandler notificationHandler, ChessGame.TeamColor perspective) {
        try {
            String url = URLBuilder.getWSURLFromPort(port);
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

                    ServerMessage actualMessage = null;
                    switch (serverMessage.getServerMessageType()) {
                        case LOAD_GAME:
                            actualMessage = new Gson().fromJson(message, LoadGameMessage.class);
                            break;
                        case ERROR:
                            actualMessage = new Gson().fromJson(message, ErrorMessage.class);
                            break;
                        case NOTIFICATION:
                            actualMessage = new Gson().fromJson(message, NotificationMessage.class);
                            break;
                        default:
                            System.err.println("Unknown server message type received");
                            return;
                    }
                    notificationHandler.notify(actualMessage, perspective);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            //throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameID){
        try{
            var command = new UserGameCommand(CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception ex){
            System.out.println("Error connecting to server");
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move){
        try{
            var command = new MakeMoveCommand(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception ex){
            System.out.println("Error connecting to server");
        }
    }

    public void leave(String authToken, int gameID){
        try{
            var command = new UserGameCommand(LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception ex){
            System.out.println("Error connecting to server");
        }
    }

    public void resign(String authToken, int gameID){
        try{
            var command = new UserGameCommand(RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception ex){
            System.out.println("Error connecting to server");
        }
    }
}
