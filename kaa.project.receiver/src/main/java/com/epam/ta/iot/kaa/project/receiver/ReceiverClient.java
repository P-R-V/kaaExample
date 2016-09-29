/**
 *  Copyright 2014-2016 CyberVision, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.epam.ta.iot.kaa.project.receiver;
//      org.kaaproject.kaa.demo.customReceiver.TstCustomReceiverDemo
import org.kaaproject.kaa.client.DesktopKaaPlatformContext;
import org.kaaproject.kaa.client.Kaa;
import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.notification.NotificationListener;
import org.kaaproject.kaa.client.notification.NotificationTopicListListener;
import org.kaaproject.kaa.client.notification.UnavailableTopicException;
import org.kaaproject.kaa.common.endpoint.gen.SubscriptionType;
import org.kaaproject.kaa.common.endpoint.gen.Topic;
//import org.kaaproject.kaa.demo.notification.SampleNotification3;
import org.kaaproject.kaa.demo.notification.SampleNotification3;
import org.kaaproject.kaa.schema.sample.event.thermo.ChangeDegreeRequest;
import org.kaaproject.kaa.schema.sample.event.thermo.ThermostatEventClassFamily;
//import org.kaaproject.kaa.schema.sample.event.thermo.ThermostatInfoRequest;
//import org.kaaproject.kaa.schema.sample.event.thermo.ThermostatInfoResponse;
//import org.kaaproject.kaa.schema.sample.event.thermo.ThermostatInfo;
import org.kaaproject.kaa.schema.sample.event.thermo.ThermostatInfoRequest;
import org.kaaproject.kaa.schema.sample.event.thermo.ThermostatInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * A demo application that shows how to use the Kaa notifications API.
 */
public class ReceiverClient {
    private static final Logger LOG = LoggerFactory.getLogger(ReceiverClient.class);
    private static KaaClient kaaClient;

    public static void main(String[] args) {
    	ReceiverClient receiverClient = new ReceiverClient();
    	receiverClient.run();
    }

    public void run() {
        LOG.info("TestCustomReceiverDemo demo started");
        LOG.info("--= Press any key to exit =--");
        kaaClient = Kaa.newClient(new DesktopKaaPlatformContext());

        // A listener that listens to the notification topic list updates.
        NotificationTopicListListener topicListListener = new BasicNotificationTopicListListener();
        kaaClient.addTopicListListener(topicListListener);

        // Add a notification listener that listens to all notifications.
        kaaClient.addNotificationListener(new NotificationListener() {

        	public void onNotification(long id, SampleNotification3 sampleNotificationEpam) {
                LOG.info("Notification for topic id [{}] received.", id);
                LOG.info("------------------------------------------------------------------------");
                LOG.info("----------------");
                LOG.info("Notification body: {}", sampleNotificationEpam.getMessage3());
                LOG.info("----------------");
                LOG.info("------------------------------------------------------------------------");
            }
        });

        
        //Create Listener
        EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        final ThermostatEventClassFamily thermostatEventClass = eventFamilyFactory.getThermostatEventClassFamily();
        ThermoEventListener thermoEventListener = new ThermoEventListener(); 
        thermostatEventClass.addListener(thermoEventListener);
        
        // Start the Kaa client and connect it to the Kaa server.
        kaaClient.start();

	

        try {
            
            // Wait for some input before exiting.
            System.in.read();
        } catch (IOException e) {
            LOG.error("IOException was caught", e);
        }

        // Stop the Kaa client and release all the resources which were in use.
        kaaClient.stop();
        LOG.info("Notification demo stopped");
    }

    // A listener that tracks the notification topic list updates
    // and subscribes the Kaa client to every new topic available.
    private class BasicNotificationTopicListListener implements NotificationTopicListListener {

    	public void onListUpdated(List<Topic> list) {
            LOG.info("Topic list was updated");
            showTopicList(list);
            try {
                //Try to subscribe to all new optional topics, if any.
                List<Long> optionalTopics = extractOptionalTopicIds(list);
                for(Long optionalTopicId : optionalTopics){
                    LOG.info("Subscribing to optional topic {}", optionalTopicId);
                }
                kaaClient.subscribeToTopics(optionalTopics, true);
            } catch (UnavailableTopicException e) {
                LOG.error("Topic is unavailable, can't subscribe: {}", e.getMessage());
            }
        }
    }

    private List<Long> extractOptionalTopicIds(List<Topic> list) {
        List<Long> topicIds = new ArrayList<>();
        for (Topic t : list) {
            if (t.getSubscriptionType() == SubscriptionType.OPTIONAL_SUBSCRIPTION) {
                topicIds.add(t.getId());
            }
        }
        return topicIds;
    }

    private void showTopicList(List<Topic> topics) {
        if (topics == null || topics.isEmpty()) {
            LOG.info("Topic list is empty");
        } else {
            for (Topic topic : topics) {
                LOG.info("Topic id: {}, name: {}, type: {}", topic.getId(), topic.getName(), topic.getSubscriptionType());
            }
        }
    }
    
    private class ThermoEventListener implements ThermostatEventClassFamily.Listener {

        private final Logger LOG2 = LoggerFactory.getLogger(ThermoEventListener.class);

    	public void onEvent(ThermostatInfoRequest event, String source) {
    		// TODO Auto-generated method stub
            LOG2.info("1-----------------------------------------------------------------------");
            LOG2.info("1---------------");
            LOG2.info("1---ThermostatInfoRequest---Notification body: {}", source);
            LOG2.info("1----------------");
            LOG2.info("1------------------------------------------------------------------------");

    	}

    	public void onEvent(ThermostatInfoResponse event, String source) {
    		// TODO Auto-generated method stub
            LOG2.info("2-----------------------------------------------------------------------");
            LOG2.info("2---------------");
            LOG2.info("2---ThermostatInfoResponse---Notification body: {}", source);
            LOG2.info("2----------------");
            LOG2.info("2------------------------------------------------------------------------");

    	}

    	public void onEvent(ChangeDegreeRequest event, String source) {
    		// TODO Auto-generated method stub
            LOG2.info("3-----------------------------------------------------------------------");
            LOG2.info("3---------------");
            LOG2.info("3---ChangeDegreeRequest---Notification body: {}", source);
            LOG2.info("3----------------");
            LOG2.info("3------------------------------------------------------------------------");
    		
    	}
    	
    }
    
}
