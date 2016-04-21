package dao.user;

import dao.Database;
import db.DBException;
import domain.user.User;
import java.util.Collection;

public interface UserDAO extends Database {

    void updateUser(long id, String prevPW, String username, String pw, boolean adminState) throws DBException;

    Collection<User> getUsers() throws DBException;
}
