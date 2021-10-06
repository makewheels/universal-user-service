package com.github.makewheels.universaluserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

`@EnableDiscoveryClient
@SpringBootApplication
public class UniversalUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversalUserServiceApplication.class, args);
    }

}
