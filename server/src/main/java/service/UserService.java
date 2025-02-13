package service;

import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;

public class UserService {
    public LoginResult register(RegisterRequest registerRequest) {
        return new LoginResult("", "");
    }
    public LoginResult login(LoginRequest loginRequest) {
        return new LoginResult("", "");
    }
    public void logout(LogoutRequest logoutRequest) {}
}
