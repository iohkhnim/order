package com.khoi.order.dao.dao.impl;

import com.khoi.basecrud.dao.dao.impl.BaseDAOImpl;
import com.khoi.order.dao.IOrderItemDAO;
import com.khoi.order.dto.OrderItem;
import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class OrderItemDAOImpl extends BaseDAOImpl<OrderItem, Integer> implements IOrderItemDAO {

  /**
   * {@inheritDoc}
   */
  @Override
  public int calculateTotalPrice(int order_id) {
    String hql = "SELECT SUM(oi.price*oi.amount) AS total "
        + "FROM OrderItem oi WHERE oi.order_id = :order_id "
        + "GROUP BY oi.order_id";
    Query query = entityManager.createQuery(hql);
    query.setParameter("order_id", order_id);
    return Integer.parseInt(query.getResultList().get(0).toString());
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public List<OrderItem> getOrderItemsByOrderId(int order_id) {
    String hql = "FROM OrderItem o WHERE o.order_id = :order_id";
    Query query = entityManager.createQuery(hql);
    query.setParameter("order_id", order_id);
    return (List<OrderItem>) query.getResultList();
  }
}
