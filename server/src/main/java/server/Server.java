package server;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import request.*;
import result.Result;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.ArrayList;
import java.util.Map;

public class Server {
    private final GameService gameService;
    private final UserService userService;
    private final AuthService authService;

    public Server(GameService gameService, UserService userService, AuthService authService) {
        this.gameService = gameService;
        this.userService = userService;
        this.authService = authService;
    }

    //for the tests
    public Server(){
        this.authService = new AuthService(new MemoryAuthDAO());
        this.gameService = new GameService(new MemoryGameDAO(), this.authService);
        this.userService = new UserService(new MemoryUserDAO(), this.authService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game", this::listGames);
        Spark.delete("/db", this::deleteAll);
        Spark.delete("/session", this::logout);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


    private Object register(Request req, Response res) {
        var registerReq = new Gson().fromJson(req.body(), RegisterRequest.class);
        var user = userService.register(registerReq);
        if(user.code() == 403) {
            res.status(403);
            return new Gson().toJson(Map.of("message", "Error: already taken"));
        }
        if(user.code() == 400) {
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        }
        return new Gson().toJson(user);
    }

    private Object login(Request req, Response res) {
        var loginReq = new Gson().fromJson(req.body(), LoginRequest.class);
        var loginRes = userService.login(loginReq);
        if(loginRes.code() == 401){
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        return new Gson().toJson(loginRes);
    }

    private Object createGame(Request req, Response res) {
        var gameReq = new Gson().fromJson(req.body(), CreateGameRequest.class);
        gameReq = gameReq.withAuthToken(req.headers("Authorization"));
        var game = gameService.addGame(gameReq);
        if (game.code() == 401){
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        return new Gson().toJson(game);
    }

    private Object joinGame(Request req, Response res) {
        var joinReq = new Gson().fromJson(req.body(), JoinGameRequest.class);
        joinReq = joinReq.withAuthToken(req.headers("Authorization"));
        var game = gameService.joinGame(joinReq);
        if (game.code() == 401){
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        else if (game.code() == 403){
            res.status(403);
            return new Gson().toJson(Map.of("message", "Error: already taken"));
        }
        else if (game.code() == 400){
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        }
        return new Gson().toJson(game);
    }

    private Object listGames(Request req, Response res) {
        var result = gameService.listGames(req.headers("Authorization"));
        if (result.code() == 401) {
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        return new Gson().toJson(result);
    }

    private Object deleteAll(Request req, Response res) {
        var results = new ArrayList<Result>();
        results.add(gameService.deleteAllGames());
        results.add(authService.deleteAllAuths());
        results.add(userService.deleteAllUsers());
        if(results.stream().anyMatch(x -> x.code() == 500)){
            res.status(500);
            return new Gson().toJson(Map.of("message", "Error: Failed to delete data"));
        }
        return "";
    }

    private Object logout(Request req, Response res) {
        var logoutReq = new LogoutRequest(req.headers("Authorization"));
        var result = authService.logout(logoutReq);
        if(result.code() == 401){
            res.status(401);
            return new Gson().toJson(Map.of("message", "Error: unauthorized"));
        }
        return "";
    }
}
