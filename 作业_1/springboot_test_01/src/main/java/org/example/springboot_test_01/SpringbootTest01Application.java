package org.example.springboot_test_01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;



@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringbootTest01Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTest01Application.class, args);
    }

    Object obj = new Object();
    String str = "Hello World";
    int num = 10;
    //System.out.println(obj);
    //System.out.println(str);
}
