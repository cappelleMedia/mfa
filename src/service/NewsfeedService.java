package service;

import domain.DaoObject;
import domain.newsfeed.NewsfeedItem;
import java.util.Collection;

public interface NewsfeedService {

    void addNewsfeedItem(DaoObject newsfeedItem) throws ServiceException;

    void removeNewsfeedItem(long id) throws ServiceException;

    boolean containsNewsfeedItem(long id) throws ServiceException;

    boolean containsNewsfeedItem(String subject) throws ServiceException;

    NewsfeedItem getNewsfeedItem(long newsfeedItemId) throws ServiceException;

    long getNewsfeedId(String subject) throws ServiceException;

    void updateNewsfeedItem(long id, String subject, String text) throws ServiceException;

    Collection<NewsfeedItem> getNewsfeedItems() throws ServiceException;
}
