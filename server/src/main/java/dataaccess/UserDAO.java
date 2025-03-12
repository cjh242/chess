package dataaccess;

import dataobjects.UserData;
import request.RegisterRequest;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAO implements IUserDAO{
    private final DatabaseAssistant dbAssist;

    public UserDAO() throws DataAccessException {
        this.dbAssist = new DatabaseAssistant();
        dbAssist.configureDatabase();
    }

    public UserData addUser(RegisterRequest request) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        dbAssist.executeUpdate(statement, request.username(), request.password(), request.email());
        return new UserData(request.username(), request.password(), request.email());
    }

    public UserData getUserByUsername(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Failed to execute query");
        }
        return null;
    }

    public void deleteAllUsers() throws DataAccessException {
        var statement = "TRUNCATE user";
        dbAssist.executeUpdate(statement);
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }
}
