package com.universalinvents.udccs.events;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.Record;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.ByteBuffer;

@Configuration
public class EventStreamingController {

    @Value("${kinesis.delivery-stream}")
    private String streamName;

    @Value("${aws.region}")
    private String awsRegionName;

    public void putRecord(String data) {
        PutRecordRequest putRecordRequest = new PutRecordRequest();
        putRecordRequest.setStreamName(this.streamName);

        Record record = new Record().withData(ByteBuffer.wrap(data.getBytes()));
        putRecordRequest.setData(record.getData());
        putRecordRequest.setPartitionKey("partitionKey-1");
        // Put record into the delivery stream
        getKinesisClient().putRecord(putRecordRequest);
    }

    private AmazonKinesis getKinesisClient() {
        AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .withRegion(Regions.fromName(awsRegionName));

        return clientBuilder.build();
    }
}
