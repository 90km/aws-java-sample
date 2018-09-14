package com.amazonaws.samples.ec2;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;

public class GetInstance {

    // 创建覆盖所有可配置属性的
    private final AmazonEC2ClientBuilder standard = AmazonEC2ClientBuilder.standard()
        .withCredentials(new ProfileCredentialsProvider("C:/Users/LongShiLin/.aws/credentials","credentials"))
        .withRegion(Regions.US_WEST_2)
        .withClientConfiguration(new ClientConfiguration().withRequestTimeout(5000));


    // 使用生成器作为客户端实例的工厂
    public AmazonEC2 createClient() {
        return standard.build();
    }

    // main method
    public static void main(String[] args) {
        AmazonEC2Client ec2 = new AmazonEC2Client();
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        ec2.setRegion(usWest2);

        ec2.getServiceName();

        // 关闭ec2客户端
        ec2.shutdown();

    }

}
