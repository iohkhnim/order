package com.khoi.order.dto;

import com.khoi.basecrud.dto.baseDTO;
import com.khoi.orderproto.GetOrdersResponse;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "my_order")
public class Order extends baseDTO implements Serializable {

  @Column(name = "customer_id")
  private int customer_id;

  @Column(name = "order_number")
  private long order_number;
  @Column(name = "status")
  private int status;
  @Column(name = "address")
  private String address;

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * This method convert date from Date type to String type
   *
   * @param date Date in Date type
   * @return Date in String type
   */
  private String convertDate2String(Date date) {
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    return dateFormat.format(date);
  }

  public int getCustomer_id() {
    return customer_id;
  }

  public void setCustomer_id(int customer_id) {
    this.customer_id = customer_id;
  }

  public long getOrder_number() {
    return order_number;
  }

  public void setOrder_number(long order_number) {
    this.order_number = order_number;
  }

  /**
   * This method builds GetOrdersResponse type with given information in Order type
   *
   * @param total_price total Price of current Order
   * @return GetOrdersResponse object
   */
  public GetOrdersResponse toProto(int total_price) {
    return GetOrdersResponse.newBuilder()
        .setOrderId(getId())
        .setOrderNumber(getOrder_number())
        .setCreatedAt(convertDate2String(getCreatedTime()))
        .setTotalPrice(total_price)
        .setAddress(getAddress())
        .setStatus(getStatus())
        .build();
  }
}
