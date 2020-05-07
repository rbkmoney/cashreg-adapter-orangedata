package com.rbkmoney.adapter.orangedata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class CashregOrangeDataApplication extends SpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(CashregOrangeDataApplication.class, args);
    }
}
