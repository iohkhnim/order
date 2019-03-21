package com.khoi.order.controller;

import com.khoi.order.dto.Order;
import com.khoi.order.service.IOrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("order")
public class OrderController {

  @Autowired
  IOrderService orderService;

  @PostMapping("create")
  public ResponseEntity<Void> create(@RequestBody Order order) {
    Boolean flag = orderService.create(order);
    if (flag.equals(true)) {
      return new ResponseEntity<Void>(HttpStatus.CREATED);
    } else {
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
  }

  @PutMapping("update")
  public ResponseEntity<Void> update(@RequestBody Order order) {
    Boolean flag = orderService.update(order);
    if (flag.equals(true)) {
      return new ResponseEntity<Void>(HttpStatus.OK);
    } else {
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
  }

  @GetMapping("findById/{id}")
  public ResponseEntity<Order> findByid(@PathVariable("id") int id) {
    Order obj = orderService.findByid(id);
    return new ResponseEntity<Order>(obj, HttpStatus.OK);
  }

  @GetMapping("findAll")
  public ResponseEntity<List<Order>> findAll() {
    return new ResponseEntity<List<Order>>(orderService.findAll(), HttpStatus.OK);
  }

  @DeleteMapping("delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") int id) {
    if (orderService.delete(id)) {
      return new ResponseEntity<Void>(HttpStatus.OK);
    } else {
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
  }
}
