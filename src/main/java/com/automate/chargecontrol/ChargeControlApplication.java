package com.automate.chargecontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
public class ChargeControlApplication {

  public static void main(String[] args) {
    SpringApplication
        .run(ChargeControlApplication.class, args);
  }

}
