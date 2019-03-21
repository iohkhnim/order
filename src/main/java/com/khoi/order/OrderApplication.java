package com.khoi.order;

import com.khoi.order.dao.IOrderItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderApplication {

  @Autowired
  public static IOrderItemDAO orderItemDAO;

  public static void main(String[] args) {
    SpringApplication.run(OrderApplication.class, args);
  }

}
