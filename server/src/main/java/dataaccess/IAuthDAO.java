package dataaccess;

import dataobjects.AuthData;

public interface IAuthDAO {
    void deleteAuth(String authToken) throws DataAccessException;
    AuthData getAuthByToken(String authToken) throws DataAccessException;
    AuthData addAuth(String username) throws DataAccessException;
    void deleteAllAuths() throws DataAccessException;
}
