package org.example.springboot_test_01;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;



@SpringBootApplication
@MapperScan("org.example.springboot_test_01") // 扫描 Mapper 接口的包路径
public class SpringbootTest01Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTest01Application.class, args);
    }

}
