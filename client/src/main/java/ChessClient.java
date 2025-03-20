import chess.ChessGame;
import client.ServerFacade;
import dataobjects.GameData;
import request.*;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;

public class ChessClient {

    private final static Collection<String> preLoginCommands = List.of("help", "quit", "login", "register");
    private final static Collection<String> postLoginCommands =
            List.of("help", "logout", "create", "list", "play", "observe");

    public void RunChessClient(){
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        boolean isLoggedIn = false;
        String authToken = null;
        int gameId = 0;
        Collection<GameData> games;
        //TODO: Local board variable?

        ServerFacade server = new ServerFacade();

        System.out.println("\uD83C\uDF1F Welcome to 240 Chess. Type Help to get started. \uD83C\uDF1F");

        while (isRunning) {
            System.out.print(isLoggedIn ? "\n[LOGGED_IN] >>> " : "\n[LOGGED_OUT] >>> ");
            String input = scanner.nextLine().trim();

            // Split input into command and arguments
            String[] parts = input.split("\\s+"); // Splits by spaces
            if (parts.length == 0) continue; // Ignore empty input

            String command = parts[0].toLowerCase(); // First part is the command

            String loginString = "Please first login or register";
            switch (command) {
                case "help":
                    if (isLoggedIn) {
                        System.out.println("  create <NAME> - a game");
                        System.out.println("  list - games");
                        System.out.println("  join <ID> [WHITE|BLACK] - a game");
                        System.out.println("  observe <ID> - a game");
                        System.out.println("  logout - when you are done");
                    } else {
                        System.out.println("  register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
                        System.out.println("  login <USERNAME> <PASSWORD> - to play chess");
                    }
                    System.out.println("  quit - playing chess");
                    System.out.println("  help - with possible commands");
                    break;

                case "register":
                    if(isLoggedIn){
                        System.out.println("Cannot register while logged in. Please first logout.");
                    }
                    if (parts.length < 4) {
                        System.out.println("Usage: register <USERNAME> <PASSWORD> <EMAIL>");
                        break;
                    }
                    try {
                        var result = server.register(new RegisterRequest(parts[1], parts[2], parts[3]));
                        System.out.println(result.message());
                        if (result.isOk()){
                            isLoggedIn = true;
                            authToken = result.authToken();
                        }
                    } catch (Exception ex) {
                        System.out.println("Failed to register user");
                    }
                    break;

                case "login":
                    if(isLoggedIn){
                        System.out.println("Already logged in. Please first logout.");
                    }
                    if (parts.length < 3) {
                        System.out.println("Usage: login <USERNAME> <PASSWORD>");
                        break;
                    }
                    try {
                        var result = server.login(new LoginRequest(parts[1], parts[2]));
                        System.out.println(result.message());
                        if (result.isOk()){
                            isLoggedIn = true;
                            authToken = result.authToken();
                        }
                    } catch (Exception ex) {
                        System.out.println("Failed to login user");
                    }

                    break;

                case "logout":
                    if(!isLoggedIn){
                        System.out.println(loginString);
                        break;
                    }
                    if (parts.length != 1){
                        System.out.println("Usage: logout");
                        break;
                    }
                    try {
                        var result = server.logout(new LogoutRequest(authToken));
                        System.out.println(result.message());
                        if (result.isOk()){
                            isLoggedIn = false;
                            authToken = null;
                        }
                    } catch (Exception ex) {
                        System.out.println("Failed to login user");
                    }
                    break;
                case "create":
                    if(!isLoggedIn){
                        System.out.println(loginString);
                    }
                    if (parts.length != 2){
                        System.out.println("Usage: create <GAME_NAME>");
                        break;
                    }
                    try {
                        var result = server.createGame(new CreateGameRequest(authToken, parts[1]));
                        System.out.println(result.message());
                    } catch (Exception ex) {
                        System.out.println("Failed to login user");
                    }
                    break;
                case "list":
                    if(!isLoggedIn){
                        System.out.println(loginString);
                    }
                    if (parts.length != 1){
                        System.out.println("Usage: list");
                        break;
                    }
                    try {
                        var result = server.listGames(authToken);
                        System.out.println(result.message());
                    } catch (Exception ex) {
                        System.out.println("Failed to observe game");
                    }
                    break;
                case "observe":
                    if(!isLoggedIn){
                        System.out.println(loginString);
                    }
                    if (parts.length != 2){
                        System.out.println("Usage: observe <ID>");
                        break;
                    }
                    try {
                        gameId = Integer.parseInt(parts[1]);
                    } catch (Exception ex){
                        System.out.println("<ID> Must be a number");
                        break;
                    }
                    try {
                        var result = server.observeGame(gameId);
                        System.out.println(result.message());
                    } catch (Exception ex) {
                        System.out.println("Failed to observe game");
                    }
                    break;
                case "play":
                    ChessGame.TeamColor teamColor;
                    if(!isLoggedIn){
                        System.out.println(loginString);
                    }
                    if (parts.length != 3){
                        System.out.println("Usage: play <ID> [WHITE|BLACK]");
                        break;
                    }
                    try {
                        gameId = Integer.parseInt(parts[1]);
                    } catch (Exception ex){
                        System.out.println("<ID> Must be a number");
                    }
                    try {
                        teamColor = ChessGame.TeamColor.valueOf(parts[2].toUpperCase());
                    } catch (Exception ex){
                        System.out.println("Team Color must be WHITE or BLACK");
                        break;
                    }
                    try {
                        var result = server.playGame(new JoinGameRequest(teamColor, gameId, authToken));
                        System.out.println(result.message());
                    } catch (Exception ex) {
                        System.out.println("Failed to create game");
                    }
                    break;
                case "quit":
                    System.out.println("Exiting chess client...");
                    isRunning = false;
                    break;

                default:
                    System.out.println("Unknown command. Type 'help' for available commands.");
            }
        }

        scanner.close();
    }
}
