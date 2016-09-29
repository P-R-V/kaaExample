package com.epam.ta.iot.kaa.project.sender;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class SenderTest 
{
    @Test
    public void testSender()
    {
		SenderClient senderClient = new SenderClient();
    	senderClient.run();
    }
}
