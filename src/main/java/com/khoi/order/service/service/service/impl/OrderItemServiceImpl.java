package com.khoi.order.service.service.service.impl;

import com.khoi.basecrud.service.service.impl.BaseServiceImpl;
import com.khoi.order.dto.OrderItem;
import com.khoi.order.service.IOrderItemService;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem, Integer> implements
    IOrderItemService {

}
