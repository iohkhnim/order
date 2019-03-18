package com.khoi.order.service;

import com.khoi.basecrud.service.IBaseService;
import com.khoi.order.dto.OrderItem;
import com.khoi.orderproto.CheckoutDataProto;

public interface IOrderItemService extends IBaseService<OrderItem, Integer> {
    Boolean create(CheckoutDataProto checkoutDataProto, int order_id);
}
