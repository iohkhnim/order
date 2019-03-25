package com.khoi.order.dao;

import com.khoi.basecrud.dao.IBaseDAO;
import com.khoi.order.dto.Order;
import java.util.List;

public interface IOrderDAO extends IBaseDAO<Order, Integer> {

  /**
   * <p>This method gets all orders has been placed by given customer ID</p>
   *
   * @param customer_id customer ID needs to be got its order information
   * @return All orders information belong to given customer
   */
  List<Order> getOrdersByCustomerId(int customer_id);

  /**
   * <p>This method checks if given order belongs to given customer</p>
   *
   * @param customer_id Customer ID gave this Order ID
   * @param order_id Order ID given by customer
   * @return Return A boolean value according to result
   */
  Boolean authenticateOrderOwner(int customer_id, int order_id);

  /**
   * <p>This method returns order information of given Order ID</p>
   *
   * @param order_id Order ID needs to be retrieved its information
   * @return Retunr information of given Order ID
   */
  Order getOrderByOrderId(int order_id);
}
