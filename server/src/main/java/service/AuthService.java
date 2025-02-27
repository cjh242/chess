package service;

import dataaccess.IAuthDAO;
import dataobjects.AuthData;
import exception.ResponseException;
import request.LogoutRequest;

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

    public void deleteAllAuths() {
        authDao.deleteAllAuths();
    }
}
