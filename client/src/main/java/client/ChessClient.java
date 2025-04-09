package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
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
import static chess.ChessPiece.PieceType.*;

public class ChessClient {

    private int port;
    private WebSocketFacade ws = null;
    private int gameID;
    private ChessGame.TeamColor perspective = WHITE;
    private final NotificationCentral notificationCentral;

    public ChessClient(){
        this.port = -1;
        this.notificationCentral = new NotificationCentral();
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
        int gameNumber = 0;
        List<GameData> games = new ArrayList<>();

        ServerFacade server = new ServerFacade(port);

        System.out.println("\uD83C\uDF1F Welcome to 240 Chess. Type Help to get started. \uD83C\uDF1F");

        while (isRunning) {
            if (isLoggedIn && isInGameplay) {
                System.out.print("\n[GAMEPLAY] >>> ");
            } else if (isLoggedIn) {
                System.out.print("\n[LOGGED_IN] >>> ");
            } else {
                System.out.print("\n[LOGGED_OUT] >>> ");
            }

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
                    perspective = WHITE;
                    break;
                case "redraw":
                    handleRedraw(isLoggedIn, isInGameplay, authToken);
                    break;
                case "leave":
                    handleLeave(isLoggedIn, isInGameplay, authToken);
                    isInGameplay = false;
                    isObserving = false;
                    break;
                case "move":
                    handleMove(parts, isLoggedIn, isInGameplay, authToken, isObserving);
                    break;
                case "resign":
                    handleResign(isLoggedIn, isInGameplay, authToken, isObserving);
                    isInGameplay = false;
                    isObserving = false;
                    break;
                case "highlight":
                    handleHighlight(parts, isLoggedIn, isInGameplay);
                    break;
                default:
                    System.out.println("Unknown command. Type 'help' for available commands.");
            }
        }

