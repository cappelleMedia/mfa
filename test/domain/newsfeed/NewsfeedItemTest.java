/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.newsfeed;

import domain.DomainException;
import domain.UpdateState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Jens
 */
public class NewsfeedItemTest {
    
    private long id; 
    private String subject, text;
    private NewsfeedItem newsFeed;
    
    @Before
    public void setUp() throws DomainException{
        id = 1L;
        subject = "Test feed";
        text = "Newest test feed!!";
        newsFeed = new NewsfeedItem(subject, text);
    }

    @Test
    public void test_construct_createsNewNewsFeedItem() throws DomainException {
        NewsfeedItem newsFeedItem = new NewsfeedItem("Test 1", "First test feed");
        Assert.assertFalse(newsFeedItem.getState() == null);
        Assert.assertFalse(newsFeedItem.getDate() == null);
    }
    
    @Test
    public void test_setSubject_validSubject_setsSubject() throws DomainException {
        newsFeed.setSubject("New subject");
        Assert.assertFalse(subject.equals(newsFeed.getSubject()));
    }
    
    @Test (expected = DomainException.class)
    public void test_setSubject_nullSubject_throwsDomainException() throws DomainException {
        newsFeed.setSubject(null);
    }
    
    @Test (expected = DomainException.class)
    public void test_setSubject_emptySubject_throwsDomainException() throws DomainException {
        newsFeed.setSubject(" ");
    }
    
    @Test
    public void test_setText_validSubject_setsText() throws DomainException {
        newsFeed.setText("New text");
        Assert.assertFalse(text.equals(newsFeed.getText()));
    }
    
    @Test (expected = DomainException.class)
    public void test_setText_nullText_throwsDomainException() throws DomainException {
        newsFeed.setText(null);
    }
    
    @Test (expected = DomainException.class)
    public void test_setTexst_emptyText_throwsDomainException() throws DomainException {
        newsFeed.setText(" ");
    }
    
    @Test 
    public void test_update_validValues_changesSubjectAndText_changesState() throws DomainException {
        newsFeed.update("Updated subject", "Updated text");
        Assert.assertFalse(newsFeed.getSubject().equals(subject));
        Assert.assertFalse(newsFeed.getText().equals(text));
        Assert.assertFalse(newsFeed.getState().equals(UpdateState.ADDED));
    }
    
}
