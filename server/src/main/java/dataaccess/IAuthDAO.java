package dataaccess;

import dataobjects.AuthData;

public interface IAuthDAO {
    AuthData getAuthByToken(String authToken);
}
