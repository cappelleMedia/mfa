package domain.poll;

import domain.DaoObject;
import domain.DomainException;
import domain.UpdateState;
import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

@Entity
public class PollItem extends DaoObject implements Serializable {

    private String question; //TODO add multi langs
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar displayDate;
    @Enumerated
    private UpdateState displayState;
    @OneToMany(cascade = CascadeType.PERSIST)
    private Set<PollItemAnswer> answers;

    public PollItem() {
        this.init();
    }

    public PollItem(String question) throws DomainException {
        this();
        this.setQuestion(question);
    }
    
    public PollItem(String question, String... answersTxt) throws DomainException {
        this(question);
        this.setAnswersTxt(answersTxt);
    }

    private void init() {
        this.displayDate = Calendar.getInstance();
        this.displayState = UpdateState.ADDED;
        this.answers = new LinkedHashSet<>();
    }

    /*--- STANDARD GETTERS AND SETTERS ---*/

    public String getQuestion() {
        return this.question;
    }

    public Calendar getDate() {
        return this.displayDate;
    }

    public UpdateState getState() {
        return this.displayState;
    }

    public Set<PollItemAnswer> getAnswers() {
        return this.answers;
    }

    public void setQuestion(String question) throws DomainException {
        if (question == null || question.trim().isEmpty()) {
            throw new DomainException("A poll item really needs a question to excist");
        }
        if (!question.endsWith("?")) {
            question += "?";
        }
        this.question = question;
    }

    public void setDate(Calendar date) throws DomainException {
        if (date == null) {
            throw new DomainException("Date should not be empty");
        }
        this.displayDate = date;
    }

    public void setState(UpdateState state) throws DomainException {
        if (state == null) {
            throw new DomainException("State should not be emtpy");
        }
        this.displayState = state;
    }

    public void replaceAnswers(Set<PollItemAnswer> answers) throws DomainException {
        if (answers != null) {//FIXME necessary?
            this.answers.clear();
        }
        this.setAnswers(answers);
    }

    public void setAnswersTxt(String... answersTxt)throws DomainException {
        for(String answerTxt : answersTxt) {
           this.addAnswerTxt(answerTxt);
        }
    }
    
    public void setAnswers(Set<PollItemAnswer> answers) throws DomainException {
        if (answers == null) {
            throw new DomainException("You cant set answers as empty");
        }
        for (PollItemAnswer answer : answers) {
            this.addAnswer(answer);
        }
    }

    /*--- METHODS ---*/
    public boolean containsAnswer(long answerId) {
        for (PollItemAnswer answer : this.getAnswers()) {
            if (answer.getId() == answerId) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAnswer(PollItemAnswer answerOG) {
        if (answerOG == null) {
            return false;
        }
        for (PollItemAnswer answer : this.getAnswers()) {
            if (answer.equals(answerOG)) {
                return true;
            }
        }
        return false;
    }

    public PollItemAnswer getAnswer(long answerId) throws DomainException {
        if (!this.containsAnswer(answerId)) {
            throw new DomainException("Could not find answer with id " + answerId);
        }
        PollItemAnswer theAnswer = null;
        for (PollItemAnswer answer : this.getAnswers()) {
            if (answer.getId() == answerId) {
                theAnswer = answer;
            }
        }
        return theAnswer;
    }

    public void addAnswerTxt(String answerText) throws DomainException {
        PollItemAnswer answer = new PollItemAnswer(answerText);
        this.addAnswer(answer);
    }

    public void addAnswer(PollItemAnswer answer) throws DomainException {
        if (answer == null) {
            throw new DomainException("You can not add an empty answer");
        }
        if (this.answers.contains(answer)) {
            throw new DomainException("It seems you already added this answer");
        }
        this.answers.add(answer);
    }

    public void removeAnwser(long answerId) throws DomainException {
        PollItemAnswer answer = this.getAnswer(answerId);
        this.answers.remove(answer);
    }

    public void addVoteToAnswer(long answerId) throws DomainException {
        PollItemAnswer answer = this.getAnswer(answerId);
        answer.addVote();
    }

    public void removeVoteFromAnswer(long answerId) throws DomainException {
        PollItemAnswer answer = this.getAnswer(answerId);
        answer.removeVote();
    }

    private void stateChanged() throws DomainException {
        this.setState(UpdateState.UPDATED);
        this.setDate(Calendar.getInstance());
    }

    public void update(String question) throws DomainException {
        this.setQuestion(question);
        this.stateChanged();
        this.setDate(Calendar.getInstance());
    }

    public int getTotalVotes() {
        int totalVotes = 0;
        for (PollItemAnswer answer : this.getAnswers()) {
            totalVotes += answer.getVotes();
        }
        return totalVotes;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof PollItem) {
            PollItem pollItem = (PollItem) object;
            if (pollItem.getQuestion().equalsIgnoreCase(this.getQuestion())) {
                return true;
            }
        }
        return false;
    }

    private String getAnswersString() {
        String answersString = "";
        for (PollItemAnswer answer : this.getAnswers()) {
            answersString += "Answer " + answer.getId()
                    + "\n" + answer.toString()
                    + "\n" + "Percentage: " + answer.getPercentage(this.getTotalVotes()) + "%"
                    + "\n---\n";
        }
        if (answersString.trim().isEmpty()) {
            answersString = "No answers added";
        }
        return answersString;
    }

    @Override
    public String toString() {
        String dateString = this.getDate().getTime().toString();
        String stateString = this.getState().getDisplayName();
        String pollItem = "";
        pollItem += "Poll " + stateString + " on:"
                + "\n" + dateString
                + "\nQuestion: " + this.getQuestion();
        pollItem += "\nAnswers: \n---\n" + this.getAnswersString();
        return pollItem;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (int) (super.getId() ^ (super.getId() >>> 32));
        hash = 89 * hash + Objects.hashCode(this.question);
        hash = 89 * hash + Objects.hashCode(this.displayDate);
        return hash;
    }
}
