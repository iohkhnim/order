package com.khoi.order.service.service.service.impl;

import com.google.rpc.Code;
import com.google.rpc.Status;
import com.khoi.order.dao.IOrderDAO;
import com.khoi.order.dao.IOrderItemDAO;
import com.khoi.order.dto.Order;
import com.khoi.order.service.IOrderItemService;
import com.khoi.orderproto.CheckoutDataProto;
import com.khoi.orderproto.CreateOrderRequest;
import com.khoi.orderproto.CreateOrderResponse;
import com.khoi.orderproto.GetOrdersRequest;
import com.khoi.orderproto.GetOrdersResponse;
import com.khoi.orderproto.OrderItem;
import com.khoi.orderproto.OrderServiceGrpc;
import com.khoi.orderproto.TrackingOrderDetailsRequest;
import com.khoi.orderproto.TrackingOrderDetailsResponse;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService
public class OrderServiceGrpcImpl extends OrderServiceGrpc.OrderServiceImplBase {

  @Autowired
  IOrderDAO orderDAO;

  @Autowired
  IOrderItemDAO orderItemDAO;

  @Autowired
  IOrderItemService orderItemService;

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
                if (createOrderItem(entry, order.getId())) {
                  return;
                }
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
  public void getOrders(GetOrdersRequest request,
      StreamObserver<GetOrdersResponse> responseStreamObserver) {
    List<Order> orderList = orderDAO.getOrdersByCustomerId(request.getCustomerId());
    orderList.stream().forEach(p -> responseStreamObserver
        .onNext(p.toProto(orderItemService.calculateTotalPrice(p.getId()))));
    responseStreamObserver.onCompleted();
  }

  @Override
  public void trackingOrderDetails(TrackingOrderDetailsRequest request,
      StreamObserver<TrackingOrderDetailsResponse> streamObserver) {

    if (orderDAO.authenticateOrderOwner(request.getCustomerId(), request.getOrderId())) {
      Order order = orderDAO.getOrderByCustomerId(request.getOrderId());
      GetOrdersResponse getOrdersResponse = order
          .toProto(orderItemService.calculateTotalPrice(request.getOrderId()));

      List<OrderItem> orderItemsProto = new ArrayList<>();
      List<com.khoi.order.dto.OrderItem> orderItemDTO = orderItemService
          .getOrderItemsByOrderId(request.getOrderId());

      for (com.khoi.order.dto.OrderItem e : orderItemDTO) {
        //get supplier name
        int supplier_id = orderItemService.getSupplierIdByStockId(e.getStock_id());
        String supplier_name = orderItemService.getSupplierNameById(supplier_id);
        //System.out.println(supplier_id);
      }

      orderItemDTO.forEach(e -> {
        //get supplier name
        int supplier_id = orderItemService.getSupplierIdByStockId(e.getStock_id());
        String supplier_name = orderItemService.getSupplierNameById(supplier_id);

        //get product name
        String product_name = orderItemService.getProductNameById(e.getProduct_id());
        orderItemsProto.add(e.toProto(supplier_name, product_name));
      });

      streamObserver.onNext(TrackingOrderDetailsResponse.newBuilder().setOrder(getOrdersResponse)
          .addAllOrderItem(orderItemsProto).build());
      streamObserver.onCompleted();

    } else {

      Status status = Status.newBuilder().setCode(Code.PERMISSION_DENIED_VALUE)
          .setMessage("This is not your order").build();
      streamObserver.onError(StatusProto.toStatusRuntimeException(status));
    }
  }

  /*@Override
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
  }*/

  private Boolean createOrderItem(CheckoutDataProto checkoutDataProto, int order_id) {
    return orderItemService.create(checkoutDataProto, order_id);
  }
}
