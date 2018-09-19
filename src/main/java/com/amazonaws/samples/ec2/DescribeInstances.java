/*
 * Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.samples.ec2;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

/**
 * Describes all EC2 instances associated with an AWS account
 */
public class DescribeInstances {


    public static void main(String[] args) {
        
        
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\LongShiLin\\.aws\\credentials), and is in valid format.",
                    e);
        }

        AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion("us-east-1")
            .build();
        
       
        boolean done = false;

        DescribeInstancesRequest request = new DescribeInstancesRequest();
        while (!done) {
            DescribeInstancesResult response = ec2.describeInstances(request);

            for (Reservation reservation : response.getReservations()) {
                for (Instance instance : reservation.getInstances()) {
                    if(instance.getPrivateIpAddress().split("\\.")[0].equals("172") && instance.getPrivateIpAddress().split("\\.")[2].equals("65")) {
                    System.out.printf(
                        "Found instance with name %s, " +
                            "Private address %s, " + 
                            "id %s, " +
                            "AMI %s, " +
                            "type %s, " +
                            "state %s, " +
                            "and monitoring state %s," +
                            "Cpu Core Count %s," +
                            "Thread Per Core %s,",
                        instance.getKeyName(),
                        instance.getPrivateIpAddress(),
                        instance.getInstanceId(),
                        instance.getImageId(),
                        instance.getInstanceType(),
                        instance.getState().getName(),
                        instance.getMonitoring().getState(),
                        instance.getCpuOptions().getCoreCount(),
                        instance.getCpuOptions().getThreadsPerCore());
                    System.out.println();
                    }
                }
            }

            if (response.getNextToken() == null) {
                done = true;
            }
        }
    }
}

