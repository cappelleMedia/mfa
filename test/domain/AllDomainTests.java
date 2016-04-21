/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import domain.newsfeed.NewsfeedItemTest;
import domain.poll.PollItemAnswerTest;
import domain.poll.PollItemTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Jens
 */
    @RunWith(Suite.class)
    @Suite.SuiteClasses({
        NewsfeedItemTest.class,
        PollItemAnswerTest.class,
        PollItemTest.class
    })
public class AllDomainTests {
}
