package client;

import chess.ChessGame;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import dataobjects.GameData;
import request.*;
import result.HttpResult;
import service.NotificationCentral;
import service.PrintingHelper;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.WHITE;

public class ChessClient {

    private int port;
    private WebSocketFacade ws = null;

    public ChessClient(){
        this.port = -1;
    }

    public void runChessClient(int port) {
        this.port = port;
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        boolean isLoggedIn = false;
        boolean hasListedGames = false;
        boolean isInGameplay = false;
        boolean isObserving = false;
        String authToken = null;
        WebSocketFacade ws = null;
        int gameNumber = 0;
        List<GameData> games = new ArrayList<>();

        ServerFacade server = new ServerFacade(port);

        System.out.println("\uD83C\uDF1F Welcome to 240 Chess. Type Help to get started. \uD83C\uDF1F");

        while (isRunning) {
            System.out.print(isLoggedIn ? "\n[LOGGED_IN] >>> " : "\n[LOGGED_OUT] >>> ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");
            if (parts.length == 0) {
                continue;
            }

            String command = parts[0].toLowerCase();

            switch (command) {
                case "help":
                    printHelp(isLoggedIn, isInGameplay);
                    break;
                case "register":
                    authToken = handleRegister(parts, server);
                    isLoggedIn = authToken != null;
                    break;
                case "login":
                    authToken = handleLogin(parts, server);
                    isLoggedIn = authToken != null;
                    break;
                case "logout":
                    isLoggedIn = !handleLogout(authToken, server);
                    authToken = isLoggedIn ? authToken : null;
                    break;
                case "create":
                    handleCreate(parts, authToken, isLoggedIn, server);
                    break;
                case "list":
                    var result = handleList(authToken, isLoggedIn, server);
                    if (result != null) {
                        games = result.games();
                        hasListedGames = !games.isEmpty();
                    }
                    break;
                case "observe":
                    handleObserve(parts, isLoggedIn, hasListedGames, games, authToken);
                    isInGameplay = true;
                    isObserving = true;
                    break;
                case "play":
                    handlePlay(parts, isLoggedIn, hasListedGames, games, authToken, server);
                    isInGameplay = true;
                    break;
                case "quit":
                    System.out.println("Exiting chess client...");
                    isRunning = false;
                    break;
                case "redraw":
                    handleRedraw(parts, isLoggedIn, hasListedGames, games, authToken, server);
                    break;
                case "leave":
                    handleLeave(parts, isLoggedIn, hasListedGames, games, authToken, server);
                    isInGameplay = false;
                    isObserving = false;
                    break;
                case "move":
                    handleMove(parts, isLoggedIn, hasListedGames, games, authToken, server);
                    break;
                case "resign":
                    handleResign(parts, isLoggedIn, hasListedGames, games, authToken, server);
                    isInGameplay = false;
                    isObserving = false;
                    break;
                case "highlight":
                    handleHighlight(parts, isLoggedIn, hasListedGames, games, authToken, server);
                    break;
                default:
                    System.out.println("Unknown command. Type 'help' for available commands.");
            }
        }

        scanner.close();
    }

    private void handleRedraw(){

    }

    private void handleLeave(){

    }

    private void handleMove(){

    }

    private void handleResign(){

    }

    private void handleHighlight(){

    }

