package com.rbkmoney.adapter.orangedata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class CashRegOrangeDataApplication extends SpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(CashRegOrangeDataApplication.class, args);
    }
}
