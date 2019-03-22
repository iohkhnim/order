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
   * <p>This method gets all orders has been placed by given customer ID</p>
   * @param customer_id customer ID need to be got its order information
   * @return All orders information belong to given customer
   */
  @Override
  public List<Order> getOrdersByCustomerId(int customer_id) {
    String hql = "FROM Order o WHERE o.customer_id = :cusid";
    Query query = entityManager.createQuery(hql);
    query.setParameter("cusid", customer_id);
    return (List<Order>) query.getResultList();
  }

  /**
   * <p>This method checks if given order belongs to given customer</p>
   * @param customer_id Customer ID gave this Order ID
   * @param order_id Order ID given by customer
   * @return Return A boolean value according to result
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
   * <p>This method returns order information of given Order ID</p>
   * @param order_id Order ID need to be retrieved its information
   * @return Retunr information of given Order ID
   */
  @Override
  public Order getOrderByOrderId(int order_id) {
    String hql = "FROM Order o WHERE o.id = :id";
    Query query = entityManager.createQuery(hql);
    query.setParameter("id", order_id);
    return (Order) query.getSingleResult();
  }
}
