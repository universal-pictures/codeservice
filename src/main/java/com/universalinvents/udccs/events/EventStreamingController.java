package com.universalinvents.udccs.events;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClientBuilder;
import com.amazonaws.services.kinesisfirehose.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.ByteBuffer;

@Configuration
public class EventStreamingController {

    @Value("${kinesis.firehose.delivery-stream}")
    private String streamName;

    private AmazonKinesisFirehose getFirehoseClient() {
        AmazonKinesisFirehose firehoseClient = AmazonKinesisFirehoseClientBuilder.standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .withRegion(Regions.US_EAST_2)
                .build();
        return firehoseClient;
    }

    public void putRecord(String data) {
        PutRecordRequest putRecordRequest = new PutRecordRequest();
        putRecordRequest.setDeliveryStreamName(this.streamName);

        Record record = new Record().withData(ByteBuffer.wrap(data.getBytes()));
        putRecordRequest.setRecord(record);

        // Put record into the delivery stream
        try {
            getFirehoseClient().putRecord(putRecordRequest);
        }
        catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        catch (ServiceUnavailableException e) {
            e.printStackTrace();
        }
    }
}
