package dataaccess;

import dataobjects.AuthData;
import exception.ResponseException;

public interface IAuthDAO {
    AuthData getAuthByToken(String authToken) throws ResponseException;
    AuthData addAuth(String username) throws ResponseException;
    void deleteAllAuths();
}
