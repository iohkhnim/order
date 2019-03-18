package com.khoi.order.service.service.service.impl;

import com.khoi.order.dao.IOrderDAO;
import com.khoi.order.dao.IOrderItemDAO;
import com.khoi.order.dto.Order;
import com.khoi.order.dto.OrderItem;
import com.khoi.order.service.IOrderItemService;
import com.khoi.orderproto.*;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@GRpcService
public class OderServiceGrpcImpl extends OrderServiceGrpc.OrderServiceImplBase {

  @Autowired IOrderDAO orderDAO;

  @Autowired IOrderItemDAO orderItemDAO;

  @Autowired IOrderItemService orderItemService;

  @Override
  public void createOrder(
      CreateOrderRequest request, StreamObserver<CreateOrderResponse> responseObserver) {
    Order order = new Order();
    order.setCustomer_id(request.getCustomerId());
    // generate unique number
    order.setOrder_number(new Date().getTime());

    List<CheckoutDataProto> checkoutDataProtos = request.getCheckoutDataProtoList();

    // create order
    if (orderDAO.create(order)) { // create order
      // create order_item
      checkoutDataProtos.stream()
          .forEach(
              entry -> {
                if(createOrderItem(entry, order.getId())) return;
              });

      // everything is good, send response
      responseObserver.onNext(CreateOrderResponse.newBuilder().setOrderId(order.getId()).build());
      responseObserver.onCompleted();
    } else {
      responseObserver.onNext(CreateOrderResponse.newBuilder().setOrderId(-1).build());
      responseObserver.onCompleted();
    }
  }

  @Override
  public void createOrderItem(
      CreateOrderItemRequest request, StreamObserver<CreateOrderItemResponse> responseOberserver) {
    OrderItem orderItem = new OrderItem();
    orderItem.setOrder_id(request.getOrderId());
    orderItem.setProduct_id(request.getProductId());
    orderItem.setAmount(request.getAmount());
    orderItem.setStock_id(request.getStockId());
    orderItem.setPrice(request.getPrice());
    orderItemDAO.create(orderItem);
    responseOberserver.onNext(
        CreateOrderItemResponse.newBuilder().setOrderItemId(orderItem.getId()).build());
    responseOberserver.onCompleted();
  }

  public Boolean createOrderItem(CheckoutDataProto checkoutDataProto, int order_id) {
    return orderItemService.create(checkoutDataProto, order_id);
  }
}
