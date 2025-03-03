package service;

import dataaccess.DataAccessException;
import dataaccess.IAuthDAO;
import dataobjects.AuthData;
import request.LogoutRequest;
import result.Result;

import java.util.Objects;

public class AuthService {
    private final IAuthDAO authDao;

    public AuthService(IAuthDAO authDao){
        this.authDao = authDao;
    }

    public Result logout(LogoutRequest logout) {
        try {
            var auth = getAuthByID(logout.authToken());
            if (isAuthValid(auth)) {
                authDao.deleteAuth(logout.authToken());
                return new Result(200);
            }
            return new Result(401);
        } catch (Exception ex) {
            return new Result(500);
        }
    }

    public boolean isAuthValid(AuthData auth) {
        try {
            var retrievedAuth = authDao.getAuthByToken(auth.authToken());
            if (retrievedAuth != null && Objects.equals(auth.username(), retrievedAuth.username())) {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    public AuthData getAuthByID(String authToken) throws DataAccessException {
        return authDao.getAuthByToken(authToken);
    }

    public AuthData addAuth(String username) throws DataAccessException {
        if(username == null || username == "") {
            return null;
        }
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
