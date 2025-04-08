package client.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import service.URLBuilder;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static websocket.commands.UserGameCommand.CommandType.CONNECT;

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
                    notificationHandler.notify(serverMessage, perspective);
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

    public void makeMove(){

    }

    public void leave(){

    }

    public void resign(){

    }
}
