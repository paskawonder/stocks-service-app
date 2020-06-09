package com.payconiq.stocks;

import com.payconiq.stocks.config.BasicConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(BasicConfiguration.class)
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class);
    }

}