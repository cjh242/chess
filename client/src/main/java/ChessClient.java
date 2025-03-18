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

        System.out.println("\uD83C\uDF1F Welcome to 240 Chess. Type Help to get started. \uD83C\uDF1F");

        while (running) {
            System.out.print("\n[LOGGED_OUT] >>> ");
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
                    String regUsername = parts[1];
                    String regPassword = parts[2];
                    String regEmail = parts[3];
                    System.out.println("Registering user: " + regUsername + " with email: " + regEmail);
                    break;

                case "login":
                    if (parts.length < 3) {
                        System.out.println("Usage: login <USERNAME> <PASSWORD>");
                        break;
                    }
                    String loginUsername = parts[1];
                    String loginPassword = parts[2];
                    System.out.println("Logging in user: " + loginUsername);
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
