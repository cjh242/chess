package dataaccess;

import dataobjects.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthDAO implements IAuthDAO{
    private final DatabaseAssistant dbAssist;

    public AuthDAO() {
        this.dbAssist = new DatabaseAssistant();
        dbAssist.configureDatabase();
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        dbAssist.executeUpdate(statement, authToken);
    }


    public AuthData getAuthByToken(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Failed to execute query");
        }
        return null;
    }


    public AuthData addAuth(String username) throws DataAccessException {
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        var token = UUID.randomUUID().toString();
        dbAssist.executeUpdate(statement, token, username);
        return new AuthData(token, username);
    }


    public void deleteAllAuths() throws DataAccessException {
        var statement = "TRUNCATE auth";
        dbAssist.executeUpdate(statement);
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        return new AuthData(authToken, username);
    }
}
