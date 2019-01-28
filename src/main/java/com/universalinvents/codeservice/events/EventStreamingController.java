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

package com.universalinvents.codeservice.events;

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
