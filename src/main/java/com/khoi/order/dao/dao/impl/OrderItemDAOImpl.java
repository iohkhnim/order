package com.khoi.order.dao.dao.impl;

import com.khoi.basecrud.dao.dao.impl.BaseDAOImpl;
import com.khoi.order.dao.IOrderItemDAO;
import com.khoi.order.dto.OrderItem;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class OrderItemDAOImpl extends BaseDAOImpl<OrderItem, Integer> implements IOrderItemDAO {

}
