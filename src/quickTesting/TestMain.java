
package quickTesting;

import dao.DAOFactory;
import domain.DomainException;
import domain.newsfeed.NewsfeedItem;
import domain.poll.PollItem;
import domain.poll.PollItemAnswer;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import service.MFASystem;
import service.ServiceException;

public class TestMain {
    public static void main(String[] arg) {
//        NewsfeedItem testFeed;
//        try{
//            testFeed = new NewsfeedItem("Test item", "this is an item to test that shit");
//            System.out.println(testFeed.toString());
//            testFeed.update("Test item update", "Item should be updated now");
//            System.out.println(testFeed.toString());
//        } catch (DomainException domEx) {
//            System.out.println(domEx.getMessage());
//        }
//        PollItem poll;
//        PollItemAnswer answer1;
//        PollItemAnswer answer2;
//        PollItemAnswer answer3;
//        PollItemAnswer answer1Copy;
//        
//        try{
//            poll = new PollItem("Is this A nice poll?");
//            answer1 = new PollItemAnswer("Yes");
//            answer2 = new PollItemAnswer("No");
//            answer3 = new PollItemAnswer("Maybe");
//            answer1Copy = new PollItemAnswer("Yes");
//
//            answer1.addVote();
//            poll.addAnswer(answer1);
//            poll.getAnswer(1).addVote();
//            System.out.println(poll.toString());
//
//            
//            
//        } catch(DomainException domEx) {
//            System.out.println(domEx.getMessage());
//        }
        
//        DAOFactory fac = DAOFactory.getInstance();
//        Set<String> supported = DAOFactory.getSupportedTypes();
//        
//        for(String type : supported) {
//            System.out.println(type);
//        }
        try{
            Properties props = new Properties();
            props.put("dbtype.poll", "memory");
            props.put("dbtype.newsfeed", "jpa");
            MFASystem sys = new MFASystem(props);
            for(NewsfeedItem newsfeed : sys.getNewsfeedItems()){
            System.out.println(newsfeed.toString());
            }
            
            System.out.println("\n");
            
        } catch (ServiceException serEx) {
            System.out.println(serEx.getMessage());
        }
    }
}
