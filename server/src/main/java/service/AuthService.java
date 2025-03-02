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

    public Result logout(LogoutRequest logout) {
        try {
            if (isAuthValid(logout.authToken())) {
                authDao.deleteAuth(logout.authToken());
                return new Result(200);
            }
            return new Result(401);
        } catch (Exception ex) {
            return new Result(500);
        }
    }

    public boolean isAuthValid(String authToken) {
        try {
            var auth = authDao.getAuthByToken(authToken);
            return auth != null;
        } catch (Exception ex) {
            return false;
        }
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