    private void printHelp(boolean isLoggedIn, boolean isInGameplay) {
        if(isInGameplay){
            System.out.println("  redraw - the board");
            System.out.println("  leave - the game");
            System.out.println("  move <PIECE_STARTING_LOCATION> <PIECE_ENDING_LOCATION> - a piece at a location to a provided location, if valid");
            System.out.println("  resign - the game as a loss");
            System.out.println("  highlight <PIECE_LOCATION> - the valid moves for a piece at a location");
        }
        if (isLoggedIn) {
            System.out.println("  create <NAME> - a game");
            System.out.println("  list - games");
            System.out.println("  play <ID> [WHITE|BLACK] - a game");
            System.out.println("  observe <ID> - a game");
            System.out.println("  logout - when you are done");
        } else {
            System.out.println("  register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
            System.out.println("  login <USERNAME> <PASSWORD> - to play chess");
        }
        System.out.println("  quit - playing chess");
        System.out.println("  help - with possible commands");
    }

    private String handleRegister(String[] parts, ServerFacade server) {
        if (parts.length < 4) {
            System.out.println("Usage: register <USERNAME> <PASSWORD> <EMAIL>");
            return null;
        }
        try {
            var result = server.register(new RegisterRequest(parts[1], parts[2], parts[3]));
            System.out.println(result.message());
            return result.isOk() ? result.authToken() : null;
        } catch (Exception ex) {
            System.out.println("Failed to register user");
            return null;
        }
    }

    private String handleLogin(String[] parts, ServerFacade server) {
        if (parts.length < 3) {
            System.out.println("Usage: login <USERNAME> <PASSWORD>");
            return null;
        }
        try {
            var result = server.login(new LoginRequest(parts[1], parts[2]));
            System.out.println(result.message());
            return result.isOk() ? result.authToken() : null;
        } catch (Exception ex) {
            System.out.println("Failed to login user");
            return null;
        }
    }

    private boolean handleLogout(String authToken, ServerFacade server) {
        try {
            var result = server.logout(new LogoutRequest(authToken));
            System.out.println(result.message());
            return result.isOk();
        } catch (Exception ex) {
            System.out.println("Failed to logout user");
            return false;
        }
    }

    private void handleCreate(String[] parts, String authToken, boolean isLoggedIn, ServerFacade server) {
        if (!isLoggedIn) {
            System.out.println("Please first login or register");
            return;
        }
        if (parts.length != 2) {
            System.out.println("Usage: create <GAME_NAME>");
            return;
        }
        try {
            var result = server.createGame(new CreateGameRequest(authToken, parts[1]));
            System.out.println(result.message());
        } catch (Exception ex) {
            System.out.println("Failed to create game");
        }
    }

    private HttpResult handleList(String authToken, boolean isLoggedIn, ServerFacade server) {
        if (!isLoggedIn) {
            System.out.println("Please first login or register");
            return null;
        }
        try {
            var result = server.listGames(authToken);
            System.out.println(result.message());
            int i = 0;
            if(result.games().isEmpty()){
                System.out.println("No games to list");
                return null;
            }
            for (var game : result.games()) {
                PrintingHelper.printBoard(game.game().getBoard(), i++, game.gameName(), WHITE);
            }
            return result;
        } catch (Exception ex) {
            System.out.println("Failed to list games");
            return null;
        }
    }

    private void handleObserve(String[] parts, boolean isLoggedIn, boolean hasListedGames, List<GameData> games, String authToken) {
        if (!isLoggedIn) {
            System.out.println("Please first login or register");
            return;
        }
        if (!hasListedGames) {
            System.out.println("Please list games before observing");
            return;
        }
        if (parts.length != 2) {
            System.out.println("Usage: observe <ID>");
            return;
        }
        try {
            int gameNumber = Integer.parseInt(parts[1]);
            var game = games.get(gameNumber);
            ws = new WebSocketFacade(port, new NotificationCentral(), WHITE);
            ws.connect(authToken, game.gameID());
        } catch (Exception ex) {
            System.out.println("Failed to observe game");
        }
    }

    private void handlePlay(String[] parts, boolean isLoggedIn, boolean hasListedGames, List<GameData> games, String authToken, ServerFacade server) {
        if (!isLoggedIn) {
            System.out.println("Please first login or register");
            return;
        }
        if (!hasListedGames) {
            System.out.println("Please list games before attempting to join");
            return;
        }
        if (parts.length != 3) {
            System.out.println("Usage: play <ID> [WHITE|BLACK]");
            return;
        }
        try {
            int gameNumber = Integer.parseInt(parts[1]);
            var teamColor = ChessGame.TeamColor.valueOf(parts[2].toUpperCase());
            var game = games.get(gameNumber);
            var result = server.playGame(new JoinGameRequest(teamColor, game.gameID(), authToken));
            System.out.println(result.message());
            ws = new WebSocketFacade(port, new NotificationCentral(), teamColor);
            ws.connect(authToken, game.gameID());
        } catch (NumberFormatException ex) {
            System.out.println("<ID> Must be a number");
        } catch (IllegalArgumentException ex) {
            System.out.println("Team Color must be WHITE or BLACK");
        } catch (Exception ex) {
            System.out.println("Failed to join game");
        }
    }
}
