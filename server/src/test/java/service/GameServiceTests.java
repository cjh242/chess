package service;

import chess.ChessGame;
import dataaccess.MemoryGameDAO;
import dataobjects.GameData;
import exception.ResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    static final GameService gameService = new GameService(new MemoryGameDAO());

    @BeforeEach
    void clear() throws ResponseException {
        gameService.deleteAllGames();
    }

    @Test
    @DisplayName("Add Game Test")
    public void addGame() throws ResponseException {
        var game = new GameData(0, "whiteUser", "blackUser", "testGame",
                new ChessGame());
        game = gameService.addGame(game);

        var games = gameService.listGames();
        assertEquals(1, games.size());
        assertTrue(games.contains(game));
    }

    @Test
    @DisplayName("List Games Test")
    public void listGames() throws ResponseException {
        List<GameData> expected = new ArrayList<>();
        expected.add(gameService.addGame(new GameData(
                0,
                "whiteUser1",
                "blackUser1",
                "testGame1",
                new ChessGame())));
        expected.add(gameService.addGame(new GameData(
                0,
                "whiteUser2",
                "blackUser2",
                "testGame2",
                new ChessGame())));
        expected.add(gameService.addGame(new GameData(
                0,
                "whiteUser3",
                "blackUser3",
                "testGame3",
                new ChessGame())));

        var actual = gameService.listGames();
        assertIterableEquals(expected, actual);
    }


}
