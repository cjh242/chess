package service;

import dataaccess.DataAccessException;
import dataaccess.IAuthDAO;
import dataaccess.MemoryAuthDAO;
import dataobjects.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.LogoutRequest;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTests {
    private final IAuthDAO authDao = new MemoryAuthDAO();
    private final AuthService authService = new AuthService(authDao);

    @BeforeEach
    void clear() throws DataAccessException {
        authDao.deleteAllAuths();
    }

    @Test
    @DisplayName("Logout Valid")
    public void logout() throws DataAccessException {
        var auth = authService.addAuth("testUser");
        authService.logout(new LogoutRequest(auth.authToken()));

        assertFalse(authService.isAuthValid(auth));
    }

    @Test
    @DisplayName("Logout Wrong Token")
    public void badLogout() throws DataAccessException {
        var auth = authService.addAuth("testUser");

        var result = authService.logout(new LogoutRequest("WRONG TOKEN"));
        assertEquals(401, result.code());
        assertTrue(authService.isAuthValid(auth));
    }

    @Test
    @DisplayName("IsAuthValid Valid")
    public void isAuthValid() throws DataAccessException {
        var auth = authService.addAuth("testUser");

        assertTrue(authService.isAuthValid(auth));
    }

    @Test
    @DisplayName("IsAuthValid Wrong Token")
    public void badIsAuthValid() {
        assertFalse(authService.isAuthValid(new AuthData("BAD TOKEN", "testUser")));
    }

    @Test
    @DisplayName("Add Auth Valid")
    public void addAuth() throws DataAccessException {
        var auth = authService.addAuth("testUser");
        var retrieved = authService.getAuthByID(auth.authToken());

        assertEquals(auth, retrieved);
    }

    // TODO: FINISH THIS TEST
    @Test
    @DisplayName("Add Auth Bad")
    public void badAddAuth() throws DataAccessException {
        String nullUsername = null;
        var auth = authService.addAuth(nullUsername);

        assertNull(auth);
    }

    @Test
    @DisplayName("Get By Id Valid")
    public void getByID() throws DataAccessException {
        var auth = authService.addAuth("testUser");
        var retrievedAuth = authService.getAuthByID(auth.authToken());

        assertEquals(auth, retrievedAuth);
    }

    @Test
    @DisplayName("Get By Id Wrong Token")
    public void badGetByID() throws DataAccessException {
        authService.addAuth("testUser");
        var retrievedAuth = authService.getAuthByID("WRONG TOKEN");

        assertNull(retrievedAuth);
    }

    @Test
    @DisplayName("Delete All")
    public void delete() throws DataAccessException {

        var test1 = authDao.addAuth("test1");
        var test2 = authDao.addAuth("test2");
        var test3 = authDao.addAuth("test3");

        authService.deleteAllAuths();

        var retrieved1 = authService.getAuthByID(test1.authToken());
        var retrieved2 = authService.getAuthByID(test2.authToken());
        var retrieved3 = authService.getAuthByID(test3.authToken());

        assertFalse(authService.isAuthValid(test1));
        assertFalse(authService.isAuthValid(test2));
        assertFalse(authService.isAuthValid(test3));
        assertNull(retrieved1);
        assertNull(retrieved2);
        assertNull(retrieved3);

    }


}
