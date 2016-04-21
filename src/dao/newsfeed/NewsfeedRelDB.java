package dao.newsfeed;

import db.DBException;
import domain.DaoObject;
import domain.newsfeed.NewsfeedItem;
import java.util.Collection;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class NewsfeedRelDB implements NewsfeedDAO {

    @PreDestroy
    public void destruct() {
        this.closeConnection();
        System.out.println("Newsfeed dao connections were closed");
    }

    private EntityManagerFactory managerFactory;
    private EntityManager manager;
    private final String idNotFoundEx = "No newsfeed with this id was found. Id was: ";
//    private String nameDB;

    public NewsfeedRelDB(String nameDB) {
        this.init(nameDB);
    }

    private void init(String nameDB) {
        managerFactory = Persistence.createEntityManagerFactory(nameDB);
        manager = managerFactory.createEntityManager();
//        this.nameDB = nameDB;// to reopen a connection
    }

    private boolean containsExceptionTrigger(long id) throws DBException {
        if (!this.contains(id)) {
            throw new DBException(idNotFoundEx + id);
        }
        return true;
    }

    @Override
    public void updateNewsfeedItem(long newsfeedItemId, String subject, String text) throws DBException {
        //Using newsfeeditem native update method for validation reasons
        this.containsExceptionTrigger(newsfeedItemId);
        try {
            NewsfeedItem newsfeed = manager.find(NewsfeedItem.class, newsfeedItemId);
            manager.getTransaction().begin();
            newsfeed.update(subject, text);
            manager.flush();
            manager.getTransaction().commit();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public Collection<NewsfeedItem> getNewsfeedItems() throws DBException {
        try {
            Query query = manager.createQuery("SELECT n FROM NewsfeedItem n");
            return query.getResultList();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public void add(DaoObject object) throws DBException {
        if (object == null) {
            throw new DBException("Sorry, it's not allowed to add a non existing (null) newsfeed item");
        }
        if (!(object instanceof NewsfeedItem)) {
            throw new DBException("You can only add Newsfeed item objects to this database");
        }
        try {
            NewsfeedItem newsfeed = (NewsfeedItem) object;
            if (this.contains(newsfeed.getId())) {
                throw new DBException("It seems you have already added a newsfeed with thid id. Id was: " + newsfeed.getId());
            };
            if (this.contains(newsfeed.getSubject())) {
                throw new DBException("It seems you have already added a newsfeed with this subject");
            }
            manager.getTransaction().begin();
            manager.persist(newsfeed);
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
            NewsfeedItem newsfeed = manager.find(NewsfeedItem.class, id);
            manager.getTransaction().begin();
            manager.remove(newsfeed);
            manager.flush();
            //FIXME flush here too?
            manager.getTransaction().commit();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean contains(long id) throws DBException {
        boolean contains = false;
        try {
            if (manager.find(NewsfeedItem.class, id) != null) {
                contains = true;
            }
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
        return contains;
    }

    @Override
    public boolean contains(String identifier) throws DBException {
        boolean contains = false;
        try {
            Query query = manager.createQuery(
                    "SELECT n FROM NewsfeedItem n WHERE n.subject = :sub").setParameter("sub", identifier);
            if (!query.getResultList().isEmpty()) {
                contains = true;
            }
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
        return contains;
    }

    @Override
    public DaoObject get(long id) throws DBException {
        this.containsExceptionTrigger(id);
        try {
            NewsfeedItem newsfeed;
            newsfeed = manager.find(NewsfeedItem.class, id);
            return newsfeed;
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public long getIDFromString(String identifier) throws DBException {
        if (!this.contains(identifier)) {
            throw new DBException("No newsfeed with this subject found");
        }
        try {
            Query query = manager.createQuery(
                    "SELECT n FROM NewsfeedItem n WHERE n.subject = :sub").setParameter("sub", identifier);
            NewsfeedItem newsfeed = (NewsfeedItem) query.getSingleResult();
            return newsfeed.getId();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public void reset() throws DBException {
        //FIXME find beter way
        try {
            Collection<NewsfeedItem> items = this.getNewsfeedItems();
            for (NewsfeedItem item : items) {
                this.remove(item.getId());
            }
        } catch (Exception ex) {
            throw new DBException("Something went wrong while resetting the newsfeed database");
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

}
