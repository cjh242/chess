package client;

import chess.ChessGame;
import dataobjects.GameData;
import request.*;
import result.HttpResult;
import service.PrintingHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChessClient {

    public void runChessClient(int port) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        boolean isLoggedIn = false;
        boolean hasListedGames = false;
        String authToken = null;
        int gameNumber = 0;
        List<GameData> games = new ArrayList<>();

        ServerFacade server = new ServerFacade(port);

        System.out.println("\uD83C\uDF1F Welcome to 240 Chess. Type Help to get started. \uD83C\uDF1F");

        while (isRunning) {
            System.out.print(isLoggedIn ? "\n[LOGGED_IN] >>> " : "\n[LOGGED_OUT] >>> ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");
            if (parts.length == 0) continue;

            String command = parts[0].toLowerCase();

            switch (command) {
                case "help":
                    printHelp(isLoggedIn);
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
                    handleObserve(parts, isLoggedIn, hasListedGames, games);
                    break;
                case "play":
                    handlePlay(parts, isLoggedIn, hasListedGames, games, authToken, server);
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

    private void printHelp(boolean isLoggedIn) {
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
            for (var game : result.games()) {
                PrintingHelper.printBoard(game.game().getBoard(), i++, game.gameName(), ChessGame.TeamColor.WHITE);
            }
            return result;
        } catch (Exception ex) {
            System.out.println("Failed to list games");
            return null;
        }
    }

    private void handleObserve(String[] parts, boolean isLoggedIn, boolean hasListedGames, List<GameData> games) {
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
            PrintingHelper.printBoard(game.game().getBoard(), gameNumber, game.gameName(), ChessGame.TeamColor.WHITE);
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
            PrintingHelper.printBoard(game.game().getBoard(), gameNumber, game.gameName(), teamColor);
        } catch (NumberFormatException ex) {
            System.out.println("<ID> Must be a number");
        } catch (IllegalArgumentException ex) {
            System.out.println("Team Color must be WHITE or BLACK");
        } catch (Exception ex) {
            System.out.println("Failed to join game");
        }
    }


}
