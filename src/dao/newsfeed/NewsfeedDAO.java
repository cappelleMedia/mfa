package dao.newsfeed;

import dao.Database;
import db.DBException;
import domain.newsfeed.NewsfeedItem;
import java.util.Collection;

public interface NewsfeedDAO extends Database{
    public void updateNewsfeedItem(long newsfeedItemId, String subject, String text) throws DBException;
    public Collection<NewsfeedItem> getNewsfeedItems() throws DBException;
}
