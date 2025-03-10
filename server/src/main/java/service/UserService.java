package service;

import dataaccess.IUserDAO;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.Result;

import java.util.Objects;

public class UserService {
    private final IUserDAO userDao;
    private final AuthService authService;

    public UserService(IUserDAO userDao, AuthService authService){
        this.userDao = userDao;
        this.authService = authService;
    }

    public LoginResult register(RegisterRequest registerRequest) {
        try {
            if(!registerRequest.isValid()) {
                return new LoginResult(null, null, 400);
            }
            if(userDao.getUserByUsername(registerRequest.username()) != null){
                return new LoginResult(null, null, 403);
            }
            var user = userDao.addUser(registerRequest);
            var login = new LoginRequest(user.username(), user.password());
            return login(login);
        } catch (Exception ex) {
            return new LoginResult(null, null, 500);
        }

    }
    public LoginResult login(LoginRequest loginRequest) {
        try {
            var user = userDao.getUserByUsername(loginRequest.username());
            if(user == null || !Objects.equals(user.password(), loginRequest.password())){
                //wrong password path or no user path
                return new LoginResult(null, null, 401);
            }
            var auth = authService.addAuth(loginRequest.username());
            return new LoginResult(auth.username(), auth.authToken(), 200);
        } catch(Exception ex) {
            return new LoginResult(null, null, 500);
        }

    }

    public Result deleteAllUsers() {
        try{
            userDao.deleteAllUsers();
            return new Result(200);
        } catch(Exception ex) {
            return new Result(500);
        }
    }
}
