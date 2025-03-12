package passoff.dataaccess;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTests {
    private final UserDAO userDao = new UserDAO();

    @BeforeEach
    void clear() throws DataAccessException {
        userDao.deleteAllUsers();
    }

    @Test
    @DisplayName("Add User")
    public void addUser() throws DataAccessException {
        var user = userDao.addUser(new RegisterRequest("TEST_USER", "PASSWORD", "TEST@EMAIL.COM"));

        var retrievedUser = userDao.getUserByUsername("TEST_USER");

        assertEquals(user, retrievedUser);

    }

    @Test
    @DisplayName("Add User Null")
    public void addUserBad() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> {
            userDao.addUser(new RegisterRequest(null, null, null));
        });
    }

    @Test
    @DisplayName("Get User")
    public void getByUsername() throws DataAccessException {
        var user = userDao.addUser(new RegisterRequest("TEST_USER", "PASSWORD", "TEST@EMAIL.COM"));

        var retrievedUser = userDao.getUserByUsername("TEST_USER");

        assertEquals(user, retrievedUser);

    }




}
