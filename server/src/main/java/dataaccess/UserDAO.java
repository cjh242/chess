package dataaccess;

import dataobjects.UserData;
import request.RegisterRequest;

public class UserDAO implements IUserDAO{
    @Override
    public UserData addUser(RegisterRequest request) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUserByUsername(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAllUsers() throws DataAccessException {

    }
}
