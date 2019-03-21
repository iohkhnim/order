package com.khoi.order.dao;

import com.khoi.basecrud.dao.IBaseDAO;
import com.khoi.order.dto.Order;
import java.util.List;

public interface IOrderDAO extends IBaseDAO<Order, Integer> {

  List<Order> getOrdersByCustomerId(int customer_id);
}
