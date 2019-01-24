/*
 * Copyright 2019 Universal City Studios LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.universalinvents.codeservice.messaging;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazon.sqs.javamessaging.SQSSession;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.jms.*;

@Configuration
public class MessagingController {

    @Value("${aws.region}")
    private String awsRegionName;

    private SQSConnection createConnection() throws JMSException {
        // Create the connection factory using the environment variable credential provider.
        // This assumes that the AWS_ACCESS_KEY_ID (or AWS_ACCESS_KEY) and AWS_SECRET_KEY (or AWS_SECRET_ACCESS_KEY)
        // environment variables are set.
        //
        // These connections can talk to queues in the configured region.
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(new ProviderConfiguration(),
                AmazonSQSClientBuilder.standard().withRegion(
                        Regions.fromName(awsRegionName)).withCredentials(
                        new EnvironmentVariableCredentialsProvider()));

        // Create the connection
        return (connectionFactory.createConnection());
    }

    public void sendMessage(String queueName, String message) throws JMSException {
        // Create the nontransacted session with UNORDERED_ACKNOWLEDGE mode.
        // The client must acknowledge every message for them to get deleted from the queue.
        Session session = createConnection().createSession(false, SQSSession.UNORDERED_ACKNOWLEDGE);

        // Create a queue identity and specify the queue name to the session
        Queue queue = session.createQueue(queueName);

        // Create a producer for the given queue
        MessageProducer producer = session.createProducer(queue);

        // Create the text message
        TextMessage textMessage = session.createTextMessage(message);

        // Set the message group ID (necessary for FIFO queues)
        textMessage.setStringProperty("JMSXGroupID", "Default");

        // You can also set a custom message deduplication ID
        // message.setStringProperty("JMS_SQS_DeduplicationId", "hello");
        // Here, it's not needed because content-based deduplication is enabled for the queue

        // Send the message
        producer.send(textMessage);
    }
}
