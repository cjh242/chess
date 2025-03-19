package client;

import com.google.gson.Gson;
import request.*;
import result.CreateGameResult;
import result.HttpResult;
import result.ListGamesResult;
import result.LoginResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
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
        LoginResult result = (LoginResult)readResponseBody(http);
        var resultString = status >= 200 && status < 300 ? "Logged in as" + result.username() : getResultString(status);
        return new HttpResult(status == 200, resultString);
    }
    public HttpResult register(RegisterRequest register) throws Exception{
        URI uri = new URI(baseUrl + "user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");

        var body = Map.of("username", register.username(),
                "password", register.password(),
                "email", register.password());

        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();

        // Handle bad HTTP status
        var status = http.getResponseCode();
        LoginResult result = (LoginResult)readResponseBody(http);
        var resultString = status >= 200 && status < 300 ? "Logged in as" + result.username() : getResultString(status);
        return new HttpResult(status == 200, resultString, result.authToken());
    }

    public HttpResult logout(LogoutRequest logout) throws Exception{
        URI uri = new URI(baseUrl + "user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("authorization", logout.authToken());

        http.connect();

        // Handle bad HTTP status
        var status = http.getResponseCode();
        var resultString = status >= 200 && status < 300 ? "Logged out" : getResultString(status);
        return new HttpResult(status == 200, resultString);
    }

    private static String getResultString(int status) {
        var resultString = "";
        if(status == 500){
            resultString = "An unknown error occurred with the server.";
        }
        else if(status == 400){
            resultString = "There was an issue with your request. Please check usage constraints";
        }
        else if(status == 401){
            resultString = "Wrong username or password";
        }
        else if(status == 403){
            resultString = "Username already taken. Please choose a different username";
        }
        else {
            resultString = "An unknown error occurred.";
        }
//            try (InputStream in = http.getErrorStream()) {
//                System.out.println(new Gson().fromJson(new InputStreamReader(in), Map.class));
//            }
        return resultString;
    }

    public HttpResult createGame(CreateGameRequest request){
        return new HttpResult(false, "");
    }

    public HttpResult playGame(JoinGameRequest request){
        return new HttpResult(false, "");
    }

    public HttpResult listGames(){
        return new HttpResult(false, "");
    }
    public HttpResult observeGame(int gameNumber){
        return new HttpResult(false, "");
    }


    private static Object readResponseBody(HttpURLConnection http) throws IOException {
        Object responseBody = "";
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            responseBody = new Gson().fromJson(inputStreamReader, Map.class);
        }
        return responseBody;
    }

}
