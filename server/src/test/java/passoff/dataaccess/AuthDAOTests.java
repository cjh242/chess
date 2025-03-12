package passoff.dataaccess;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTests {
    private final AuthDAO authDao = new AuthDAO();


    @BeforeEach
    void clear() throws DataAccessException {
        authDao.deleteAllAuths();
    }

    @Test
    @DisplayName("Add Auth")
    public void addAuth() throws DataAccessException {

        var auth = authDao.addAuth("TEST_USER");

        var retrievedAuth = authDao.getAuthByToken(auth.authToken());

        assertEquals(auth, retrievedAuth);
    }

    @Test
    @DisplayName("Add Auth Fails")
    public void addAuthBad() {

        assertThrows(DataAccessException.class, () -> authDao.addAuth(null));
    }

    @Test
    @DisplayName("Get Auth")
    public void getAuth() throws DataAccessException {
        var auth = authDao.addAuth("TEST_USER");

        var retrievedAuth = authDao.getAuthByToken(auth.authToken());

        assertEquals(auth, retrievedAuth);
    }

    @Test
    @DisplayName("Get Auth Invalid")
    public void getAuthBad() throws DataAccessException {

        var retrievedAuth = authDao.getAuthByToken("TEST_USER");

        assertNull(retrievedAuth);
    }

    @Test
    @DisplayName("Delete Auth")
    public void deleteAuth() throws DataAccessException {

        var auth = authDao.addAuth("TEST_USER");

        authDao.deleteAuth(auth.authToken());

        var retrievedAuth = authDao.getAuthByToken(auth.authToken());

        assertNull(retrievedAuth);
    }

    @Test
    @DisplayName("Delete Auth")
    public void deleteAuthBad() throws DataAccessException {

        var retrievedAuth = authDao.getAuthByToken("authToken");
        authDao.deleteAuth("authToken");

        assertNull(retrievedAuth);
    }
}
