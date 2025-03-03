package dataaccess;

import dataobjects.UserData;
import request.RegisterRequest;

public interface IUserDAO {
    UserData addUser(RegisterRequest request) throws DataAccessException;
    UserData getUserByUsername(String username) throws DataAccessException;
    void deleteAllUsers() throws DataAccessException;
}
