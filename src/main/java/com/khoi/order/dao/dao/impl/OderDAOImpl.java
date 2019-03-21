package com.khoi.order.dao.dao.impl;

import com.khoi.basecrud.dao.dao.impl.BaseDAOImpl;
import com.khoi.order.dao.IOrderDAO;
import com.khoi.order.dto.Order;
import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class OderDAOImpl extends BaseDAOImpl<Order, Integer> implements IOrderDAO {

  public List<Order> getOrdersByCustomerId(int customer_id) {
    String hql = "FROM Order o WHERE o.customer_id = :cusid";
    Query query = entityManager.createQuery(hql);
    query.setParameter("cusid", customer_id);
    return (List<Order>) query.getResultList();
  }
}
