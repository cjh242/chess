package dataaccess;

import dataobjects.UserData;
import exception.ResponseException;
import request.RegisterRequest;

public interface IUserDAO {
    UserData addUser(RegisterRequest request) throws ResponseException;
    UserData getUserByUsername(String username) throws ResponseException;
}
