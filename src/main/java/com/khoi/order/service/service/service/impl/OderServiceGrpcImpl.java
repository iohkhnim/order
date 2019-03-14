package com.khoi.order.service.service.service.impl;

import com.khoi.order.dao.IOrderDAO;
import com.khoi.order.dao.IOrderItemDAO;
import com.khoi.order.dto.Order;
import com.khoi.order.dto.OrderItem;
import com.khoi.orderproto.CreateOrderItemRequest;
import com.khoi.orderproto.CreateOrderItemResponse;
import com.khoi.orderproto.CreateOrderRequest;
import com.khoi.orderproto.CreateOrderResponse;
import com.khoi.orderproto.OrderServiceGrpc;
import io.grpc.stub.StreamObserver;
import java.util.Date;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService
public class OderServiceGrpcImpl extends OrderServiceGrpc.OrderServiceImplBase {

  @Autowired
  IOrderDAO orderDAO;

  @Autowired
  IOrderItemDAO orderItemDAO;

  @Override
  public void createOrder(CreateOrderRequest request,
      StreamObserver<CreateOrderResponse> responseObserver) {
    Order order = new Order();
    order.setCustomer_id(request.getCustomerId());
    order.setOrder_number(new Date().getTime());
    orderDAO.create(order);
    responseObserver.onNext(CreateOrderResponse.newBuilder().setOrderId(order.getId()).build());
    responseObserver.onCompleted();
  }

  @Override
  public void createOrderItem(CreateOrderItemRequest request,
      StreamObserver<CreateOrderItemResponse> responseOberserver) {
    OrderItem orderItem = new OrderItem();
    orderItem.setOrder_id(request.getOrderId());
    orderItem.setProduct_id(request.getProductId());
    orderItem.setAmount(request.getAmount());
    orderItem.setStock_id(request.getStockId());
    orderItem.setPrice(request.getPrice());
    orderItemDAO.create(orderItem);
    responseOberserver
        .onNext(CreateOrderItemResponse.newBuilder().setOrderItemId(orderItem.getId()).build());
    responseOberserver.onCompleted();
  }

}
