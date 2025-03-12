package passoff.dataaccess;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataobjects.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTests {
    private final GameDAO gameDao = new GameDAO();

    @BeforeEach
    void clear() throws DataAccessException {
        gameDao.deleteAllGames();
    }

    @Test
    @DisplayName("Add Game")
    public void addGame() throws DataAccessException {
        var game = gameDao.addGame("NEW_GAME");

        var retrievedGame = gameDao.findByID(game.gameID());

        assertEquals(game, retrievedGame);
    }

    @Test
    @DisplayName("Add Game Null")
    public void addGameBad() {
        assertThrows(DataAccessException.class, () -> {
            gameDao.addGame(null);
        });
    }

    @Test
    @DisplayName("Update Game")
    public void updateGame() throws DataAccessException {

        var game = gameDao.addGame("NEW_GAME");
        var updatedGame = game.addWhiteUsername("TEST_USER");
        gameDao.update(updatedGame);
        var retrievedGame = gameDao.findByID(updatedGame.gameID());

        assertEquals(updatedGame, retrievedGame);
    }

    @Test
    @DisplayName("Update Game Null")
    public void updateGameBad() throws DataAccessException {

        var game = gameDao.addGame("NEW_GAME");
        var updatedGame = new GameData(game.gameID(), null, null, null, null);
        assertThrows(DataAccessException.class, () -> {
            gameDao.update(updatedGame);
        });
    }

    @Test
    @DisplayName("Get Game")
    public void getGame() throws DataAccessException {

        var game = gameDao.addGame("NEW_GAME");
        var retrievedGame = gameDao.findByID(game.gameID());
        assertEquals(game, retrievedGame);

    }

    @Test
    @DisplayName("Get Game Null")
    public void getGameNull() throws DataAccessException {
        var retrievedGame = gameDao.findByID(1);
        assertNull(retrievedGame);
    }

    @Test
    @DisplayName("List Games")
    public void listGames() throws DataAccessException {
        var game = gameDao.addGame("NEW_GAME");
        var game2 = gameDao.addGame("OTHER_GAME");
        var games = gameDao.listGames();

        assertTrue(games.contains(game));
        assertTrue(games.contains(game2));
        assertEquals(2, games.size());
    }

    @Test
    @DisplayName("List Empty Games")
    public void listGamesNone() throws DataAccessException {

        var games = gameDao.listGames();

        assertEquals(0, games.size());
    }


}
