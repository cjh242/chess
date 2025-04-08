package server.websocket;

import com.google.gson.Gson;
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
                        String jsonMessage = new Gson().toJson(message);
                        c.send(jsonMessage);
                    } catch (Exception ex){
                        System.out.println("Error sending message in broadcast");
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
                String jsonMessage = new Gson().toJson(message);
                connection.send(jsonMessage);
            } catch (Exception ex){
                System.out.println(ex + "failed to send");
            }
        }
    }
}
