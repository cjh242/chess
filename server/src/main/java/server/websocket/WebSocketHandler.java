package server.websocket;

import com.google.gson.Gson;
import dataobjects.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.swing.*;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    public WebSocketHandler(AuthService authService){
        this.authService = authService;
    }

    private final ConnectionManager connections = new ConnectionManager();
    private final AuthService authService;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        //TODO: FINISH THIS METHOD
        //AUTHORIZE, HANDLE CASE WHEN NOT AUTHORIZED
        boolean isAuthenticated;
        AuthData auth = null;
        try{
            auth = authService.getAuthByID(command.getAuthToken());
            isAuthenticated = authService.isAuthValid(auth);
        } catch (Exception ex){
            isAuthenticated = false;
        }

        //IF NOT AUTH, DO NOTHING
        if(!isAuthenticated || auth == null){
            return;
        }

        //SAVE THE SESSION TO CONNECTION MANAGER IF NOT EXISTS
        connections.add(auth.username(), command.getGameID(), session);

        //ACT ON THE COMMAND
        switch (command.getCommandType()) {
            case CONNECT -> connect();
            case MAKE_MOVE -> unimplemented();
            case LEAVE -> unimplemented();
            case RESIGN -> unimplemented();
        }



    }

    private void connect(){
        var message = new ServerMessage();
    }

    private void enter(String visitorName, Session session) throws IOException {
//        connections.add(visitorName, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
    }

    private void exit(String visitorName) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
    }

    private void unimplemented(){

    }
}
