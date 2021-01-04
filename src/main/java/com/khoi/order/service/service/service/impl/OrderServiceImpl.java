package com.khoi.order.service.service.service.impl;

import com.khoi.basecrud.service.service.impl.BaseServiceImpl;
import com.khoi.order.dto.Order;
import com.khoi.order.dto.OrderItem;
import com.khoi.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, Integer> implements IOrderService {

  @Autowired OrderItemServiceImpl orderItemService;

  @Override
  public int updateOrderStatus(int id, int status) {
    try {
      Order order = findByid(id);
      order.setStatus(status);
      update(order);
      if (status != 3) {
        return order.getId();
      } else {
        List<OrderItem> list = orderItemService.getOrderItemsByOrderId(id);
        for (OrderItem item : list) {
          orderItemService.addStock(item.getStock_id(), item.getAmount());
        }
        return 1;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      return 0;
    }
  }
}
