package client;

import com.google.gson.Gson;
import dataobjects.GameData;
import request.*;
import result.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class ServerFacade {

    private final String baseUrl = "http://localhost:8080/";

    public HttpResult login(LoginRequest login) throws Exception{
        URI uri = new URI(baseUrl + "session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");

        var body = Map.of("username", login.username(), "password", login.password());

        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();

        // Handle bad HTTP status
        var status = http.getResponseCode();
        if ( status >= 200 && status < 300) {
            try (InputStream in = http.getInputStream()) {
                var successResult = new Gson().fromJson(new InputStreamReader(in), LoginResult.class);
                return new HttpResult(true, "Logged in as " + successResult.username(), successResult.authToken());
            }
        } else {
            try (InputStream in = http.getErrorStream()) {
                var failResult = new Gson().fromJson(new InputStreamReader(in), ErrorResult.class);
                return new HttpResult(false, failResult.message());
            }
        }
    }
    public HttpResult register(RegisterRequest register) throws Exception{
        URI uri = new URI(baseUrl + "user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");

        var body = Map.of("username", register.username(),
                "password", register.password(),
                "email", register.email());

        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();

        // Handle bad HTTP status
        var status = http.getResponseCode();
        if ( status >= 200 && status < 300) {
            try (InputStream in = http.getInputStream()) {
                var successResult = new Gson().fromJson(new InputStreamReader(in), LoginResult.class);
                return new HttpResult(true, "Logged in as " + successResult.username(), successResult.authToken());
            }
        } else {
            try (InputStream in = http.getErrorStream()) {
                var failResult = new Gson().fromJson(new InputStreamReader(in), ErrorResult.class);
                return new HttpResult(false, failResult.message());
            }
        }
    }

    public HttpResult logout(LogoutRequest logout) throws Exception{
        URI uri = new URI(baseUrl + "session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.setDoOutput(true);
        http.setRequestProperty("authorization", logout.authToken());

        http.connect();

        var status = http.getResponseCode();
        if ( status >= 200 && status < 300) {
            return new HttpResult(true, "Logged Out");
        } else {
            try (InputStream in = http.getErrorStream()) {
                var failResult = new Gson().fromJson(new InputStreamReader(in), ErrorResult.class);
                return new HttpResult(false, failResult.message());
            }
        }
    }

    public HttpResult createGame(CreateGameRequest request) throws Exception{
        URI uri = new URI(baseUrl + "game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("authorization", request.authToken());
        http.addRequestProperty("Content-Type", "application/json");

        var body = Map.of("gameName", request.gameName());

        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();

        // Handle bad HTTP status
        var status = http.getResponseCode();
        // TODO: Need to print the created game
        if ( status >= 200 && status < 300) {
            try (InputStream in = http.getInputStream()) {
                var successResult = new Gson().fromJson(new InputStreamReader(in), CreateGameResult.class);
                return new HttpResult(true, "Game Created " + request.gameName());
            }
        } else {
            try (InputStream in = http.getErrorStream()) {
                var failResult = new Gson().fromJson(new InputStreamReader(in), ErrorResult.class);
                return new HttpResult(false, failResult.message());
            }
        }
    }

    public HttpResult playGame(JoinGameRequest request) throws Exception{
        URI uri = new URI(baseUrl + "game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("PUT");
        http.setDoOutput(true);
        http.setRequestProperty("authorization", request.authToken());
        http.addRequestProperty("Content-Type", "application/json");

        var body = Map.of("playerColor", request.playerColor().toString(), "gameID", request.gameID());

        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();

        // Handle bad HTTP status
        var status = http.getResponseCode();
        // TODO: return a different result here, and prep to print the game
        if ( status >= 200 && status < 300) {
            return new HttpResult(true, "Game Joined");
        } else {
            try (InputStream in = http.getErrorStream()) {
                var failResult = new Gson().fromJson(new InputStreamReader(in), ErrorResult.class);
                return new HttpResult(false, failResult.message());
            }
        }
    }

    public HttpResult listGames(String authToken) throws Exception{
        URI uri = new URI(baseUrl + "game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");
        http.setDoOutput(true);
        http.setRequestProperty("authorization", authToken);

        http.connect();

        // Handle bad HTTP status
        var status = http.getResponseCode();
        // TODO: return a different result here, and prep to print the game
        if ( status >= 200 && status < 300) {
            try (InputStream in = http.getInputStream()) {
                var successResult = new Gson().fromJson(new InputStreamReader(in), ListGamesResult.class);
                return new HttpResult(true, "Games: ", (List<GameData>)successResult.games());
            }
        } else {
            try (InputStream in = http.getErrorStream()) {
                var failResult = new Gson().fromJson(new InputStreamReader(in), ErrorResult.class);
                return new HttpResult(false, failResult.message());
            }
        }
    }
    public HttpResult observeGame(int gameNumber){
        //TODO: finish this method
        return new HttpResult(false, "");
    }
}
