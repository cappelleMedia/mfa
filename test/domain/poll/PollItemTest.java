/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.poll;

import domain.DomainException;
import domain.UpdateState;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Jens
 */
public class PollItemTest {
    /*--- PollItemAnswer variables ---*/
    private String answerText;
    private PollItemAnswer answer1, answer2, answer3, answer4;

    
    /*--- PollItem variables ---*/
    private String question;
    private Set<PollItemAnswer> answersSet1;
    
    private PollItem poll;

    @Before
    public void setUp() throws DomainException {
        /*--- PollItemAnswer set up ---*/
        answerText = "First answer";
        answer1 = new PollItemAnswer(answerText);
        answer1.addVote();
        
        answerText = "Second answer";
        answer2 = new PollItemAnswer(answerText);
        answer2.addVote();
        
        answerText = "Third answer";
        answer3 = new PollItemAnswer(answerText);
        answer3.addVote();
        
        answerText = "Fourth answer";
        answer4 = new PollItemAnswer(answerText);
        
        /*--- PollItem set up ---*/
        question = "Test question";
        answersSet1 = new LinkedHashSet<>();
        answersSet1.add(answer2);        
        answersSet1.add(answer3);
        answersSet1.add(answer4);
        poll = new PollItem(question);
        poll.addAnswer(answer1);
    }
    
    @Test
    public void test_constructor_validQuestion_createsPoll() throws DomainException { 
        PollItem pollItem = new PollItem("A Test question");
        Assert.assertEquals(-1L, pollItem.getId());
        Assert.assertFalse(pollItem.getDate()== null);
        Assert.assertEquals(UpdateState.ADDED, pollItem.getState());
        Assert.assertFalse(pollItem.getAnswers() == null);
        Assert.assertEquals("A Test question?", pollItem.getQuestion());
    }
    
    @Test
    public void test_setQuestion_validStringWithoutQuestionMark_setsQuestionAndAddsQuestionMark() throws DomainException {
        PollItem pollItem = new PollItem(question);
        Assert.assertEquals("Test question?", pollItem.getQuestion());
    }
    
    @Test
    public void test_setQuestion_validStringWithQuestionMark_setsQuestion() throws DomainException {
        PollItem pollItem = new PollItem(question+"?");
        Assert.assertEquals("Test question?", pollItem.getQuestion());
    }
    
    @Test(expected = DomainException.class)
    public void test_setQuestion_null_throwsDomainException() throws DomainException {
        poll.setQuestion(null);
    }
    @Test(expected = DomainException.class)
    public void test_setQuestion_emptyString_throwsDomainException() throws DomainException {
        poll.setQuestion(" ");
    }
    
    @Test
    public void test_addAnswer_validAnswer_addsAnswerAndAssignsFollowUp() throws DomainException {
        poll.addAnswer(answer2);
        Assert.assertTrue(poll.getAnswers().contains(answer2));
        Assert.assertTrue(answer2.getId()>0);
    }
    
    @Test(expected = DomainException.class)
    public void test_addAnswer_null_throwsDomainException() throws DomainException{
        poll.addAnswer(null);
    }
    
    @Test(expected = DomainException.class)
    public void test_addAnswer_answerAlreadyPresent_throwsDomainException() throws DomainException{
        poll.addAnswer(answer1);
    }
    
    @Test
    public void test_setAnswers_validAnswerSet_addsAnswers() throws DomainException {
        poll.setAnswers(answersSet1);
        Assert.assertTrue(poll.getAnswers().size()>1);
        Assert.assertTrue(poll.getAnswers().contains(answer1));
    }
    
    @Test (expected = DomainException.class)
    public void test_setAnswers_nullSet_throwsDomainException() throws DomainException {
        poll.setAnswers(null);
    }
    
    @Test
    public void test_replaceAnswers_validSet_removesExcistingAnswersAndAddsAnswersInSet() throws DomainException {
        poll.replaceAnswers(answersSet1);
        Assert.assertTrue(poll.getAnswers().size()== 3);
        Assert.assertFalse(poll.getAnswers().contains(answer1));
    }
    
