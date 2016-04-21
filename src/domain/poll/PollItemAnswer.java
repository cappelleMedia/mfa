package domain.poll;

import domain.DaoObject;
import domain.DomainException;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;

@Entity
public class PollItemAnswer extends DaoObject implements Serializable{

    private String answerText; //TODO add multi langs
    private int votes;
    
    
    public PollItemAnswer(){
        
    }
    
    public PollItemAnswer(String answerText) throws DomainException{
        this.setAnswerText(answerText);
    }
    
    public String getAnswerText() {
        return this.answerText;
    }
    
    public int getVotes() {
        return this.votes;
    }
    
    public void setAnswerText(String answerText) throws DomainException {
        if(answerText == null || answerText.trim().isEmpty()) {
            throw new DomainException("A poll answer needs text to be useful");
        }
        this.answerText = answerText;
    }
    
    public void addVote() {
        this.votes++;
    }
    
    public void removeVote() {
        if(this.votes > 0){
            this.votes--;
        }
    }
 
    public double getPercentage(int totalVotes) {
        double percentage = 0;
        if(totalVotes > 0 && this.getVotes() > 0){
            double thisVotesD = (double) this.getVotes();
            double totalVotesD = (double) totalVotes;
            percentage = (thisVotesD/totalVotesD) * 100;
        }
        return percentage;
    }    
    
    @Override
    public String toString() {
        String pollItemAnswer = "";
        pollItemAnswer+= "Answer: "+this.getAnswerText();
        pollItemAnswer+= "\nVotes: " + this.getVotes();
        return pollItemAnswer;
    }
    
    @Override
    public boolean equals(Object object) {
        if(object instanceof PollItemAnswer) {
            PollItemAnswer answer = (PollItemAnswer) object;
            if(answer.getAnswerText().equalsIgnoreCase(this.getAnswerText())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.answerText);
        hash = 71 * hash + this.votes;
        hash = 71 * hash + (int) (super.getId() ^ (super.getId() >>> 32));
        return hash;
    }
   
   
}
