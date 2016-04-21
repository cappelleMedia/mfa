package service;

import dao.DAOFactory;
import dao.newsfeed.NewsfeedDAO;
import dao.poll.PollDAO;
import db.DBException;
import domain.DaoObject;
import domain.DomainException;
import domain.newsfeed.NewsfeedItem;
import domain.poll.PollItem;
import domain.poll.PollItemAnswer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MFASystem {

    private PollDAO pollDB;
    private NewsfeedDAO newsfeedDB;
    private Map<String, MenuItem> menuItems;
    private Map<String, Boolean> authenticationItems;

    public MFASystem(Properties props) throws ServiceException {
        init(props);
    }

    private void init(Properties props) throws ServiceException {
        this.menuItems = new LinkedHashMap<>();
        this.authenticationItems = new LinkedHashMap<>();
        this.createMenu();
        this.createAuthenticationItems();
        try {
            pollDB = DAOFactory.getInstance().getPollDB(props);
            newsfeedDB = DAOFactory.getInstance().getNewsfeedDB(props);
            this.fill();
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    private void fill() throws ServiceException {
        try {
            PollItem poll = new PollItem("Do you like this site?");
            poll.setId(1L);
            this.addPollItem(poll);
            PollItemAnswer ans1 = new PollItemAnswer("Yes");
            PollItemAnswer ans2 = new PollItemAnswer("No");

            this.addAnswerToPollItem(1L, ans1);
            this.addAnswerToPollItem(1L, ans2);

            poll = new PollItem("Are you happy?");
            poll.setId(2L);
            this.addPollItem(poll);
            ans1 = new PollItemAnswer("Hell yeah");
            ans2 = new PollItemAnswer("meh, not realy");
            this.addAnswerToPollItem(2L, ans1);
            this.addAnswerToPollItem(2L, ans2);

            NewsfeedItem newsfeedItem = new NewsfeedItem("Creation", "The mfa website has finaly been created");
            
            this.addNewsfeedItem(newsfeedItem);
            newsfeedItem = new NewsfeedItem("Awesome", "The website is up and running");
            
            this.addNewsfeedItem(newsfeedItem);

        } catch (DomainException domEx) {
            throw new ServiceException(domEx.getMessage(), domEx);
        }
    }

    private void createAuthenticationItems() {
        this.authenticationItems.put("addPoll", true);
        this.authenticationItems.put("addPollPage", true);
        this.authenticationItems.put("addNewsfeed", true);
        this.authenticationItems.put("addNewsfeedPage", true);
        this.authenticationItems.put("deletePoll", true);
        this.authenticationItems.put("deleteNewsfeed", true);
        this.authenticationItems.put("updatePoll", true);
        this.authenticationItems.put("updatePollPage", true);
        this.authenticationItems.put("updateNewsfeed", true);
        this.authenticationItems.put("updateNewsfeedPage", true);
    }

    private void createMenu() {
        this.menuItems.put("index", new MenuItem("index", "Home"));
        this.menuItems.put("about", new MenuItem("about", "About"));
        this.menuItems.put("info", new MenuItem("info", "Info"));
        this.menuItems.put("products", new MenuItem("products", "Products"));
        this.menuItems.put("contact", new MenuItem("contact", "Contact"));
        this.menuItems.put("extras", new MenuItem("extras", "Extras"));
        this.menuItems.put("account", new MenuItem("account", "Account"));

    }

    public List<MenuItem> getMenu() {
        List<MenuItem> items = new ArrayList<>();
        for (MenuItem item : menuItems.values()) {
            items.add(item);
        }
        return items;
    }

    public boolean isAuthenticationNeeded(String action) {
        boolean needsAuthentication = this.authenticationItems.getOrDefault(action, false);
        boolean authenticationNeeded;
        authenticationNeeded = needsAuthentication;
        return authenticationNeeded;
    }

    public String getAuthenticatedUser(String username, String password) throws ServiceException {
        String user = null;
        if (username == null || username.trim().isEmpty()) {
            throw new ServiceException("No username to check authentication");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ServiceException("No password to check authentication");
        }
        if (username.equals("admin") && password.equals("admin")) {
            user = username;
        }
        return user;
    }
    
    public void closeConnections(){
        this.newsfeedDB.closeConnection();
        this.pollDB.closeConnection();
    }

    /*---------NewsfeedDAO Operations---------*/
    /*---NewsfeedDAO basics---*/
    public void addNewsfeedItem(DaoObject newsfeedItem) throws ServiceException {
        try {
            this.newsfeedDB.add(newsfeedItem);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public void removeNewsfeedItem(long newsfeedItemId) throws ServiceException {
        try {
            this.newsfeedDB.remove(newsfeedItemId);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public boolean containsNewsfeedItem(long newsfeedItemId) throws ServiceException {
        try {
            return newsfeedDB.contains(newsfeedItemId);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public boolean containsNewsfeedItem(String subject) throws ServiceException {
        try {
            return newsfeedDB.contains(subject);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public NewsfeedItem getNewsfeedItem(long newsfeedItemId) throws ServiceException {
        try {
            NewsfeedItem newsfeed = (NewsfeedItem) newsfeedDB.get(newsfeedItemId);
            return newsfeed;
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public void resetNewsfeedDB() throws ServiceException {
        try {
            newsfeedDB.reset();
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    /*---NewsfeedDAO specifics---*/
    public long getNewsfeedId(String subject) throws ServiceException {
        try {
            long newsfeedId = newsfeedDB.getIDFromString(subject);
            return newsfeedId;
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public void updateNewsfeedItem(long newsfeedItemId, String subject, String text) throws ServiceException {
        try {
            newsfeedDB.updateNewsfeedItem(newsfeedItemId, subject, text);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public Collection<NewsfeedItem> getNewsfeedItems() throws ServiceException {
        try {
            return newsfeedDB.getNewsfeedItems();
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }

    }

    /*---------PollDAO Operations---------*/
    /*---PollDAO basics---*/
    public void addPollItem(DaoObject pollItem) throws ServiceException {
        try {
            this.pollDB.add(pollItem);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public void removePollItem(long pollItemId) throws ServiceException {
        try {
            this.pollDB.remove(pollItemId);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public boolean containsPollItem(long pollItemId) throws ServiceException {
        try {
            return pollDB.contains(pollItemId);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public boolean containsPollItem(String question) throws ServiceException {
        try {
            return pollDB.contains(question);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public PollItem getPollItem(long pollItemId) throws ServiceException {
        try {
            PollItem poll = (PollItem) pollDB.get(pollItemId);
            return poll;
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public void resetPollDB() throws ServiceException {
        try {
            pollDB.reset();
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    /*---NewsfeedDAO specifics---*/
    public long getPollId(String question) throws ServiceException {
        try {
            long pollId = pollDB.getIDFromString(question);
            return pollId;
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public void updatePollItem(long pollItemId, String question) throws ServiceException {
        try {
            pollDB.updatePollItem(pollItemId, question);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }
    
    public void updatePollItem(long pollItemId, String question, Set<PollItemAnswer> answers) throws ServiceException {
        this.updatePollItem(pollItemId, question);
        this.updatePollItemAnswers(pollItemId, answers);
    }

    public void updatePollItemAnswers(long pollItemId, Set<PollItemAnswer> answers) throws ServiceException {
        try {
            pollDB.replacePollAnswers(pollItemId, answers);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public void addAnswerToPollItem(long pollItemId, PollItemAnswer answer) throws ServiceException {
        try {
            pollDB.addAnswerToPollItem(pollItemId, answer);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public void removeAnswerFromPollItem(long pollItemId, long answerId) throws ServiceException {
        try {
            pollDB.removeAnswerFromPollItem(pollItemId, answerId);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public void addAVoteToAnswerOfPollItem(long pollItemId, long answerId) throws ServiceException {
        try {
            pollDB.addAVoteToAnswerOfPollItem(pollItemId, answerId);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public void removeVoteFromPollItemAnswer(long pollItemId, int answerFollowUp) throws ServiceException {
        try {
            pollDB.removeAVoteFromAnswerOfPollItem(pollItemId, answerFollowUp);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    public Collection<PollItem> getPollItems() throws ServiceException{
        try{
            return pollDB.getPollItems();
        } catch(DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }

    }
}
