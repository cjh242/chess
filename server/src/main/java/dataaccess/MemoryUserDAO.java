package dataaccess;

import dataobjects.UserData;
import request.RegisterRequest;

import java.util.HashMap;

public class MemoryUserDAO implements IUserDAO {
    final private HashMap<String, UserData> users = new HashMap<>();

    public UserData addUser(RegisterRequest request) throws DataAccessException {
        var user = new UserData(request.username(), request.password(), request.email());

        users.put(user.username(), user);
        return user;
    }

    public UserData getUserByUsername(String username) throws DataAccessException {
        return users.get(username);
    }

    public void deleteAllUsers(){
        users.clear();
    }
}
