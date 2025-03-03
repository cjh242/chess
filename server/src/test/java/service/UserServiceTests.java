package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    IUserDAO userDao = new MemoryUserDAO();
    IAuthDAO authDao = new MemoryAuthDAO();
    AuthService authService = new AuthService(authDao);
    UserService userService = new UserService(userDao, authService);

    @BeforeEach
    void clear() throws DataAccessException {
        userDao.deleteAllUsers();
    }

    @Test
    @DisplayName("Register Valid Test")
    public void register(){

    }

    @Test
    @DisplayName("Login Valid Test")
    public void login() throws DataAccessException {
        var user = userDao.addUser(new RegisterRequest("testUser", "password", "test@email.com"));
        var login = userService.login(new LoginRequest(user.username(), "password"));
        var auth = authService.getAuthByID(login.authToken());

        assertTrue(authService.isAuthValid(auth));
    }

}
