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

  /**
   * <p>An API endpoint (/order/create) with method POST creates an order</p>
   * @param order Order information
   * @return Https status according to result
   */
  @PostMapping("create")
  public ResponseEntity<String> create(@RequestBody Order order) {
    int id = orderService.create(order);
    if (id > 0) {
      return new ResponseEntity<String>(String.valueOf(id), HttpStatus.CREATED);
    } else {
      return new ResponseEntity<String>(String.valueOf(id), HttpStatus.CONFLICT);
    }
  }

  /**
   * <p>An API endpoint (/order/update) with method PUT updates an order</p>
   * @param order Order information
   * @return Https status according to result
   */
  @PutMapping("update")
  public ResponseEntity<Void> update(@RequestBody Order order) {
    Boolean flag = orderService.update(order);
    if (flag.equals(true)) {
      return new ResponseEntity<Void>(HttpStatus.OK);
    } else {
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
  }

  /**
   * <p>An API endpoint (/order/findById/{id}) with method GET gets information of given Order ID</p>
   * @param id Order ID
   * @return Return information of give Order ID
   */
  @GetMapping("findById/{id}")
  public ResponseEntity<Order> findByid(@PathVariable("id") int id) {
    Order obj = orderService.findByid(id);
    return new ResponseEntity<Order>(obj, HttpStatus.OK);
  }

  /**
   * <p>An API endpoint (/order/findAll) with method GET gets information of all Orders </p>
   * @return Return all orders information
   */
  @GetMapping("findAll")
  public ResponseEntity<List<Order>> findAll() {
    return new ResponseEntity<List<Order>>(orderService.findAll(), HttpStatus.OK);
  }

  /**
   * <p>An API endpoint (/order/delete/{id}) with method DELETE deletes an Order </p>
   * @param id Order ID needs to be deleted
   * @return Return Http status according to result
   */
  @DeleteMapping("delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") int id) {
    if (orderService.delete(id)) {
      return new ResponseEntity<Void>(HttpStatus.OK);
    } else {
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
  }
  @GetMapping("updateStatus/{id}/{status}")
  public ResponseEntity<Void> updateOrderStatus(@PathVariable("id") int id, @PathVariable("status") int status) {
    if (orderService.updateOrderStatus(id, status) > 0) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }
  }
}
