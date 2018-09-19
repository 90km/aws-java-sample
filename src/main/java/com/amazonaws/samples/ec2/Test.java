package com.amazonaws.samples.ec2;

public class Test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        String ip = "172.31.65.5";
        
        String[] str = ip.split("\\.");
        
        System.out.println(str[0] + " - " + str[2]);
        
    }

}
