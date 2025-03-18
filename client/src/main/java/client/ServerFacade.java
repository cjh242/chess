package client;

import com.google.gson.Gson;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
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

    public LoginResult login(LoginRequest login) throws Exception{
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
                System.out.println(new Gson().fromJson(new InputStreamReader(in), Map.class));
            }
        } else {
            try (InputStream in = http.getErrorStream()) {
                System.out.println(new Gson().fromJson(new InputStreamReader(in), Map.class));
            }
        }
        return new LoginResult(null, null, 0);
    }
    public String register(RegisterRequest register) throws Exception{
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
        //var result = readResponseBody(http);
        var resultString = "";
        if ( status >= 200 && status < 300) {
            try (InputStream in = http.getInputStream()) {
                resultString = "Success" + new Gson().fromJson(new InputStreamReader(in), Map.class);
            }
        } else {
            if(status == 500){
                resultString = "An unknown error occurred with the server.";
            }
            else if(status == 400){
                resultString = "There was an issue with your request. Please check usage constraints";
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
        }
        return resultString;
    }
    public CreateGameResult createGame(){
        return new CreateGameResult(0, 0);
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
