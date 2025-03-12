package dataaccess;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseAssistant {

    private final String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS game (
          gameID INT NOT NULL AUTO_INCREMENT,
          whiteUsername VARCHAR(256) NULL,
          blackUsername VARCHAR(256) NULL,
          gameName VARCHAR(256) NOT NULL,
          game BLOB NOT NULL,
          PRIMARY KEY (gameID)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """,

        """
        CREATE TABLE IF NOT EXISTS user (
          username VARCHAR(256) NOT NULL,
          password VARCHAR(256) NOT NULL,
          email VARCHAR(256) NOT NULL,
          PRIMARY KEY (username)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """,

        """
        CREATE TABLE IF NOT EXISTS auth (
          authToken VARCHAR(256) NOT NULL,
          username VARCHAR(256) NOT NULL,
          PRIMARY KEY (authToken)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """
    };


    public void configureDatabase() {
        try {
            DatabaseManager.createDatabase();
            System.out.println("Success creating database or database already created");
        } catch (Exception ex) {
            System.err.println("Failed to create database: " + ex.getMessage());
        }
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
            System.out.println("Success connecting to database");
        } catch (SQLException | DataAccessException ex) {
            System.err.println("Failed to create database: " + ex.getMessage());
        }
    }

    public int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p){ ps.setString(i + 1, p); }
                    else if (param instanceof Integer p){ ps.setInt(i + 1, p); }
                    else if (param == null){ ps.setNull(i + 1, NULL); }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
}
