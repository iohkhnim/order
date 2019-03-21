package com.khoi.order.service;

import com.khoi.basecrud.service.IBaseService;
import com.khoi.order.dto.OrderItem;
import com.khoi.orderproto.CheckoutDataProto;
import java.util.List;

public interface IOrderItemService extends IBaseService<OrderItem, Integer> {

  Boolean create(CheckoutDataProto checkoutDataProto, int order_id);

  int calculateTotalPrice(int order_id);

  int getSupplierIdByStockId(int stock_id);

  List<OrderItem> getOrderItemsByOrderId(int order_id);

  String getSupplierNameById(int supplier_id);

  String getProductNameById(int product_id);
}
