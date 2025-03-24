package client;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import request.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    private final String username = "TEST_USER";
    private final String password = "PASSWORD";
    private final String email = "EMAIL@MAIL.COM";

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @BeforeEach
    public void clearData() throws Exception {
        try{
            facade.deleteAll();
        } catch (Exception ex){
            System.out.println("FAILED TO DELETE ALL");
        }

    }

    @Test
    public void loginValid() throws Exception {

        var registerResult = facade.register(new RegisterRequest(username, password, email));
        var logoutResult = facade.logout(new LogoutRequest(registerResult.authToken()));

        var result = facade.login(new LoginRequest(username, password));

        Assertions.assertTrue(result.isOk());
        Assertions.assertEquals("Logged in as " + username, result.message());
    }

    @Test
    public void loginWrongPassword() throws Exception {
        var registerResult = facade.register(new RegisterRequest(username, password, email));
        facade.logout(new LogoutRequest(registerResult.authToken()));

        var result = facade.login(new LoginRequest(username, "WRONG_PASSWORD"));

        Assertions.assertFalse(result.isOk());
    }

    @Test
    public void registerValid() throws Exception {
        var result = facade.register(new RegisterRequest(username, password, email));

        Assertions.assertTrue(result.isOk());
        Assertions.assertEquals("Logged in as " + username, result.message());
    }

    @Test
    public void registerUsernameTaken() throws Exception {
        var registerResult = facade.register(new RegisterRequest(username, password, email));
        facade.logout(new LogoutRequest(registerResult.authToken()));

        var result = facade.register(new RegisterRequest(username, password, email));

        Assertions.assertFalse(result.isOk());
    }

    @Test
    public void logoutValid() throws Exception {
        var registerResult = facade.register(new RegisterRequest(username, password, email));

        var result = facade.logout(new LogoutRequest(registerResult.authToken()));

        Assertions.assertTrue(result.isOk());
    }

    @Test
    public void logoutNotLoggedIn() throws Exception {

        var result = facade.logout(new LogoutRequest("AUTH_TOKEN"));

        Assertions.assertFalse(result.isOk());
    }

    @Test
    public void createGameValid() throws Exception {
        var registerResult = facade.register(new RegisterRequest(username, password, email));

        var result = facade.createGame(new CreateGameRequest(registerResult.authToken(), "NEW_GAME"));

        Assertions.assertTrue(result.isOk());
    }

    @Test
    public void createGameBadAuth() throws Exception {
        var result = facade.createGame(new CreateGameRequest("AUTH", "NEW_GAME"));

        Assertions.assertFalse(result.isOk());
    }

    @Test
    public void playGameValid() throws Exception {
        var registerResult = facade.register(new RegisterRequest(username, password, email));
        var createResult = facade.createGame(new CreateGameRequest(registerResult.authToken(), "NEW_GAME"));
        var listResult = facade.listGames(registerResult.authToken());
        var game = listResult.games().getFirst();

        var result = facade.playGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, game.gameID(), registerResult.authToken()));

        Assertions.assertTrue(result.isOk());
    }

    @Test
    public void playGameBadAuthNoGame() throws Exception {
        var result = facade.playGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 0, "AUTH"));

        Assertions.assertFalse(result.isOk());
    }

    @Test
    public void listGamesValid() throws Exception {
        var registerResult = facade.register(new RegisterRequest(username, password, email));
        facade.createGame(new CreateGameRequest(registerResult.authToken(), "NEW_GAME1"));
        facade.createGame(new CreateGameRequest(registerResult.authToken(), "NEW_GAME2"));
        facade.createGame(new CreateGameRequest(registerResult.authToken(), "NEW_GAME3"));
        var result = facade.listGames(registerResult.authToken());

        Assertions.assertTrue(result.isOk());
        Assertions.assertEquals(3, result.games().size());
    }

    @Test
    public void listGamesNoGames() throws Exception {
        var registerResult = facade.register(new RegisterRequest(username, password, email));
        var result = facade.listGames(registerResult.authToken());

        Assertions.assertTrue(result.isOk());
        Assertions.assertTrue(result.games().isEmpty());
    }

}
