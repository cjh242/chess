package dataaccess;

import dataobjects.AuthData;
import exception.ResponseException;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements IAuthDAO {
    final private HashMap<String, AuthData> auths = new HashMap();

    public AuthData addAuth(AuthData auth) throws ResponseException {
        auth = new AuthData(generateToken(), auth.username());

        auths.put(auth.authToken(), auth);
        return auth;
    }

    public AuthData getAuthByToken(String authToken) throws ResponseException {
        return auths.get(authToken);
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
