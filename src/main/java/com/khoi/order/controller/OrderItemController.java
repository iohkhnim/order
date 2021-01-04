package com.khoi.order.controller;

import com.khoi.order.dto.OrderItem;
import com.khoi.order.service.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("orderItem")
public class OrderItemController {

  @Autowired
  IOrderItemService orderItemService;

  /**
   * <p>An API endpoint (/orderItem/create) with method POST creates an order item</p>
   * @param orderItem Order item information
   * @return Https status according to result
   */
  @PostMapping("create")
  public ResponseEntity<String> create(@RequestBody OrderItem orderItem) {
    int id = orderItemService.create(orderItem);
    if (id > 0) {
      return new ResponseEntity<String>(String.valueOf(id), HttpStatus.CREATED);
    } else {
      return new ResponseEntity<String>(String.valueOf(id), HttpStatus.CONFLICT);
    }
  }

  /**
   * <p>An API endpoint (/orderItem/update) with method PUT updates an order item</p>
   * @param orderItem Order item information
   * @return Https status according to result
   */
  @PutMapping("update")
  public ResponseEntity<Void> update(@RequestBody OrderItem orderItem) {
    Boolean flag = orderItemService.update(orderItem);
    if (flag.equals(true)) {
      return new ResponseEntity<Void>(HttpStatus.OK);
    } else {
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
  }

  /**
   * <p>An API endpoint (/orderItem/findById/{id}) with method GET gets information of given Order item ID</p>
   * @param id Order item ID
   * @return Return information of give Order item ID
   */
  @GetMapping("findById/{id}")
  public ResponseEntity<OrderItem> findByid(@PathVariable("id") int id) {
    OrderItem obj = orderItemService.findByid(id);
    return new ResponseEntity<OrderItem>(obj, HttpStatus.OK);
  }

  /**
   * <p>An API endpoint (/orderItem/findAll) with method GET gets information of all Order items</p>
   * @return Return all order items information
   */
  @GetMapping("findAll")
  public ResponseEntity<List<OrderItem>> findAll() {
    return new ResponseEntity<List<OrderItem>>(orderItemService.findAll(), HttpStatus.OK);
  }

  /**
   * <p>An API endpoint (/orderItem/delete/{id}) with method DELETE deletes an Order item </p>
   * @param id Order item ID needs to be deleted
   * @return Return Http status according to result
   */
  @DeleteMapping("delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") int id) {
    if (orderItemService.delete(id)) {
      return new ResponseEntity<Void>(HttpStatus.OK);
    } else {
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
  }
}
