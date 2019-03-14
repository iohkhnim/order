package com.khoi.order.dao.dao.impl;

import com.khoi.basecrud.dao.dao.impl.BaseDAOImpl;
import com.khoi.order.dao.IOrderDAO;
import com.khoi.order.dto.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class OderDAOImpl extends BaseDAOImpl<Order, Integer> implements IOrderDAO {

}
