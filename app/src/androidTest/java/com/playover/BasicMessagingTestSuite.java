package com.playover;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SignInTestSetup.class,
        MessagingBubblesTest.class,
        MessagingThreadsTest.class
})
public class BasicMessagingTestSuite {



}
