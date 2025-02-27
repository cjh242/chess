package service;

import dataaccess.IUserDAO;
import exception.ResponseException;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;

public class UserService {
    private final IUserDAO userDao;
    private final AuthService authService;

    public UserService(IUserDAO userDao, AuthService authService){
        this.userDao = userDao;
        this.authService = authService;
    }

    public LoginResult register(RegisterRequest registerRequest) throws ResponseException {
        if(userDao.getUserByUsername(registerRequest.username()) != null){
            return null;
        }
        var user = userDao.addUser(registerRequest);
        var login = new LoginRequest(user.username(), user.password());
        return login(login);
    }
    public LoginResult login(LoginRequest loginRequest) throws ResponseException {
        var user = userDao.getUserByUsername(loginRequest.username());
        if(user.password() != loginRequest.password()){
            return null;
        }
        var auth = authService.addAuth(loginRequest.username());
        return new LoginResult(auth.username(), auth.authToken());
    }

    public void deleteAllUsers() throws ResponseException {
        userDao.deleteAllUsers();
    }
}
