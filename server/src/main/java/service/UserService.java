package service;

import dataaccess.IUserDAO;
import exception.ResponseException;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;

public class UserService {
    private final IUserDAO userDao;

    public UserService(IUserDAO userDao){
        this.userDao = userDao;
    }

    public LoginResult register(RegisterRequest registerRequest) throws ResponseException {
        if(userDao.getUserByUsername(registerRequest.username()) != null){
            return null;
        }
        var user = userDao.addUser(registerRequest);
        var login = new LoginRequest(user.username(), user.password());
        return login(login);
    }
    public LoginResult login(LoginRequest loginRequest) {
        return new LoginResult("", "");
    }
    public void logout(LogoutRequest logoutRequest) {}
}
