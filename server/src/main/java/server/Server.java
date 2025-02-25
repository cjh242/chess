package server;

import com.google.gson.Gson;
import dataaccess.MemoryGameDataDAO;
import exception.ResponseException;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Map;

public class Server {
    private final GameService gameService;
    private final UserService userService;

    public Server(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    //for the tests
    public Server(){
        this.gameService = new GameService(new MemoryGameDataDAO());
        this.userService = new UserService();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//        Spark.post("/user", this::addPet);
//        Spark.post("/session", this::addPet);
//        Spark.post("/game", this::addPet);
//        Spark.put("/game", this::function);
        Spark.get("/game", this::listGames);
//        Spark.delete("/pet/:id", this::deletePet);
//        Spark.delete("/db", this::deleteAllPets);
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

//    private Object addPet(Request req, Response res) throws ResponseException {
//        var pet = new Gson().fromJson(req.body(), Pet.class);
//        pet = service.addPet(pet);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
//        return new Gson().toJson(pet);
//    }

    private Object listGames(Request req, Response res) throws ResponseException {
        res.type("application/json");
        var list = gameService.listGames().toArray();
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

//    private Object deleteAllPets(Request req, Response res) throws ResponseException {
//        service.deleteAllPets();
//        res.status(204);
//        return "";
//    }
}
