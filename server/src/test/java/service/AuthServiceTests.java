package service;

import dataaccess.IAuthDAO;
import dataaccess.MemoryAuthDAO;
import exception.ResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.LogoutRequest;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTests {
    private final IAuthDAO authDao = new MemoryAuthDAO();
    private final AuthService authService = new AuthService(authDao);

    @BeforeEach
    void clear() throws ResponseException {
        authDao.deleteAllAuths();
    }

    @Test
    @DisplayName("Logout Valid Test")
    public void logout() throws ResponseException {
        var auth = authService.addAuth("testUser");
        authService.logout(new LogoutRequest(auth.authToken()));

        assertFalse(authService.isAuthValid(auth.authToken()));
    }

    @Test
    @DisplayName("IsAuthValid Valid Test")
    public void isAuthValid() throws ResponseException {
        var auth = authService.addAuth("testUser");

        assertTrue(authService.isAuthValid(auth.authToken()));
    }

    @Test
    @DisplayName("Add Auth Valid Test")
    public void addAuth() throws ResponseException {
        var auth = authService.addAuth("testUser");
        var retrieved = authService.getAuthByID(auth.authToken());

        assertEquals(auth, retrieved);
    }

    @Test
    @DisplayName("Get By Id Valid Test")
    public void getByID() throws ResponseException {
        var auth = authService.addAuth("testUser");
        var retrievedAuth = authService.getAuthByID(auth.authToken());

        assertEquals(auth, retrievedAuth);
    }


}