        scanner.close();
    }

    private void handleRedraw(boolean isLoggedIn, boolean isInGameplay, String authToken){
        if(!isLoggedIn){
            System.out.println("Must be logged in, and in a game to redraw");
        }
        if(!isInGameplay){
            System.out.println("Must be in a game to redraw");
        }
        PrintingHelper.printBoard(notificationCentral.getGame().game().getBoard(), -1, null, perspective);
    }

    private void handleLeave(boolean isLoggedIn, boolean isInGameplay, String authToken){
        if(!isLoggedIn){
            System.out.println("Must be logged in, and in a game to leave");
        }
        if(!isInGameplay){
            System.out.println("Must be in a game to leave");
        }
        ws.leave(authToken, gameID);
        perspective = WHITE;
        System.out.println("You have left the game");
    }

    private void handleMove(String[] parts, boolean isLoggedIn, boolean isInGameplay, String authToken, boolean isObserving){
        if(!isLoggedIn){
            System.out.println("Must be logged in, and in a game to move");
            return;
        }
        if(!isInGameplay){
            System.out.println("Must be in a game to move");
            return;
        }
        if(isObserving){
            System.out.println("Observers cannot move");
            return;
        }
        if(parts.length != 3 && parts.length != 4){
            System.out.println("Usage: move <PIECE_STARTING_LOCATION> <PIECE_ENDING_LOCATION> [OPTIONAL_PROMOTION_PIECE]- a piece at a location to a provided location, if valid");
            return;
        }

        //READ POSITIONS
        if(parts[1].length() != 2){
            System.out.println("Position should be two characters. Example: a4");
            return;
        }
        var colChar = parts[1].charAt(0);
        var rowChar = parts[1].charAt(1);
        // Validate column character ('a' to 'h')
        if (Character.isLetter(colChar) && (colChar < 'a' || colChar > 'h')) {
            System.out.println("Invalid column character in notation: " + colChar);
            return;
        }
        // Validate row character ('1' to '8')
        if (Character.isDigit(rowChar) && (rowChar < '1' || rowChar > '8')) {
            System.out.println("Invalid row character in notation: " + rowChar);
            return;
        }
        int col = colChar - 'a' + 1;
        int row = Character.getNumericValue(rowChar);
        var startPosition = new ChessPosition(row, col);

        if(parts[2].length() != 2){
            System.out.println("Position should be two characters. Example: a4");
            return;
        }
        colChar = parts[2].charAt(0);
        rowChar = parts[2].charAt(1);
        // Validate column character ('a' to 'h')
        if (Character.isLetter(colChar) && (colChar < 'a' || colChar > 'h')) {
            System.out.println("Invalid column character in notation: " + colChar);
            return;
        }
        // Validate row character ('1' to '8')
        if (Character.isDigit(rowChar) && (rowChar < '1' || rowChar > '8')) {
            System.out.println("Invalid row character in notation: " + rowChar);
            return;
        }
        col = colChar - 'a' + 1;
        row = Character.getNumericValue(rowChar);
        var endPosition = new ChessPosition(row, col);

        if(parts.length == 3){
            var move = new ChessMove(startPosition, endPosition, null);
            ws.makeMove(authToken, gameID, move);
        }

        if(parts.length == 4 && parts[3].length() != 1){
            System.out.println("Promotion piece must be: Q, K, B, R");
            return;
        }

        ChessMove move = null;
        switch (parts[3].toLowerCase()) {
            case "q":
                move = new ChessMove(startPosition, endPosition, QUEEN);
                break;
            case "k":
                move = new ChessMove(startPosition, endPosition, KNIGHT);
                break;
            case "b":
                move = new ChessMove(startPosition, endPosition, BISHOP);
                break;
            case "r":
                move = new ChessMove(startPosition, endPosition, ROOK);
                break;
            default:
                System.out.println("Promotion piece must be: Q, K, B, R");
                return;
        }

        ws.makeMove(authToken, gameID, move);
    }

    private void handleResign(boolean isLoggedIn, boolean isInGameplay, String authToken, boolean isObserving){
        if(!isLoggedIn){
            System.out.println("Must be logged in, and in a game to resign");
        }
        if(!isInGameplay){
            System.out.println("Must be in a game to resign");
        }
        if(isObserving){
            System.out.println("Observers cannot resign");
        }
        ws.resign(authToken, gameID);
        perspective = WHITE;
        System.out.println("You have resigned from the game");
    }

    private void handleHighlight(String[] parts, boolean isLoggedIn, boolean isInGameplay){
        if(!isLoggedIn){
            System.out.println("Must be logged in, and in a game to highlight");
            return;
        }
        if(!isInGameplay){
            System.out.println("Must be in a game to highlight");
            return;
        }
        if (parts.length != 2) {
            System.out.println("Usage: highlight <PIECE_LOCATION>");
            System.out.println("Example: highlight a4");
            return;
        }
        //READ POSITION
        if(parts[1].length() != 2){
            System.out.println("Position should be two characters. Example: a4");
            return;
        }
        var colChar = parts[1].charAt(0);
        var rowChar = parts[1].charAt(1);
        // Validate column character ('a' to 'h')
        if (Character.isLetter(colChar) && (colChar < 'a' || colChar > 'h')) {
            System.out.println("Invalid column character in notation: " + colChar);
        }
        // Validate row character ('1' to '8')
        if (Character.isDigit(rowChar) && (rowChar < '1' || rowChar > '8')) {
            System.out.println("Invalid row character in notation: " + rowChar);
        }
        int col = colChar - 'a' + 1;
        int row = Character.getNumericValue(rowChar);
        var position = new ChessPosition(row, col);

        //GET THE VALID MOVES
        var moves = notificationCentral.getGame().game().validMoves(position);
        List<ChessPosition> positions = new ArrayList<>();
        positions.add(position);

        for (var move: moves){
            positions.add(move.getEndPosition());
        }

        //PRINT
        PrintingHelper.highlightValidMoves(notificationCentral.getGame().game().getBoard(), perspective, positions);
    }

    private void printHelp(boolean isLoggedIn, boolean isInGameplay) {
        if(isInGameplay){
            System.out.println("  redraw - the board");
            System.out.println("  leave - the game");
            System.out.println("  move <PIECE_STARTING_LOCATION> <PIECE_ENDING_LOCATION> [OPTIONAL_PROMOTION_PIECE]- a piece at a location to a provided location, if valid");
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
            perspective = WHITE;
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
            ws = new WebSocketFacade(port, notificationCentral, WHITE);
            ws.connect(authToken, game.gameID());
            gameID = game.gameID();
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
            perspective = teamColor;
            var game = games.get(gameNumber);
            var result = server.playGame(new JoinGameRequest(teamColor, game.gameID(), authToken));
            System.out.println(result.message());
            ws = new WebSocketFacade(port, notificationCentral, teamColor);
            ws.connect(authToken, game.gameID());
            gameID = game.gameID();
        } catch (NumberFormatException ex) {
            System.out.println("<ID> Must be a number");
        } catch (IllegalArgumentException ex) {
            System.out.println("Team Color must be WHITE or BLACK");
        } catch (Exception ex) {
            System.out.println("Failed to join game");
        }
    }
}
