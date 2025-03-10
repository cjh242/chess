package dataaccess;

import dataobjects.AuthData;

public class AuthDAO implements IAuthDAO{
    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public AuthData getAuthByToken(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData addAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAllAuths() throws DataAccessException {

    }
}
