package com.epam.ta.iot.kaa.project.receiver;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class RecieverTest 
{
	@Test
    public void testReciever()
    {
    	ReceiverClient receiverClient = new ReceiverClient();
    	receiverClient.run();
    }
}
