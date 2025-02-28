package service;

import chess.ChessGame;
import dataaccess.*;
import dataobjects.AuthData;
import dataobjects.GameData;
import dataobjects.UserData;
import exception.ResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.RegisterRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    static final IGameDAO gameDao = new MemoryGameDAO();
    static final IUserDAO userDao = new MemoryUserDAO();
    static final AuthService authService = new AuthService( new MemoryAuthDAO());
    static final GameService gameService = new GameService(gameDao, authService);

    @BeforeEach
    void clear() throws ResponseException {
        gameService.deleteAllGames();
        authService.deleteAllAuths();
    }

    @Test
    @DisplayName("Add Game Valid Test")
    public void addGame() throws ResponseException {
        var auth = addAuthForTests();
        var gameRequest = new CreateGameRequest(auth.authToken(), "");
        var game = gameService.addGame(gameRequest);

        var games = gameService.listGames(auth.authToken());
        assertEquals(1, games.size());
        assertTrue(games.contains(game));
    }

    @Test
    @DisplayName("List Games Valid Test")
    public void listGames() throws ResponseException {
        var auth = addAuthForTests();
        List<GameData> expected = new ArrayList<>();
        expected.add(gameService.addGame(new CreateGameRequest(auth.authToken(), "")));
        expected.add(gameService.addGame(new CreateGameRequest(auth.authToken(), "")));
        expected.add(gameService.addGame(new CreateGameRequest(auth.authToken(), "")));

        var actual = gameService.listGames(auth.authToken());
        assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("Delete All Games Valid Test")
    public void deleteAllGames() throws ResponseException {
        var auth = addAuthForTests();
        gameService.addGame(new CreateGameRequest(auth.authToken(), ""));
        gameService.addGame(new CreateGameRequest(auth.authToken(), ""));
        gameService.addGame(new CreateGameRequest(auth.authToken(), ""));

        gameService.deleteAllGames();

        var games = gameService.listGames(auth.authToken());
        assertEquals(0, games.size());
    }

    @Test
    @DisplayName("Join Game Valid Test")
    public void joinGame() throws ResponseException {
        var auth = addAuthForTests();
        var user = addUserForTests();
        var game = new GameData(1, null, null, "test", new ChessGame());
        //calling update here will actually add the game, and skip normal adding protocols
        gameDao.update(game);
        var joinReq = new JoinGameRequest(ChessGame.TeamColor.BLACK, game.gameID(), auth.authToken());
        gameService.joinGame(joinReq);
        game = game.addBlackUsername(user.username());

        var games = gameService.listGames(auth.authToken());
        assertEquals(1, games.size());
        assertTrue(games.contains(game));
    }

    private AuthData addAuthForTests() throws ResponseException {
        return authService.addAuth("testUser");
    }

    private UserData addUserForTests() throws ResponseException {
        return userDao.addUser(new RegisterRequest("testUser", "password", "test@email.com"));
    }



}
