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
    @DisplayName("Register Valid")
    public void register() throws DataAccessException {
        var register = userService.register(new RegisterRequest("testUser", "password", "test@email.com"));
        var auth = authService.getAuthByID(register.authToken());

        assertEquals(200, register.code());
        assertTrue(authService.isAuthValid(auth));
    }

    @Test
    @DisplayName("Register Bad Request")
    public void badRegister(){
        var register = userService.register(new RegisterRequest(null, null, null));

        assertEquals(400, register.code());
    }

    @Test
    @DisplayName("Login Valid")
    public void login() throws DataAccessException {
        var user = userDao.addUser(new RegisterRequest("testUser", "password", "test@email.com"));
        var login = userService.login(new LoginRequest(user.username(), "password"));
        var auth = authService.getAuthByID(login.authToken());

        assertTrue(authService.isAuthValid(auth));
        assertEquals(200, login.code());
    }

    @Test
    @DisplayName("Login Wrong Password")
    public void wrongPasswordLogin() throws DataAccessException {
        var user = userDao.addUser(new RegisterRequest("testUser", "password", "test@email.com"));
        var login = userService.login(new LoginRequest(user.username(), "WRONG PASSWORD"));

        assertNull(login.authToken());
        assertEquals(401, login.code());
    }

    @Test
    @DisplayName("Delete All")
    public void delete() throws DataAccessException {
        var user1 = userDao.addUser(new RegisterRequest("testUser1", "password", "test1@email.com"));
        var user2 = userDao.addUser(new RegisterRequest("testUser2", "password", "test2@email.com"));
        var user3 = userDao.addUser(new RegisterRequest("testUser3", "password", "test3@email.com"));

        userService.deleteAllUsers();

        var login1 = userService.login(new LoginRequest(user1.username(), "password"));
        var login2 = userService.login(new LoginRequest(user2.username(), "password"));
        var login3 = userService.login(new LoginRequest(user3.username(), "password"));

        assertNull(login1.authToken());
        assertNull(login2.authToken());
        assertNull(login3.authToken());
        assertEquals(401, login1.code());
        assertEquals(401, login2.code());
        assertEquals(401, login3.code());

    }

}
