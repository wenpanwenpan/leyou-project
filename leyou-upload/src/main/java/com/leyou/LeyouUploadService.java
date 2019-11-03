package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * description
 *
 * @author 文攀 2019/10/21 14:00
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LeyouUploadService {
    public static void main(String[] args) {
        SpringApplication.run(LeyouUploadService.class, args);
    }
}
