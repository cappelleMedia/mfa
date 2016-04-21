package service;

import domain.DaoObject;
import domain.poll.PollItem;
import domain.poll.PollItemAnswer;
import java.util.Collection;
import java.util.Set;

public interface PollItemService {

    void addPollItem(DaoObject pollItem) throws ServiceException;

    void removePollItem(long id) throws ServiceException;

    boolean containsPollItem(long id) throws ServiceException;

    boolean containsPollItem(String question) throws ServiceException;

    PollItem getPollItem(long pollItemId) throws ServiceException;

    long getPollId(String question) throws ServiceException;
    
    void updatePollItem(long id, String question) throws ServiceException;
    
    void updatePollItem(long id, String question, Set<PollItemAnswer> answers) throws ServiceException;
    
    void updatePollItemAnswers(long id, Set<PollItemAnswer> answers) throws ServiceException;

    void addAnswerToPollItem(long id, PollItemAnswer ans) throws ServiceException;
    
    void removeAnswerFromPollItem(long pollId, long answerId) throws ServiceException;
    
    void addAVoteToAnswerOfPollItem(long pollItemId, long answerId) throws ServiceException;
    
    void removeVoteFromPollItemAnswer(long pollId, long answerId) throws ServiceException;
    
    Collection<PollItem> getPollItems() throws ServiceException;
}
