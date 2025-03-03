package dataaccess;

import dataobjects.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements IAuthDAO {
    final private HashMap<String, AuthData> auths = new HashMap<>();

    public AuthData addAuth(String username) {
        var auth = new AuthData(generateToken(), username);

        auths.put(auth.authToken(), auth);
        return auth;
    }

    public AuthData getAuthByToken(String authToken) {
        return auths.get(authToken);
    }

    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    public void deleteAllAuths() {
        auths.clear();
    }

    //Private Helpers
    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
