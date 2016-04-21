package domain.newsfeed;


import domain.DaoObject;
import domain.DomainException;
import domain.UpdateState;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;

@Entity
public class NewsfeedItem extends DaoObject implements Serializable{   
    private String subject, text; //TODO add multi langs
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar displayDate;
    @Enumerated
    private UpdateState displayState;
    
    public NewsfeedItem() {
        this.init();
    }
    
    public NewsfeedItem(String subject, String text) throws DomainException{
        this();
        this.setSubject(subject);
        this.setText(text);
    }
    
    private void init() {
        this.displayDate = Calendar.getInstance();
        this.displayState = UpdateState.ADDED;
        
    }
    
    public Calendar getDate() {
        return this.displayDate;
    }
    
    public UpdateState getState() {
        return this.displayState;
    }
    
    public String getSubject() {
        return this.subject;
    }
    
    public String getText() {
        return this.text;
    }    
    
    public void setSubject(String subject) throws DomainException {
        if(subject == null || subject.trim().isEmpty()) {
            throw new DomainException("A newsfeed item needs subject can not be left empty");
        }
        subject = subject.substring(0, 1).toUpperCase()+subject.substring(1);
        this.subject = subject;
    }
    
    public void setText(String text) throws DomainException{
        if(text == null || text.trim().isEmpty()) {
            throw new DomainException("A newsfeed item needs text to be useful");
        }
        text = text.substring(0, 1).toUpperCase()+text.substring(1);
        this.text = text;
    }
    
    public void update(String subject, String text) throws DomainException {
        this.setSubject(subject);
        this.setText(text);
        this.displayDate = Calendar.getInstance();
        this.displayState = UpdateState.UPDATED;
    }
    
    @Override 
    public String toString() {
        String dateString = this.getDate().getTime().toString();
        String stateString = this.getState().getDisplayName();
        String newsfeed = stateString+ " on:"+ 
                "\n" + dateString + 
                "\n" + this.getSubject() + 
                "\n" + this.getText();
        return newsfeed;
    }
    
    @Override
    public boolean equals(Object object) {
        boolean equal = false;
        if(object instanceof NewsfeedItem) {
            NewsfeedItem item = (NewsfeedItem) object;
            if(item.getSubject().equals(this.getSubject())&&
                    item.getText().equals(this.getText())){
                equal = true;
            }
        }
        return equal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (super.getId() ^ (super.getId() >>> 32));
        hash = 97 * hash + Objects.hashCode(this.text);
        hash = 97 * hash + Objects.hashCode(this.displayDate);
        return hash;
    }

}
