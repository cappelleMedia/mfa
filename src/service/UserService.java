package service;

import domain.DaoObject;
import domain.user.User;
import java.util.Collection;

public interface UserService {

    void addUser(DaoObject user) throws ServiceException;

    void removeUser(long id) throws ServiceException;

    boolean containsUser(long id) throws ServiceException;

    boolean containsUser(String username) throws ServiceException;

    User getUser(long id) throws ServiceException;

    long getUserId(String username) throws ServiceException;

    void updateUser(long id, String prevPW, String username, String pw, boolean adminState) throws ServiceException;
    
    boolean checkAdminPW(String adPw);

    Collection<User> getUsers() throws ServiceException;
}
