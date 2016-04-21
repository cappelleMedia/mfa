package dao.user;

import dao.IdAssigner;
import db.DBException;
import domain.DaoObject;
import domain.DomainException;
import domain.user.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserMemDB implements UserDAO {

    private static Map<Long, User> users;
    private IdAssigner assigner;
    private final String idNotFoundEx = "No user with this id was found. Id was: ";

    public UserMemDB() {
        this.init();
    }

    private void init() {
        users = new HashMap<>();
        this.assigner = new IdAssigner();
    }

    private boolean containsExceptionTrigger(long id) throws DBException {
        if (!this.contains(id)) {
            throw new DBException(idNotFoundEx + id);
        }        
        return true;
    }

    @Override
    public void updateUser(long id, String prevPW, String username, String pw, boolean adminState) throws DBException {
        this.containsExceptionTrigger(id);
        try{
            this.get(id).updateUser(prevPW, username, pw, adminState);
        } catch (DomainException domEx) {
            throw new DBException(domEx.getMessage(), domEx);
        }
    }

    @Override
    public Collection<User> getUsers() throws DBException {
        return  users.values();
    }

    @Override
    public void add(DaoObject object) throws DBException {
        if(object == null) {
            throw new DBException("Sorry, it's not allowed to add a non existing (null) user");
        }
        if(!(object instanceof User)) {
            throw new DBException("You can only add User objects to this database");
        }
        User user = (User) object;
        if(this.contains(user.getId())){
            throw new DBException("It seems you have already added a user with this id. Id was: " + user.getId());
        }
        if(this.contains(user.getUsername())) {
            throw new DBException("It seems you have already added a user with this username. Username was: " + "'" + user.getUsername() +"'");
        }
        if(user.getId() == 0) {
            this.assigner.assignID(user, users.keySet());
        }
        users.put(user.getId(), user);
    }

    @Override
    public void remove(long id) throws DBException {
        this.containsExceptionTrigger(id);
        Iterator<Long> userIter = users.keySet().iterator();
        while(userIter.hasNext()) {
            long userId = userIter.next();
            if(userId == id) {
                userIter.remove();
                break;
            }
        }
    }

    @Override
    public boolean contains(long id) throws DBException {
        return users.containsKey(id);
    }

    @Override
    public boolean contains(String username) throws DBException {
        return users.values().stream().anyMatch((userAt) -> (userAt.getUsername().equals(username)));
    }

    @Override
    public User get(long id) throws DBException {
        this.containsExceptionTrigger(id);
        return users.get(id);
    }

    @Override
    public long getIDFromString(String username) throws DBException {
        if(username == null || username.trim().isEmpty()) {
            throw new DBException("The username of the user has to be filled in to search for their id");
        }
        for(User user : users.values()) {
            if(user.getUsername().equals(username)) {
                return user.getId();
            }          
        }
        throw new DBException("Could not find a user with username: " + username);
    }

    @Override
    public void closeConnection() {
        // not needed here
    }

    @Override
    public void reset() throws DBException {
        users.clear();
    }

}
