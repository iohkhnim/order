package com.khoi.order.dao;

import com.khoi.basecrud.dao.IBaseDAO;
import com.khoi.order.dto.OrderItem;
import java.util.List;


public interface IOrderItemDAO extends IBaseDAO<OrderItem, Integer> {

  int calculateTotalPrice(int order_id);

  List<OrderItem> getOrderItemsByOrderId(int order_id);
}
