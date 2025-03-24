package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ChessClient client;
    private static ServerFacade facade;

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
    public void loginValid() {

        Assertions.assertTrue(true);
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
