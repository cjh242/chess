import client.ServerFacade;
import request.LoginRequest;
import request.RegisterRequest;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class ChessClient {

    private final static Collection<String> preLoginCommands = List.of("help", "quit", "login", "register");
    private final static Collection<String> postLoginCommands =
            List.of("help", "logout", "create game", "list games", "play game", "observe game");

    public void RunChessClient(){
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        boolean loggedIn = false;

        ServerFacade server = new ServerFacade();

        System.out.println("\uD83C\uDF1F Welcome to 240 Chess. Type Help to get started. \uD83C\uDF1F");

        while (running) {
            System.out.print(loggedIn ? "\n[LOGGED_IN] >>> " : "\n[LOGGED_OUT] >>> ");
            String input = scanner.nextLine().trim();

            // Split input into command and arguments
            String[] parts = input.split("\\s+"); // Splits by spaces
            if (parts.length == 0) continue; // Ignore empty input

            String command = parts[0].toLowerCase(); // First part is the command

            switch (command) {
                case "help":
                    System.out.println("  register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
                    System.out.println("  login <USERNAME> <PASSWORD> - to play chess");
                    System.out.println("  quit - playing chess");
                    System.out.println("  help - with possible commands");
                    break;

                case "register":
                    if (parts.length < 4) {
                        System.out.println("Usage: register <USERNAME> <PASSWORD> <EMAIL>");
                        break;
                    }
                    try {
                        var result = server.register(new RegisterRequest(parts[1], parts[2], parts[3]));
                        System.out.println(result);
                    } catch (Exception ex) {
                        System.out.println("Failed to register user");
                    }
                    break;

                case "login":
                    if (parts.length < 3) {
                        System.out.println("Usage: login <USERNAME> <PASSWORD>");
                        break;
                    }
                    try {
                        var result = server.login(new LoginRequest(parts[1], parts[2]));
                        System.out.println(result);
                    } catch (Exception ex) {
                        System.out.println("Failed to login user");
                    }

                    break;

                case "quit":
                    System.out.println("Exiting chess client...");
                    running = false;
                    break;

                default:
                    System.out.println("Unknown command. Type 'help' for available commands.");
            }
        }

        scanner.close();
    }
}
