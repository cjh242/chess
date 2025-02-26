package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
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
    static final GameService gameService = new GameService(new MemoryGameDAO(), new AuthService(new MemoryAuthDAO()));

    @BeforeEach
    void clear() throws ResponseException {
        gameService.deleteAllGames();
    }

    @Test
    @DisplayName("Add Game Test")
    public void addGame() throws ResponseException {
        var gameRequest = new CreateGameRequest("", "");
        var game = gameService.addGame(gameRequest);

        var games = gameService.listGames();
        assertEquals(1, games.size());
        assertTrue(games.contains(game));
    }

    @Test
    @DisplayName("List Games Test")
    public void listGames() throws ResponseException {
        List<GameData> expected = new ArrayList<>();
        expected.add(gameService.addGame(new CreateGameRequest("", "")));
        expected.add(gameService.addGame(new CreateGameRequest("", "")));
        expected.add(gameService.addGame(new CreateGameRequest("", "")));

        var actual = gameService.listGames();
        assertIterableEquals(expected, actual);
    }


}
