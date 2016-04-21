package dao.newsfeed;

import dao.IdAssigner;
import db.DBException;
import domain.DaoObject;
import domain.DomainException;
import domain.newsfeed.NewsfeedItem;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class NewsfeedMemDB implements NewsfeedDAO {

    private static Map<Long, NewsfeedItem> newsfeedItems;
    private IdAssigner assigner;
    private String dbName;
    private final String idNotFoundEx = "No newsfeed with this id was found. Id was: ";

    public NewsfeedMemDB(String dbName) throws DBException {
        this.setDBName(dbName);
        this.init();
    }

    private void init() {
        newsfeedItems = new HashMap<>();
        this.assigner = new IdAssigner();
    }

    public String getDBName() {
        return this.dbName;
    }

    public void setDBName(String dbName) throws DBException {
        if (dbName == null || dbName.trim().isEmpty()) {
            throw new DBException("Please fill in a valid database name");
        }
        this.dbName = dbName;
    }

    private boolean containsExceptionTrigger(long id) throws DBException {
        if (!this.contains(id)) {
            throw new DBException(idNotFoundEx + id);
        }
        return true;
    }

    @Override
    public Collection<NewsfeedItem> getNewsfeedItems() {
        return newsfeedItems.values();
    }

    @Override
    public void updateNewsfeedItem(long newsfeedItemId, String subject, String text) throws DBException {
        this.containsExceptionTrigger(newsfeedItemId);
        try {
            this.get(newsfeedItemId).update(subject, text);
        } catch (DomainException domEx) {
            throw new DBException(domEx.getMessage(), domEx);
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
        NewsfeedItem newsfeedItem = (NewsfeedItem) object;
        if (this.contains(newsfeedItem.getId())) {
            throw new DBException("It seems you have already added a newsfeed with thid id. Id was: " + newsfeedItem.getId());
        }
        if (this.contains(newsfeedItem.getSubject())) {
            throw new DBException("It seems you have already added a newsfeed with this subject: '" + newsfeedItem.getSubject() + "'");
        }
        if (newsfeedItem.getId() == 0) {
            this.assigner.assignID(newsfeedItem, newsfeedItems.keySet());
        }
        newsfeedItems.put(newsfeedItem.getId(), newsfeedItem);

    }

    @Override
    public void remove(long id) throws DBException {
        this.containsExceptionTrigger(id);
        Iterator<Long> newsfeedIter = newsfeedItems.keySet().iterator();
        while (newsfeedIter.hasNext()) {
            Long newsfeedId = newsfeedIter.next();
            if (newsfeedId == id) {
                newsfeedIter.remove();
                break;
            }
        }
    }

    @Override
    public boolean contains(long id) {
        return newsfeedItems.containsKey(id);
    }

    @Override
    public boolean contains(String subject) { //TODO make this a part of interface?
        boolean contains = false;
        for (NewsfeedItem newsfeedItemAt : newsfeedItems.values()) {
            if (newsfeedItemAt.getSubject().equals(subject)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    @Override
    public NewsfeedItem get(long id) throws DBException {
        this.containsExceptionTrigger(id);
        return newsfeedItems.get(id);
    }

    @Override
    public long getIDFromString(String subject) throws DBException {
        if (subject == null || subject.trim().isEmpty()) {
            throw new DBException("The subject of the newsfeed item has to be filled in");
        }
        for (NewsfeedItem newsfeedItem : newsfeedItems.values()) {
            if (newsfeedItem.getSubject().equalsIgnoreCase(subject)) {
                return newsfeedItem.getId();
            }
        }
        throw new DBException("Could not find this particular newsfeed with subject = '" + subject + "'");

    }

    @Override
    public void reset() {
        newsfeedItems.clear();
    }

    @Override
    public void closeConnection() {
        //Not needed here
    }

}
