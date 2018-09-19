/*
 * Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except
 * in compliance with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.amazonaws.samples.ec2;

import java.util.HashMap;
import java.util.Map.Entry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

/**
 * Describes all EC2 instances associated with an AWS account
 */
public class EC2Instances {

    private Regions region; // EC2实例所在的地区
    private HashMap<String, Instance> EC2Instances;

    public Regions getRegion() {
        return region;
    }

    public void setRegion(Regions region) {
        this.region = region;
    }

    // 获取整个Region区域的所有EC2实例对象
    public EC2Instances(Regions region) {
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (C:\\Users\\LongShiLin\\.aws\\credentials), and is in valid format.", e);
        }

        AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
        EC2Instances = new HashMap<>();

        boolean done = false;

        DescribeInstancesRequest request = new DescribeInstancesRequest().withInstanceIds();
        while (!done) {
            DescribeInstancesResult response = ec2.describeInstances(request);

            for (Reservation reservation : response.getReservations()) {
                for (Instance instance : reservation.getInstances()) {
                    EC2Instances.put(instance.getInstanceId(), instance);
                }
            }

            if (response.getNextToken() == null) {
                done = true;
            }
        }
    }
    
    public HashMap<String, Instance> getEC2Instances() {
        return EC2Instances;
    }

    // main method
    public static void main(String args[]) {

        EC2Instances ec2instances = new EC2Instances(Regions.US_EAST_2);
        HashMap<String, Instance> instances = ec2instances.getEC2Instances();

        // 打印指定EC2实例的CPU核数
        String EC2InstanceId = "i-0c7dd4279423c0162";
        Instance EC2instance = instances.get(EC2InstanceId);
        System.out.println("实例"+EC2InstanceId+"的CPU核数是："+EC2instance.getCpuOptions().getCoreCount());
        
        // 打印所有的EC2实例信息
        for (Entry<String, Instance> entry : instances.entrySet()) {
            Instance instance = entry.getValue();
            System.out.printf(
                    "Found instance with id %s, " +
                        "AMI %s, " +
                        "type %s, " +
                        "state %s, " +
                        "and monitoring state %s," +
                        "Cpu Core Count %s",
                    instance.getInstanceId(),
                    instance.getImageId(),
                    instance.getInstanceType(),
                    instance.getState().getName(),
                    instance.getMonitoring().getState(),
                    instance.getCpuOptions().getCoreCount());
                System.out.println();
        }
    }
}

