package server.websocket;

import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataobjects.AuthData;
import dataobjects.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import service.GameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
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

        //IF NOT AUTH SEND ERROR
        if(!isAuthenticated || auth == null){
            var errorMessage = new ErrorMessage("Not Authorized");
            String jsonMessage = new Gson().toJson(errorMessage);
            var connection = new Connection(null, 0, session);
            connection.send(jsonMessage);
            return;
        }

        //SAVE THE SESSION TO CONNECTION MANAGER IF NOT EXISTS
        connections.add(auth.username(), command.getGameID(), session);

        //ACT ON THE COMMAND
        switch (command.getCommandType()) {
            case CONNECT :
                connect(auth.username(), command.getGameID());
                break;
            case MAKE_MOVE :
                var moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                makeMove(moveCommand.getGameID(), moveCommand.getMove(), auth.username());
                break;
            case LEAVE :
                leave(auth.username(), command.getGameID());
                break;
            case RESIGN :
                resign(auth.username(), command.getGameID());
                break;
        }



    }

    //TODO: ADD CHECKS ON ALL OF THESE TO CHECK IF THE GAME IS OVER

    private void connect(String username, int gameID){
        //send a LOAD_GAME back to the client
        GameData game = getGameData(username, gameID);
        if (game == null) return;
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

    private void makeMove(int gameID, ChessMove move, String username){
        GameData game = null;
        try{
            game = gameService.findGameByID(gameID);
        } catch (Exception ex){
            connections.send(username, new ErrorMessage("An unknown error occurred finding the game"));
            return;
        }

        try{
            game.game().makeMove(move);
        } catch (InvalidMoveException ex){
            connections.send(username, new ErrorMessage("Invalid Move"));
            return;
        }

        try{
            gameService.update(game);
        } catch (Exception ex){
            connections.send(username, new ErrorMessage("Error updating game"));
        }

        //all others in this game are sent a LOAD_GAME
        connections.broadcast(null, gameID, new LoadGameMessage(game));

        //all others are sent a notification that the move was made (including what it was)
        //TODO: write a toMessageString that says what the move was
        connections.broadcast(username, gameID, new NotificationMessage(username + " made move"));

        //if the move results in check, checkmate, or stalemate everyone is notified
        if(game.game().isInCheck(WHITE) || game.game().isInCheck(BLACK)) {
            if(game.game().isInCheckmate(WHITE) || game.game().isInCheckmate(BLACK)) {
                //TODO: ADD NAME
                connections.broadcast(username, gameID, new NotificationMessage("CHECKMATE"));
                //mark game as over
                game.game().setIsGameOver(true);
            } else {
                connections.broadcast(username, gameID, new NotificationMessage("CHECK"));
            }
        }
        if(game.game().isInStalemate(WHITE) || game.game().isInStalemate(BLACK)){
            connections.broadcast(username, gameID, new NotificationMessage("STALEMATE"));
            //mark game as over
            game.game().setIsGameOver(true);
        }
    }

    private void leave(String username, int gameID){
        //game is updated to remove the client
        GameData game;
        game = getGameData(username, gameID);
        if (game == null) {
            return;
        }

        if(Objects.equals(game.whiteUsername(), username)){
            //update the game
            game.addWhiteUsername(null);
            try {
                gameService.update(game);
            } catch (Exception ex) {
                connections.send(username, new ErrorMessage("Failed to update game"));
                return;
            }

        }
        else if(Objects.equals(game.blackUsername(), username)){
            game.addBlackUsername(null);
            try {
                gameService.update(game);
            } catch (Exception ex) {
                connections.send(username, new ErrorMessage("Failed to update game"));
                return;
            }
        }

        connections.remove(username);
        //notify everyone that they left
        connections.broadcast(username, gameID, new NotificationMessage(username + " left the game"));
    }



    private void resign(String username, int gameID){
        GameData game;
        game = getGameData(username, gameID);
        if (game == null) {
            return;
        }

        if(!Objects.equals(game.whiteUsername(), username) && !Objects.equals(game.blackUsername(), username)){
            connections.send(username, new ErrorMessage("Observers cannot resign. Leave instead."));
            return;
        }

        //mark game as over
        game.game().setIsGameOver(true);

        try {
            gameService.update(game);
        } catch (Exception ex) {
            connections.send(username, new ErrorMessage("Failed to update game"));
            return;
        }


        //notify everyone that they resigned
        if(Objects.equals(game.whiteUsername(), username)) {
            connections.broadcast(username, gameID, new NotificationMessage(username + " (White) has resigned"));
        }
        else if(Objects.equals(game.blackUsername(), username)){
            connections.broadcast(username, gameID, new NotificationMessage(username + " (Black) has resigned"));
        }

        connections.remove(username);
    }

    private GameData getGameData(String username, int gameID) {
        GameData game;
        try{
            game = gameService.findGameByID(gameID);
        } catch (Exception ex){
            connections.send(username, new ErrorMessage("An unknown error occurred finding the game"));
            return null;
        }

        if(game == null){
            connections.send(username, new ErrorMessage("Game not found"));
            return null;
        }
        return game;
    }
}
