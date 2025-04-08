package service;

import chess.ChessGame;
import client.websocket.NotificationHandler;
import dataobjects.GameData;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class NotificationCentral implements NotificationHandler {


    @Override
    public void notify(ServerMessage message, ChessGame.TeamColor perspective) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION :
                var notification = (NotificationMessage) message;
                displayNotification(notification.getMessage());
                break;
            case ERROR :
                var error = (ErrorMessage) message;
                displayError(error.getError());
                break;
            case LOAD_GAME :
                var load = (LoadGameMessage) message;
                loadGame(load.getGame(), perspective);
                break;
        }
    }

    private void displayNotification(String message){
        System.out.println(message);
    }

    private void displayError(String error){
        System.out.println(error);
    }

    private void loadGame(GameData game, ChessGame.TeamColor perspective){
        PrintingHelper.printBoard(game.game().getBoard(), -1, null, perspective);
    }
}
