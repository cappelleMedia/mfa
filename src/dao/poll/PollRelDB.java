package dao.poll;

import db.DBException;
import domain.DaoObject;
import domain.poll.PollItem;
import domain.poll.PollItemAnswer;
import java.util.Collection;
import java.util.Set;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class PollRelDB implements PollDAO {

    //FIXME to much use of find, transaction, flush -> create methods for this
    
    @PreDestroy
    public void destruct() {
        this.closeConnection();
    }

    private EntityManagerFactory managerFactory;
    private EntityManager manager;
    private String idNotFound = "No poll entry with this id was found. Id was: ";
//    private String nameDB;

    public PollRelDB(String nameDB) {
        this.init(nameDB);
    }

    private void init(String nameDB) {
        managerFactory = Persistence.createEntityManagerFactory(nameDB);
        manager = managerFactory.createEntityManager();
//        this.nameDB = nameDB;// to reopen a connection
    }

    private boolean containsExceptionTrigger(long id) throws DBException {
        if (!this.contains(id)) {
            throw new DBException(idNotFound + id);
        }
        return true;
    }

    @Override
    public void updatePollItem(long pollItemId, String question) throws DBException {
        //Using PollItem native update method for validation reasons
        this.containsExceptionTrigger(pollItemId);
        try {
            PollItem poll = manager.find(PollItem.class, pollItemId);
            this.manager.getTransaction().begin();
            poll.update(question);
            manager.flush();
            manager.getTransaction().commit();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public void addAnswerToPollItem(long pollItemId, PollItemAnswer answer) throws DBException {
        this.containsExceptionTrigger(pollItemId);
        try {
            PollItem poll = manager.find(PollItem.class, pollItemId);
            this.manager.getTransaction().begin();
            poll.addAnswer(answer);
            manager.flush();
            manager.getTransaction().commit();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public void removeAnswerFromPollItem(long pollItemId, long answerId) throws DBException {
        this.containsExceptionTrigger(pollItemId);
        try {
            PollItem poll = manager.find(PollItem.class, pollItemId);
            this.manager.getTransaction().begin();
            poll.removeAnwser(answerId);
            manager.flush();
            manager.getTransaction().commit();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public void replacePollAnswers(long pollItemId, Set<PollItemAnswer> answers) throws DBException {
        this.containsExceptionTrigger(pollItemId);
        try{
            PollItem poll = manager.find(PollItem.class, pollItemId);
            this.manager.getTransaction().begin();
            poll.replaceAnswers(answers);
            manager.flush();
            manager.getTransaction().commit();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public void addAVoteToAnswerOfPollItem(long pollItemId, long answerId) throws DBException {
        this.containsExceptionTrigger(pollItemId);
        try {
            PollItem poll = manager.find(PollItem.class, pollItemId);
            this.manager.getTransaction().begin();
            poll.addVoteToAnswer(answerId);
            manager.flush();
            manager.getTransaction().commit();
       } catch(Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public void removeAVoteFromAnswerOfPollItem(long pollItemId, long answerId) throws DBException {
        this.containsExceptionTrigger(pollItemId);
        try {
            PollItem poll = manager.find(PollItem.class, pollItemId);
            this.manager.getTransaction().begin();
            poll.removeVoteFromAnswer(answerId);
            manager.flush();
            manager.getTransaction().commit();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public Collection<PollItem> getPollItems() throws DBException {
        try {
            Query query = manager.createQuery("SELECT p FROM PollItem p");
            return query.getResultList();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public void add(DaoObject object) throws DBException {
        try {
            PollItem poll = (PollItem) object;
            if (this.contains(poll.getId())) {
                throw new DBException("It seems the poll item database already has an entry with this id. Id was: " + poll.getId());
            }
            if (this.contains(poll.getQuestion())) {
                throw new DBException("It seems you have already added a poll with this question");
            }
            manager.getTransaction().begin();
            manager.persist(poll);
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
            PollItem poll = manager.find(PollItem.class, id);
            manager.getTransaction().begin();
            manager.remove(poll);
            manager.flush();
            manager.getTransaction().commit();
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean contains(long id) throws DBException {
        boolean contains = false;
        try {
            contains = manager.find(PollItem.class, id) != null;
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
                    "SELECT p FROM PollItem p WHERE p.question = :sub").setParameter("sub", identifier);
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
            return manager.find(PollItem.class, id);
        } catch (Exception ex) {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public long getIDFromString(String identifier) throws DBException {
        if (!this.contains(identifier)) {
            throw new DBException("No poll with this question found");
        }
        try {
            Query query = manager.createQuery(
                    "SELECT p FROM PollItem p WHERE p.question = :sub").setParameter("sub", identifier);
            PollItem poll = (PollItem) query.getSingleResult();
            return poll.getId();
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
        //implement
    }

}
