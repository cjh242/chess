package service;

import dataaccess.IAuthDAO;
import dataobjects.AuthData;
import exception.ResponseException;
import request.LogoutRequest;
import result.Result;

public class AuthService {
    private final IAuthDAO authDao;

    public AuthService(IAuthDAO authDao){
        this.authDao = authDao;
    }

    public void logout(LogoutRequest logout) throws ResponseException {
        authDao.deleteAuth(logout.authToken());
    }

    public boolean isAuthValid(String authToken) throws ResponseException {
        var auth = authDao.getAuthByToken(authToken);
        return auth != null;
    }

    public AuthData getAuthByID(String authToken) throws ResponseException {
        return authDao.getAuthByToken(authToken);
    }

    public AuthData addAuth(String username) throws ResponseException {
        return authDao.addAuth(username);
    }

    public Result deleteAllAuths() {
        try {
            authDao.deleteAllAuths();
            return new Result(200);
        } catch (Exception ex) {
            return new Result(500);
        }
    }
}
