package com.khoi.order.dto;

import com.khoi.basecrud.dto.baseDTO;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "my_order")
public class Order extends baseDTO implements Serializable {

  @Column(name = "customer_id")
  private
  int customer_id;

  @Column(name = "order_number")
  private
  int order_number;

  public int getCustomer_id() {
    return customer_id;
  }

  public void setCustomer_id(int customer_id) {
    this.customer_id = customer_id;
  }

  public int getOrder_number() {
    return order_number;
  }

  public void setOrder_number(int order_number) {
    this.order_number = order_number;
  }
}
