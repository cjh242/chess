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
    @DisplayName("Logout Valid Test")
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
    @DisplayName("IsAuthValid Valid Test")
    public void isAuthValid() throws DataAccessException {
        var auth = authService.addAuth("testUser");

        assertTrue(authService.isAuthValid(auth));
    }

    @Test
    @DisplayName("IsAuthValid Invalid Test")
    public void badIsAuthValid() throws DataAccessException {
        assertFalse(authService.isAuthValid(new AuthData("BAD TOKEN", "testUser")));
    }

    @Test
    @DisplayName("Add Auth Valid Test")
    public void addAuth() throws DataAccessException {
        var auth = authService.addAuth("testUser");
        var retrieved = authService.getAuthByID(auth.authToken());

        assertEquals(auth, retrieved);
    }

    // TODO: FINISH THIS TEST
    @Test
    @DisplayName("Add Auth Bad Test")
    public void badAddAuth() throws DataAccessException {
        var auth = authService.addAuth("testUser");
        var retrieved = authService.getAuthByID(auth.authToken());

        assertEquals(auth, retrieved);
    }

    @Test
    @DisplayName("Get By Id Valid Test")
    public void getByID() throws DataAccessException {
        var auth = authService.addAuth("testUser");
        var retrievedAuth = authService.getAuthByID(auth.authToken());

        assertEquals(auth, retrievedAuth);
    }

    @Test
    @DisplayName("Get By Id InValid Test")
    public void badGetByID() throws DataAccessException {
        var auth = authService.addAuth("testUser");
        var retrievedAuth = authService.getAuthByID("WRONG TOKEN");

        assertNull(retrievedAuth);
    }


}
