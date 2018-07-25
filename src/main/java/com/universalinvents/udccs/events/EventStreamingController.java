package com.universalinvents.udccs.events;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClientBuilder;
import com.amazonaws.services.kinesisfirehose.model.PutRecordRequest;
import com.amazonaws.services.kinesisfirehose.model.Record;
import org.springframework.beans.factory.annotation.Value;

import java.nio.ByteBuffer;

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
        getFirehoseClient().putRecord(putRecordRequest);
    }
}
