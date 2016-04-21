package dao.poll;

import dao.Database;
import db.DBException;
import domain.poll.PollItem;
import domain.poll.PollItemAnswer;
import java.util.Collection;
import java.util.Set;

public interface PollDAO extends Database{
    public void updatePollItem(long pollItemId, String question) throws DBException;
    public void addAnswerToPollItem(long pollItemId, PollItemAnswer answer) throws DBException;
    public void removeAnswerFromPollItem(long pollItemId, long answerId) throws DBException;
    public void replacePollAnswers(long pollItemID, Set<PollItemAnswer> answers) throws DBException;
    public void addAVoteToAnswerOfPollItem(long pollItemId, long answerId) throws DBException;
    public void removeAVoteFromAnswerOfPollItem(long pollItemId, long answerId) throws DBException;
    public Collection<PollItem> getPollItems() throws DBException;
}
