package com.jerry.contentcenter;

import org.springframework.web.client.RestTemplate;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2021/2/16
 * Time: 18:35
 * Description:
 */
public class SentinelTest {

    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
//        for (int i = 0; i < 10000; i++) {
//            restTemplate.getForObject("http://localhost:8010/actuator/sentinel", String.class);
//            Thread.sleep(500);
//        }

        for (int i = 0; i < 10000; i++) {
            String object = restTemplate.getForObject("http://localhost:8010/testA", String.class);
            System.out.println(object);
        }
    }

}
