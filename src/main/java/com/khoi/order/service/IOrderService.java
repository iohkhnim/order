package com.khoi.order.service;

import com.khoi.basecrud.service.IBaseService;
import com.khoi.order.dto.Order;

public interface IOrderService extends IBaseService<Order, Integer> {
    int updateOrderStatus(int id, int status);
}
