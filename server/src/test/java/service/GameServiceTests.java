package service;

import chess.ChessGame;
import dataaccess.*;
import dataobjects.AuthData;
import dataobjects.GameData;
import dataobjects.UserData;
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
    void clear() {
        gameService.deleteAllGames();
        authService.deleteAllAuths();
    }

    @Test
    @DisplayName("Add Game Valid Test")
    public void addGame() throws DataAccessException {
        var auth = addAuthForTests();
        var gameRequest = new CreateGameRequest(auth.authToken(), "");
        var game = gameService.addGame(gameRequest);

        var games = gameService.listGames(auth.authToken());
        assertEquals(1, games.games().size());
        assertTrue(games.games().stream().anyMatch(x -> x.gameID() == game.gameID()));
    }

    @Test
    @DisplayName("Add Game Unauthorized")
    public void unauthorizedAddGame(){
        var result = gameService.addGame(new CreateGameRequest("BAD TOKEN", ""));

        assertEquals(401, result.code());
    }

    @Test
    @DisplayName("List Games Valid Test")
    public void listGames() throws DataAccessException {
        var auth = addAuthForTests();

        var game1 = gameService.addGame(new CreateGameRequest(auth.authToken(), ""));
        var game2 = gameService.addGame(new CreateGameRequest(auth.authToken(), ""));
        var game3 = gameService.addGame(new CreateGameRequest(auth.authToken(), ""));

        List<GameData> expected = new ArrayList<>();
        expected.add(new GameData(game1.gameID(), null, null, ""));
        expected.add(new GameData(game2.gameID(), null, null, ""));
        expected.add(new GameData(game3.gameID(), null, null, ""));

        var actual = gameService.listGames(auth.authToken()).games();
        assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("List Games Unauthorized Test")
    public void listGamesUnauthorized() {
        var result = gameService.listGames("BAD TOKEN");

        assertEquals(401, result.code());
    }

    @Test
    @DisplayName("Delete All Games Valid Test")
    public void deleteAllGames() throws DataAccessException {
        var auth = addAuthForTests();
        gameService.addGame(new CreateGameRequest(auth.authToken(), ""));
        gameService.addGame(new CreateGameRequest(auth.authToken(), ""));
        gameService.addGame(new CreateGameRequest(auth.authToken(), ""));

        gameService.deleteAllGames();

        var games = gameService.listGames(auth.authToken());
        assertEquals(0, games.games().size());
    }

    @Test
    @DisplayName("Join Game Valid Test")
    public void joinGame() throws DataAccessException {
        var auth = addAuthForTests();
        var user = addUserForTests();
        var game = addGameForTests();
        var joinReq = new JoinGameRequest(ChessGame.TeamColor.BLACK, game.gameID(), auth.authToken());
        var result = gameService.joinGame(joinReq);
        game = game.addBlackUsername(user.username());

        var gamesResult = gameService.listGames(auth.authToken());
        var games = new ArrayList<>(gamesResult.games());

        assertEquals(1, games.size());
        assertEquals(game.gameID(), games.getFirst().gameID());
        assertEquals(200, result.code());
    }

    @Test
    @DisplayName("Join Game Unauthorized Test")
    public void unAuthorizedJoinGame() {
        var game = addGameForTests();
        var joinReq = new JoinGameRequest(ChessGame.TeamColor.BLACK, game.gameID(), "BAD TOKEN");
        var result = gameService.joinGame(joinReq);

        assertEquals(401, result.code());
    }

    @Test
    @DisplayName("Join Game Bad ID Test")
    public void badIDJoinGame() throws DataAccessException {
        var auth = addAuthForTests();
        var joinReq = new JoinGameRequest(ChessGame.TeamColor.BLACK, 999, auth.authToken());
        var result = gameService.joinGame(joinReq);

        assertEquals(400, result.code());
    }

    @Test
    @DisplayName("Join Game Already Taken Test")
    public void alreadyTakenJoinGame() throws DataAccessException {
        var auth = addAuthForTests();
        addUserForTests();
        var game = addGameForTests();
        var joinReq = new JoinGameRequest(ChessGame.TeamColor.BLACK, game.gameID(), auth.authToken());
        gameService.joinGame(joinReq);

        var result = gameService.joinGame(joinReq);

        assertEquals(403, result.code());
    }

    private AuthData addAuthForTests() throws DataAccessException {
        return authService.addAuth("testUser");
    }

    private UserData addUserForTests() throws DataAccessException {
        return userDao.addUser(new RegisterRequest("testUser", "password", "test@email.com"));
    }

    private GameData addGameForTests() {
        var game = new GameData(1, null, null, "test");
        //calling update here will actually add the game, and skip normal adding protocols
        return gameDao.update(game);
    }



}
