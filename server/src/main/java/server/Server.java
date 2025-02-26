package server;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exception.ResponseException;
import request.CreateGameRequest;
import request.RegisterRequest;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

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
        this.userService = new UserService(new MemoryUserDAO());
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);
//        Spark.post("/session", this::addPet);
        Spark.post("/game", this::addGame);
//        Spark.put("/game", this::function);
        Spark.get("/game", this::listGames);
//        Spark.delete("/pet/:id", this::deletePet);
        Spark.delete("/db", this::deleteAll);
//        Spark.delete("/session", this::deleteAllPets);
        Spark.exception(ResponseException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(ex.toJson());
    }

    private Object register(Request req, Response res) throws ResponseException {
        var registerReq = new Gson().fromJson(req.body(), RegisterRequest.class);
        var user = userService.register(registerReq);
        return new Gson().toJson(user);
    }

    private Object addGame(Request req, Response res) throws ResponseException {
        var gameReq = new Gson().fromJson(req.body(), CreateGameRequest.class);
        var game = gameService.addGame(gameReq);
        //webSocketHandler.makeNoise(pet.name(), pet.sound());
        return new Gson().toJson(game);
    }

    private Object listGames(Request req, Response res) throws ResponseException {
        res.type("application/json");
        var list = gameService.listGames(req.headers("Authorization")).toArray();
        return new Gson().toJson(Map.of("game", list));
    }


//    private Object deletePet(Request req, Response res) throws ResponseException {
//        var id = Integer.parseInt(req.params(":id"));
//        var pet = service.getPet(id);
//        if (pet != null) {
//            service.deletePet(id);
//            webSocketHandler.makeNoise(pet.name(), pet.sound());
//            res.status(204);
//        } else {
//            res.status(404);
//        }
//        return "";
//    }

    private Object deleteAll(Request req, Response res) throws ResponseException {
        gameService.deleteAllGames();
        authService.deleteAllAuths();
        //TODO: DELETE FROM THE OTHER THINGS
        res.status(204);
        return "";
    }
}
