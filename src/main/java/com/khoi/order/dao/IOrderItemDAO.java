package com.khoi.order.dao;

import com.khoi.basecrud.dao.IBaseDAO;
import com.khoi.order.dto.OrderItem;
import java.util.List;


public interface IOrderItemDAO extends IBaseDAO<OrderItem, Integer> {

  /**
   * <p>This method calculates total price of an order</p>
   *
   * @param order_id Order ID needs to be calculated price
   * @return Total price of given order
   */
  int calculateTotalPrice(int order_id);

  /**
   * <p>This method retrieves all order items of given order in database</p>
   *
   * @param order_id Order ID needs to be retrieved its order items
   * @return All order items belong to provided Order
   */
  List<OrderItem> getOrderItemsByOrderId(int order_id);
}
