package client;

import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
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
    public void playGameValid(){

    }

    @Test
    public void playGameBadRequest(){

    }

    @Test
    public void listGamesValid(){

    }

    @Test
    public void listGamesNoGames(){

    }

}
