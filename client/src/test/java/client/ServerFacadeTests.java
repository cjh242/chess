package client;

import org.junit.jupiter.api.*;
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
        facade.deleteAll();
    }

    @Test
    public void loginValid() throws Exception {

        var registerResult = facade.register(new RegisterRequest(username, password, email));
        facade.logout(new LogoutRequest(registerResult.authToken()));

        var result = facade.login(new LoginRequest(username, password));

        Assertions.assertTrue(result.isOk());
    }

    @Test
    public void loginWrongPassword() {

    }

    @Test
    public void registerValid(){

    }

    @Test
    public void registerUsernameTaken(){

    }

    @Test
    public void logoutValid(){

    }

    @Test
    public void logoutNotLoggedIn(){

    }

    @Test
    public void createGameValid(){

    }

    @Test
    public void createGameBadRequest(){

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
