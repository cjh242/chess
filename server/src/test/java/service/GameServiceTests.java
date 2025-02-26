package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataobjects.AuthData;
import dataobjects.GameData;
import exception.ResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    static final AuthService authService = new AuthService( new MemoryAuthDAO());
    static final GameService gameService = new GameService(new MemoryGameDAO(), authService);

    @BeforeEach
    void clear() throws ResponseException {
        gameService.deleteAllGames();
        authService.deleteAllAuths();
    }

    @Test
    @DisplayName("Add Game Test")
    public void addGame() throws ResponseException {
        var auth = addAuthForTests();
        var gameRequest = new CreateGameRequest(auth.authToken(), "");
        var game = gameService.addGame(gameRequest);

        var games = gameService.listGames(auth.authToken());
        assertEquals(1, games.size());
        assertTrue(games.contains(game));
    }

    @Test
    @DisplayName("List Games Test")
    public void listGames() throws ResponseException {
        var auth = addAuthForTests();
        List<GameData> expected = new ArrayList<>();
        expected.add(gameService.addGame(new CreateGameRequest(auth.authToken(), "")));
        expected.add(gameService.addGame(new CreateGameRequest(auth.authToken(), "")));
        expected.add(gameService.addGame(new CreateGameRequest(auth.authToken(), "")));

        var actual = gameService.listGames(auth.authToken());
        assertIterableEquals(expected, actual);
    }

    private AuthData addAuthForTests() throws ResponseException {
        return authService.addAuth("test");
    }


}
