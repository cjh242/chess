package service;

import dataaccess.IAuthDAO;

public class AuthService {
    private final IAuthDAO authDao;

    public AuthService(IAuthDAO authDao){
        this.authDao = authDao;
    }

    public boolean isAuthValid(String authToken){
        var auth = authDao.getAuthByToken(authToken);
        return auth != null;
    }
}
