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

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Order> getOrdersByCustomerId(int customer_id) {
    String hql = "FROM Order o WHERE o.customer_id = :cusid";
    Query query = entityManager.createQuery(hql);
    query.setParameter("cusid", customer_id);
    return (List<Order>) query.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean authenticateOrderOwner(int customer_id, int order_id) {
    String hql = "FROM Order o WHERE o.customer_id = :cusid AND o.id = :id";
    Query query = entityManager.createQuery(hql);
    query.setParameter("cusid", customer_id);
    query.setParameter("id", order_id);
    if (query.getResultList().isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Order getOrderByOrderId(int order_id) {
    String hql = "FROM Order o WHERE o.id = :id";
    Query query = entityManager.createQuery(hql);
    query.setParameter("id", order_id);
    return (Order) query.getSingleResult();
  }
}