    @Test (expected = DomainException.class)
    public void test_replaceAnswers_null_doesNotRemoveExcistingAndThrowsDomainException() throws DomainException {
        try{
            poll.setAnswers(null);
        } catch(DomainException domEx) {
            Assert.assertTrue(poll.getAnswers().contains(answer1));
            throw new DomainException(domEx.getMessage());
        }
    }
    
    @Test
    public void test_containsAnswer_ByFollowUp_AnswerPresent_returnsTrue() {
        Assert.assertTrue(poll.containsAnswer(1));
    }
    
    @Test
    public void test_containsAnswer_ByFollowUp_AnswerNotPresent_returnsFalse() {
        Assert.assertFalse(poll.containsAnswer(50));
    }
    
    @Test
    public void test_containsAnswer_ByObject_AnswerPresent_returnsTrue() {
        Assert.assertTrue(poll.containsAnswer(answer1));
    }
    
    @Test
    public void test_containsAnswer_ByObject_AnswerNotPresent_returnsFalse() {
        Assert.assertFalse(poll.containsAnswer(answer2));
    }
    
    @Test
    public void test_containsAnswer_ByObject_null_returnsFalse() {
        Assert.assertFalse(poll.containsAnswer(null));
    }
    
    @Test 
    public void test_getAnswer_AnswerWithFollowUpPresent_returnsAnswer() throws DomainException {
        PollItemAnswer answer = poll.getAnswer(1);
        Assert.assertFalse(answer == null);
        Assert.assertEquals(answer1, answer);
    }
    
    @Test (expected = DomainException.class) 
    public void test_getAnswer_AnswerWithFollowUpNotPresent_throwsDomainException() throws DomainException {
        PollItemAnswer answer = poll.getAnswer(50);
    }
    
    @Test
    public void test_removeAnswer_followUpPresent_removesAnswer() throws DomainException {
        poll.removeAnwser(1);
        Assert.assertFalse(poll.getAnswers().contains(answer1));
    }
    
    @Test (expected = DomainException.class)
    public void test_removeAnswer_followUpNotPresent_throwsDomainException() throws DomainException{
        poll.removeAnwser(50);
    }
    
    @Test
    public void test_addVoteToAnswer_answerPresent_addsVoteToAnswer() throws DomainException {
        poll.addVoteToAnswer(1);
        Assert.assertEquals(2, answer1.getVotes());
    }
    
    @Test (expected = DomainException.class)
    public void test_addVoteToAnswer_answerNotPresent_throwsDomainException() throws DomainException {
        poll.addVoteToAnswer(50);
    }
    
    @Test
    public void test_removeVoteFromAnswer_answerPresentAndAtLeastOneVote_removesVoteFromAnswer() throws DomainException {
        poll.removeVoteFromAnswer(1);
        Assert.assertTrue(answer1.getVotes()== 0);
    }
    
    @Test
    public void test_removeVoteFromAnswer_answerPresentButNoVotes_doesNotRemoveAVote() throws DomainException {
        poll.setAnswers(answersSet1);
        Assert.assertEquals(0, answer4.getVotes());
        poll.removeVoteFromAnswer(4);
        Assert.assertEquals(0, answer4.getVotes());
    }
    
    @Test (expected = DomainException.class)
    public void test_removeVoteFromAnswer_answerNotPresent_throwsDomainException() throws DomainException{
        poll.removeVoteFromAnswer(50);
    }
    
    @Test
    public void test_update_updatesQuestionAndchangesDateAndState() throws DomainException {
        Calendar cal = Calendar.getInstance();
        poll.update("updated");
        Assert.assertEquals("updated?", poll.getQuestion());
        Assert.assertEquals(UpdateState.UPDATED, poll.getState());
        Assert.assertTrue(poll.getDate().after(cal) || poll.getDate().equals(cal)); //FIXME not a great way to check
    }//Not necessary to test for exception --> same as setQuestion()
    
    @Test
    public void test_getTotalVotes() throws DomainException{
        poll.setAnswers(answersSet1);
        Assert.assertEquals(3, poll.getTotalVotes());
    }
    
}
