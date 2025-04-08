package server.websocket;

import websocket.messages.ServerMessage;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, int gameID, Session session) {
        var connection = new Connection(username, gameID, session);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void broadcast(String excludeUserName, int gameID, ServerMessage message) {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUserName) && c.gameID == gameID) {
                    try{
                        c.send(message.toString());
                    } catch (Exception ex){

                    }
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

    public void send(String username, ServerMessage message) {
        var connection = connections.get(username);
        if(connection != null){
            try {
                connection.send(message.toString());
            } catch (Exception ex){

            }
        }
    }
}
