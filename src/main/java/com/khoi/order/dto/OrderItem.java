package com.khoi.order.dto;

import com.khoi.basecrud.dto.baseDTO;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "order_item")
public class OrderItem extends baseDTO implements Serializable {

  @Column(name = "order_id")
  private int order_id;

  @Column(name = "product_id")
  private int product_id;

  @Column(name = "stock_id")
  private int stock_id;

  @Column(name = "amount")
  private int amount;

  @Column(name = "price")
  private int price;

  public OrderItem() {
  }

  public OrderItem(int order_id, int product_id, int stock_id, int amount, int price) {
    this.order_id = order_id;
    this.product_id = product_id;
    this.stock_id = stock_id;
    this.amount = amount;
    this.price = price;
  }

  /**
   * <p>This method builds proto OrderItem type with given information in OrderItem type</p>
   * @param supplier_name supplier name of product in this order item
   * @param product_name name of the product in this order item
   * @return proto OrderItem object
   */
  public com.khoi.orderproto.OrderItem toProto(String supplier_name,
      String product_name) {
      return com.khoi.orderproto.OrderItem.newBuilder().setAmount(getAmount()).setPrice(getPrice())
        .setTotalOrderItem(getAmount() * getPrice()).setProductName(product_name)
        .setSupplierName(supplier_name).build();
  }

  public int getOrder_id() {
    return order_id;
  }

  public void setOrder_id(int order_id) {
    this.order_id = order_id;
  }

  public int getProduct_id() {
    return product_id;
  }

  public void setProduct_id(int product_id) {
    this.product_id = product_id;
  }

  public int getStock_id() {
    return stock_id;
  }

  public void setStock_id(int stock_id) {
    this.stock_id = stock_id;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }
}
