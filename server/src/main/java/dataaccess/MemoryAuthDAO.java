package dataaccess;

import dataobjects.AuthData;
import exception.ResponseException;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements IAuthDAO {
    final private HashMap<String, AuthData> auths = new HashMap();

    public AuthData addAuth(AuthData auth) throws ResponseException {
        auth = new AuthData(generateToken(), auth.username());

        auths.put(auth.username(), auth);
        return auth;
    }

    public AuthData getAuthByUsername(String username) throws ResponseException {
        return auths.get(username);
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
