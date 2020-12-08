package com.jerry.contentcenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2020/12/8
 * Time: 22:03
 * Description:
 */
@SpringBootTest
public class RestTemplateTest {

    @Test
    public void test() {
        RestTemplate restTemplate = new RestTemplate();
        String object = restTemplate.getForObject("http://localhost:8080/users/1", String.class);
        String object2 = restTemplate.getForObject("http://localhost:8080/users/{id}", String.class, 2);
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/users/{id}",
                String.class, 2);
        System.out.println(object);
        System.out.println(object2);
        System.out.println(entity);
    }

}
