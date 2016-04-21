package dao.user;

import db.DBException;
import domain.DaoObject;
import domain.user.User;
import java.util.Collection;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class UserRelDB implements UserDAO {

    @PreDestroy
    public void destruct() {
        this.closeConnection();
        System.out.println("User dao connections were closed");
    }

    private EntityManagerFactory managerFactory;
    private EntityManager manager;
    private final String idNotFoundEx = "No user with this id was found. Id was: ";

    public UserRelDB(String nameDB) {
        this.init(nameDB);
    }

    private void init(String nameDB) {
        managerFactory = Persistence.createEntityManagerFactory(nameDB);
        manager = managerFactory.createEntityManager();
    }

    private boolean containsExceptionTrigger(long id) throws DBException {
        if (!this.contains(id)) {
            throw new DBException(idNotFoundEx + id);
        }
        return true;
    }

    @Override
    public void updateUser(long id, String prevPW, String username, String pw, boolean adminState) throws DBException {
        //Using user native update method for validation reasons
        this.containsExceptionTrigger(id);
        try {
            User user = manager.find(User.class, id);
            manager.getTransaction().begin();
            user.updateUser(prevPW, username, pw, adminState);
            manager.flush();
            manager.getTransaction().commit();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public Collection<User> getUsers() throws DBException {
        try {
            Query query = manager.createQuery("SELECT u FROM User u");
            return query.getResultList();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public void add(DaoObject object) throws DBException {
        if (object == null) {
            throw new DBException("Sorry, it's not allowed to add a non existing (null) user");
        }
        if (!(object instanceof User)) {
            throw new DBException("You can only add User objects to this database");
        }
        try {
            User user = (User) object;
            if (this.contains(user.getId())) {
                throw new DBException("It seems you have already added a user with this id. Id was: " + user.getId());
            }
            if (this.contains(user.getUsername())) {
                throw new DBException("It seems you have already added a user with this username. Username was: " + "'" + user.getUsername() + "'");
            }
            manager.getTransaction().begin();
            manager.persist(user);
            manager.flush();
            manager.getTransaction().commit();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public void remove(long id) throws DBException {
        this.containsExceptionTrigger(id);
        try {
            User user = manager.find(User.class, id);
            manager.getTransaction().begin();
            manager.remove(user);
            manager.flush();
            manager.getTransaction().commit();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean contains(long id) throws DBException {
        try {
            if (manager.find(User.class, id) != null) {
                return true;
            }
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public boolean contains(String identifier) throws DBException {
        try {
            Query query = manager.createQuery(
                    "SELECT u FROM User u WHERE u.username = :sub").setParameter("sub", identifier);
            if (!query.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public DaoObject get(long id) throws DBException {
        this.containsExceptionTrigger(id);
        try {
            User user = manager.find(User.class, id);
            return user;
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public long getIDFromString(String identifier) throws DBException {
        if (!this.contains(identifier)) {
            throw new DBException("No user with this subject found");
        }
        try {
            Query query = manager.createQuery(
                    "SELECT u FROM User u WHERE u.username = :sub").setParameter("sub", identifier);
            User user = (User) query.getSingleResult();
            return user.getId();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public void closeConnection() {
        if (managerFactory != null && managerFactory.isOpen()) {
            managerFactory.close();
        }
        if (manager != null && manager.isOpen()) {
            manager.close();
        }
    }

    @Override
    public void reset() throws DBException {
        try {
            Collection<User> users = this.getUsers();
            for(User user : users) {
                this.remove(user.getId());
            }
        } catch (Exception ex) {
            throw new DBException("Somthing went wrong while resetting the user database");
        }
    }

}
