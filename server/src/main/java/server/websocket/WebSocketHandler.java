package server.websocket;

import com.google.gson.Gson;
import dataobjects.AuthData;
import dataobjects.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import service.GameService;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

import static websocket.messages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebSocketHandler {

    public WebSocketHandler(AuthService authService, GameService gameService){
        this.authService = authService;
        this.gameService = gameService;
    }

    private final ConnectionManager connections = new ConnectionManager();
    private final AuthService authService;
    private final GameService gameService;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

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
            case CONNECT -> connect(auth.username(), command.getGameID());
            case MAKE_MOVE -> unimplemented();
            case LEAVE -> unimplemented();
            case RESIGN -> unimplemented();
        }



    }

    private void connect(String username, int gameID){
        //send a LOAD_GAME back to the client
        GameData game = null;
        try{
            game = gameService.findGameByID(gameID);
        } catch (Exception ex){
            connections.send(username, new ErrorMessage("An unknown error occurred finding the game"));
            return;
        }

        if(game == null){
            connections.send(username, new ErrorMessage("Game not found"));
            return;
        }
        connections.send(username,new LoadGameMessage(game));

        //tell all others in this game that someone connected, either as observer or player(including which color)
        if(Objects.equals(game.whiteUsername(), username)){
            connections.broadcast(username, gameID, new NotificationMessage(username + " joined as white"));
        }
        else if(Objects.equals(game.blackUsername(), username)){
            connections.broadcast(username, gameID, new NotificationMessage(username + " joined as black"));
        }
        else{
            connections.broadcast(username, gameID, new NotificationMessage(username + " joined as an observer"));
        }
    }

    private void makeMove(){
        //verify move valid
        //game is updated to have made move, including in database
        //all others in this game are sent a LOAD_GAME
        //all others are sent a notification that the move was made (including what it was)
        //if the move results in check, checkmate, or stalemate everyone is notified
    }

    private void leave(){
        //game is updated to remove the client
        //notify everyone that they left
    }

    private void resign(){
        //mark game as over
        //notify everyone that they resigned
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
