package service;

import dataaccess.IAuthDAO;
import dataaccess.IUserDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataobjects.UserData;
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

//    @BeforeEach
//    void clear() throws ResponseException {
//        userDao.deleteAllUsers();
//    }
//
//    @Test
//    @DisplayName("Register Valid Test")
//    public void register(){
//
//    }

//    @Test
//    @DisplayName("Login Valid Test")
//    public void login() throws ResponseException {
//        var user = userDao.addUser(new RegisterRequest("testUser", "password", "test@email.com"));
//        var login = userService.login(new LoginRequest(user.username(), "password"));
//
//        assertTrue(authService.isAuthValid(login.authToken()));
//    }

}
