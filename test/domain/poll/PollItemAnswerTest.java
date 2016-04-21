/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.poll;

import domain.DomainException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Jens
 */
public class PollItemAnswerTest {
    private String answerText;
    private PollItemAnswer answer1;
    
    @Before
    public void setUp() throws DomainException {
        answerText = "First answer text";
        answer1 = new PollItemAnswer(answerText);
    }
    
    @Test
    public void test_constructor_validVals_createsPollItemAnswer() throws DomainException {
        PollItemAnswer answer = new PollItemAnswer(answerText);
        Assert.assertTrue(answer.getVotes()==0);
        Assert.assertTrue(answer.getAnswerText().equals(answerText));
    }
    
    @Test
    public void test_setAnswerText_textNotNullAndNotEmpty_setsAnswerText() throws DomainException {
        answer1.setAnswerText("Not null, not empty");
        Assert.assertFalse(answer1.getAnswerText().equals(answerText));
    }
    
    @Test (expected = DomainException.class)
    public void test_setAnswserTextNull_throwsDomainException() throws DomainException {
        answer1.setAnswerText(null);
    }
    
    @Test (expected = DomainException.class)
    public void test_setAnswserTextEmpty_throwsDomainException() throws DomainException {
        answer1.setAnswerText(" ");
    }
    
    @Test
    public void test_addVote_addsVoteToTotal() {
        answer1.addVote();
        Assert.assertTrue(answer1.getVotes()>0);
    }
    
    @Test
    public void test_removeVote_DoesNotRemoveIfAlreadyZero() {
        answer1.removeVote();
        Assert.assertTrue(answer1.getVotes()==0);
    }
    
    @Test
    public void test_removeVote_removesVoteIfNotZero() {
        answer1.addVote();
        Assert.assertTrue(answer1.getVotes()>0);
        answer1.removeVote();
        Assert.assertTrue(answer1.getVotes()==0);
        
    }
    
    @Test
    public void test_getPercentage_returnsPercentage() {
        answer1.addVote();
        Assert.assertEquals(50.0, answer1.getPercentage(2),0);
        
    }
    
}
