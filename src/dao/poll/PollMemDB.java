package dao.poll;

import dao.IdAssigner;
import db.DBException;
import domain.DaoObject;
import domain.DomainException;
import domain.poll.PollItem;
import domain.poll.PollItemAnswer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class PollMemDB implements PollDAO {

    private static Map<Long, PollItem> pollItems;
    private IdAssigner assigner;
    private String dbName;
    private final String idNotFoundEx = "No Poll item with this id was found";

    public PollMemDB(String dbName) throws DBException {
        this.setDBName(dbName);
        this.init();
    }

    private void init() {
        pollItems = new HashMap<>();
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

    @Override
    public Collection<PollItem> getPollItems() {
        return pollItems.values();
    }

    @Override
    public void updatePollItem(long pollItemId, String question) throws DBException {
        this.containsExceptionTrigger(pollItemId);
        try {
            this.get(pollItemId).update(question);
        } catch (DomainException domEx) {
            throw new DBException(domEx.getMessage(), domEx);
        }
    }

    @Override
    public void replacePollAnswers(long pollItemID, Set<PollItemAnswer> answers) throws DBException {
        this.containsExceptionTrigger(pollItemID);
        try {
            this.get(pollItemID).replaceAnswers(answers);
        } catch (DomainException domEx) {
            throw new DBException(domEx.getMessage(), domEx);
        }
    }

    @Override
    public void addAnswerToPollItem(long pollItemId, PollItemAnswer answer) throws DBException {
        this.containsExceptionTrigger(pollItemId);
        try {
            answer.setId(this.get(pollItemId).getAnswers().size());
            this.get(pollItemId).addAnswer(answer);
        } catch (DomainException domEx) {
            throw new DBException(domEx.getMessage(), domEx);
        }
    }

    @Override
    public void removeAnswerFromPollItem(long pollItemId, long answerId) throws DBException {
        this.containsExceptionTrigger(pollItemId);
        try {
            this.get(pollItemId).removeAnwser(answerId);
        } catch (DomainException domEx) {
            throw new DBException(domEx.getMessage(), domEx);
        }
    }

    @Override
    public void addAVoteToAnswerOfPollItem(long pollItemId, long answerId) throws DBException {
        this.containsExceptionTrigger(pollItemId);
        try {
            this.get(pollItemId).addVoteToAnswer(answerId);
        } catch (DomainException domEx) {
            throw new DBException(domEx.getMessage(), domEx);
        }
    }

    @Override
    public void removeAVoteFromAnswerOfPollItem(long pollItemId, long answerId) throws DBException {
        this.containsExceptionTrigger(pollItemId);
        try {
            this.get(pollItemId).removeVoteFromAnswer(answerId);
        } catch (DomainException domEx) {
            throw new DBException(domEx.getMessage(), domEx);
        }
    }

    @Override
    public void add(DaoObject object) throws DBException {
        if (object == null) {
            throw new DBException("You did not give a valid poll item object (it was null)");
        }
        if (!(object instanceof PollItem)) {
            throw new DBException("You can only add poll item objects to this database");
        }
        PollItem poll = (PollItem) object;
        if (this.contains(object.getId())) {
            throw new DBException("It seems there already is a poll with this id (" + object.getId() + ")");
        }
        if (this.contains(poll.getQuestion())) {
            throw new DBException("It seems there already is a poll with this question (" + poll.getQuestion() + ")");
        }
        if (poll.getId() == -1L) {
            this.assigner.assignID(poll, pollItems.keySet());
        }
        pollItems.put(poll.getId(), poll);
    }

    @Override
    public void remove(long id) throws DBException {
        this.containsExceptionTrigger(id);
        Iterator<Long> pollIter = pollItems.keySet().iterator();
        while (pollIter.hasNext()) {
            Long pollId = pollIter.next();
            if (pollId == id) {
                pollIter.remove();
                break;
            }
        }
    }

    @Override
    public boolean contains(long id) {
        return (pollItems.containsKey(id));
    }

    @Override
    public boolean contains(String question) {
        for (PollItem poll : pollItems.values()) {
            if (poll.getQuestion().equals(question)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsExceptionTrigger(long id) throws DBException {
        if (!this.contains(id)) {
            throw new DBException(idNotFoundEx + " (" + id + ")");
        }
        return true;
    }

    @Override
    public PollItem get(long id) throws DBException {
        containsExceptionTrigger(id);
        return pollItems.get(id);
    }

    @Override
    public long getIDFromString(String question) throws DBException {
        long id = -1L;
        if (question == null || question.trim().isEmpty()) {
            throw new DBException("The question of a poll item can't be empty when searching ");
        }
        for (PollItem poll : pollItems.values()) {
            if (poll.getQuestion().equalsIgnoreCase(question)) {
                id = poll.getId();
                break;
            }
        }
        if (id == -1L) {
            throw new DBException("Could not find this particular poll (question = " + question + ")");
        }
        return id;
    }

    @Override
    public void reset() {
        pollItems.clear();
    }

    @Override
    public void closeConnection() {
        //Not needed here
    }
    
    
}
