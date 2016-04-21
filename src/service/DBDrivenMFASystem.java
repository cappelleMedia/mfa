package service;

import dao.DAOFactory;
import dao.newsfeed.NewsfeedDAO;
import dao.poll.PollDAO;
import dao.user.UserDAO;
import db.DBException;
import domain.DaoObject;
import domain.DomainException;
import domain.newsfeed.NewsfeedItem;
import domain.poll.PollItem;
import domain.poll.PollItemAnswer;
import domain.user.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class DBDrivenMFASystem implements NewsfeedService, PollItemService, MenuService, UserService {

    private PollDAO pollDB;
    private NewsfeedDAO newsfeedDB;
    private UserDAO userDB;
    private Map<String, MenuItem> menuItems;
    private Map<String, Boolean> authenticationItems;
    private final String adPw = "Root"; //FIXME cleaner & safer solution needed

    public DBDrivenMFASystem(Properties props) throws ServiceException {
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
            userDB = DAOFactory.getInstance().getUserDB(props);
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

            User user = new User("admin", "admin", true);
            this.addUser(user);

            user = new User("marlon", "admin", true);
            this.addUser(user);

            user = new User("jens", "admin", true);
            this.addUser(user);

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

    @Override
    public List<MenuItem> getMenu() {
        List<MenuItem> items = new ArrayList<>();
        for (MenuItem item : menuItems.values()) {
            items.add(item);
        }
        return items;
    }

    @Override
    public boolean isAuthenticationNeeded(String action) {
        if (action == null || action.trim().isEmpty()) {
            return false;
        }
        return this.authenticationItems.getOrDefault(action, false);
    }

    public void closeConnections() {
        this.newsfeedDB.closeConnection();
        this.pollDB.closeConnection();
        this.userDB.closeConnection();
    }

    /*------------------------------------------------------------
     NEWSFEED DAO OPS
     ------------------------------------------------------------*/
    @Override
    public void addNewsfeedItem(DaoObject newsfeedItem) throws ServiceException {
        try {
            this.newsfeedDB.add(newsfeedItem);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public void removeNewsfeedItem(long newsfeedItemId) throws ServiceException {
        try {
            this.newsfeedDB.remove(newsfeedItemId);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public boolean containsNewsfeedItem(long newsfeedItemId) throws ServiceException {
        try {
            return newsfeedDB.contains(newsfeedItemId);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public boolean containsNewsfeedItem(String subject) throws ServiceException {
        try {
            return newsfeedDB.contains(subject);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
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

    @Override
    public long getNewsfeedId(String subject) throws ServiceException {
        try {
            return newsfeedDB.getIDFromString(subject);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public void updateNewsfeedItem(long newsfeedItemId, String subject, String text) throws ServiceException {
        try {
            newsfeedDB.updateNewsfeedItem(newsfeedItemId, subject, text);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public Collection<NewsfeedItem> getNewsfeedItems() throws ServiceException {
        try {
            return newsfeedDB.getNewsfeedItems();
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    /*------------------------------------------------------------
     POLL DAO OPS
     ------------------------------------------------------------*/
    @Override
    public void addPollItem(DaoObject pollItem) throws ServiceException {
        try {
            this.pollDB.add(pollItem);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public void removePollItem(long pollItemId) throws ServiceException {
        try {
            this.pollDB.remove(pollItemId);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public boolean containsPollItem(long pollItemId) throws ServiceException {
        try {
            return pollDB.contains(pollItemId);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public boolean containsPollItem(String question) throws ServiceException {
        try {
            return pollDB.contains(question);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public PollItem getPollItem(long pollItemId) throws ServiceException {
        try {
            return (PollItem) pollDB.get(pollItemId);
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

    @Override
    public long getPollId(String question) throws ServiceException {
        try {
            return pollDB.getIDFromString(question);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public void updatePollItem(long pollItemId, String question) throws ServiceException {
        try {
            pollDB.updatePollItem(pollItemId, question);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public void updatePollItem(long pollItemId, String question, Set<PollItemAnswer> answers) throws ServiceException {
        this.updatePollItem(pollItemId, question);
        this.updatePollItemAnswers(pollItemId, answers);
    }

    @Override
    public void updatePollItemAnswers(long pollItemId, Set<PollItemAnswer> answers) throws ServiceException {
        try {
            pollDB.replacePollAnswers(pollItemId, answers);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public void addAnswerToPollItem(long pollItemId, PollItemAnswer answer) throws ServiceException {
        try {
            pollDB.addAnswerToPollItem(pollItemId, answer);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public void removeAnswerFromPollItem(long pollItemId, long answerId) throws ServiceException {
        try {
            pollDB.removeAnswerFromPollItem(pollItemId, answerId);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public void addAVoteToAnswerOfPollItem(long pollItemId, long answerId) throws ServiceException {
        try {
            pollDB.addAVoteToAnswerOfPollItem(pollItemId, answerId);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public void removeVoteFromPollItemAnswer(long pollItemId, long ansId) throws ServiceException {
        try {
            pollDB.removeAVoteFromAnswerOfPollItem(pollItemId, ansId);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public Collection<PollItem> getPollItems() throws ServiceException {
        try {
            return pollDB.getPollItems();
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    /*------------------------------------------------------------
     USER DAO OPS
     ------------------------------------------------------------*/
    public User getAuthenticatedUser(String username, String password) throws ServiceException {
        if (username == null || username.trim().isEmpty()) {
            throw new ServiceException("No username to check authentication");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ServiceException("No password to check authentication");
        }
        User user = this.getUser(this.getUserId(username));
        if (user.getPw().equals(password)) {
            return user;
        }
        return null;
    }

    /*------------------------------------------------------------
     USER DAO OPS
     ------------------------------------------------------------*/
    @Override
    public void addUser(DaoObject user) throws ServiceException {
        try {
            this.userDB.add(user);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public void removeUser(long id) throws ServiceException {
        try {
            this.userDB.remove(id);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public boolean containsUser(long id) throws ServiceException {
        try {
            return this.userDB.contains(id);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public boolean containsUser(String username) throws ServiceException {
        try {
            return this.userDB.contains(username);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public User getUser(long id) throws ServiceException {
        try {
            return (User) this.userDB.get(id);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public long getUserId(String username) throws ServiceException {
        try {
            return this.userDB.getIDFromString(username);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    @Override
    public void updateUser(long id, String prevPW, String username, String pw, boolean adminState) throws ServiceException {
        try {
            this.userDB.updateUser(id, prevPW, username, pw, adminState);
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

    //FIXME find cleaner & safe solution
    @Override
    public boolean checkAdminPW(String adPw) {
        if (adPw.equals(this.adPw)) {
            return true;
        }
        return false;
    }

    @Override
    public Collection<User> getUsers() throws ServiceException {
        try {
            return this.userDB.getUsers();
        } catch (DBException dbEx) {
            throw new ServiceException(dbEx.getMessage(), dbEx);
        }
    }

}
