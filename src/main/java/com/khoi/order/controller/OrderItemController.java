package com.khoi.order.controller;

import com.khoi.order.dto.OrderItem;
import com.khoi.order.service.IOrderItemService;
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
@RequestMapping("orderItem")
public class OrderItemController {

  @Autowired
  IOrderItemService orderItemService;

  @PostMapping("create")
  public ResponseEntity<Void> create(@RequestBody OrderItem orderItem) {
    Boolean flag = orderItemService.create(orderItem);
    if (flag.equals(true)) {
      return new ResponseEntity<Void>(HttpStatus.CREATED);
    } else {
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
  }

  @PutMapping("update")
  public ResponseEntity<Void> update(@RequestBody OrderItem orderItem) {
    Boolean flag = orderItemService.update(orderItem);
    if (flag.equals(true)) {
      return new ResponseEntity<Void>(HttpStatus.OK);
    } else {
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
  }

  @GetMapping("findById/{id}")
  public ResponseEntity<OrderItem> findByid(@PathVariable("id") int id) {
    OrderItem obj = orderItemService.findByid(id);
    return new ResponseEntity<OrderItem>(obj, HttpStatus.OK);
  }

  @GetMapping("findAll")
  public ResponseEntity<List<OrderItem>> findAll() {
    return new ResponseEntity<List<OrderItem>>(orderItemService.findAll(), HttpStatus.OK);
  }

  @DeleteMapping("delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") int id) {
    if (orderItemService.delete(id)) {
      return new ResponseEntity<Void>(HttpStatus.OK);
    } else {
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
  }
}
