package dataaccess;

import dataobjects.AuthData;
import exception.ResponseException;

public interface IAuthDAO {
    void deleteAuth(String authToken) throws ResponseException;
    AuthData getAuthByToken(String authToken) throws ResponseException;
    AuthData addAuth(String username) throws ResponseException;
    void deleteAllAuths();
}
