package service;

import client.websocket.NotificationHandler;
import websocket.messages.ServerMessage;

public class NotificationCentral implements NotificationHandler {


    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification();
            case ERROR -> displayError();
            case LOAD_GAME -> loadGame();
        }
    }

    private void displayNotification(){

    }
    private void displayError(){

    }
    private void loadGame(){

    }
}
