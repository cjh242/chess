package service;

import dataaccess.IAuthDAO;
import exception.ResponseException;

public class AuthService {
    private final IAuthDAO authDao;

    public AuthService(IAuthDAO authDao){
        this.authDao = authDao;
    }

    public boolean isAuthValid(String authToken) throws ResponseException {
        var auth = authDao.getAuthByToken(authToken);
        return auth != null;
    }
}
