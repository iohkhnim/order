package com.khoi.order.service;

import com.khoi.basecrud.service.IBaseService;
import com.khoi.order.dto.OrderItem;
import com.khoi.orderproto.CheckoutDataProto;
import java.util.List;

public interface IOrderItemService extends IBaseService<OrderItem, Integer> {

  /**
   * <p>This methods creates order items belong to an order</p>
   *
   * @param checkoutDataProto Contain order items information
   * @param order_id Order ID of all order items
   * @return Return a boolean value according to result
   */
  int create(CheckoutDataProto checkoutDataProto, int order_id);

  /**
   * <p>This method calculates total price of an order</p>
   *
   * @param order_id Order ID need to be calculated
   * @return Return total price of provided order
   */
  int calculateTotalPrice(int order_id);

  /**
   * <p>This method returns supplier ID of provided stock ID through supplier gRPC server</p>
   *
   * @param stock_id stock ID need to be got supplier ID
   * @return supplier ID of provided stock ID
   */
  int getSupplierIdByStockId(int stock_id);

  /**
   * <p>This method return all order items of an order </p>
   *
   * @param order_id Order ID that needs to be got order items
   * @return All order items information of provided order ID
   */
  List<OrderItem> getOrderItemsByOrderId(int order_id);

  /**
   * <p>This method gets supplier name of a given supplier ID through supplier gRPC server</p>
   *
   * @param supplier_id supplier ID needs to be retrieved its name
   * @return supplier name of given supplier ID
   */
  String getSupplierNameById(int supplier_id);

  /**
   * <p>This method gets product name of a given product ID through product gRPC server</p>
   *
   * @param product_id product ID needs to be retrieved its name
   * @return product name of given product ID
   */
  String getProductNameById(int product_id);
}
