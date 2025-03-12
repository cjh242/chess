package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataobjects.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


public class GameDAO implements IGameDAO{
    private final DatabaseAssistant dbAssist;

    public GameDAO() throws DataAccessException {
        this.dbAssist = new DatabaseAssistant();
        dbAssist.configureDatabase();
    }

    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Failure to access games");
        }
        return result;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var id = rs.getInt("gameID");
        var wUsername = rs.getString("whiteUsername");
        var bUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var json = rs.getString("game");
        var game = new Gson().fromJson(json, ChessGame.class);
        return new GameData(id, wUsername, bUsername, gameName, game);
    }

    public GameData addGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(new ChessGame());
        var id = dbAssist.executeUpdate(statement, null, null, gameName, json);
        return new GameData(id, null, null, gameName, new ChessGame());
    }

    public void deleteAllGames() throws DataAccessException {
        var statement = "TRUNCATE game";
        dbAssist.executeUpdate(statement);
    }

    public GameData findByID(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameId=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Failed to execute query");
        }
        return null;
    }

    public GameData update(GameData game) throws DataAccessException {
        var statement = "UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE primary_key_column=?;";
        var json = new Gson().toJson(game);
        var id = dbAssist.executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), json, game.gameID());
        return findByID(id);
    }
}
