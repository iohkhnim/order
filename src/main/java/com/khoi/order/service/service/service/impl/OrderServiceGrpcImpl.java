package com.khoi.order.service.service.service.impl;

import com.google.rpc.Code;
import com.google.rpc.Status;
import com.khoi.order.dao.IOrderDAO;
import com.khoi.order.dao.IOrderItemDAO;
import com.khoi.order.dto.Order;
import com.khoi.order.service.IOrderItemService;
import com.khoi.orderproto.*;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@GRpcService
public class OrderServiceGrpcImpl extends OrderServiceGrpc.OrderServiceImplBase {

  @Autowired IOrderDAO orderDAO;

  @Autowired IOrderItemDAO orderItemDAO;

  @Autowired IOrderItemService orderItemService;

  /**
   * This method receives order information from gRPC client and create an order in database with
   * given information
   *
   * @param request Contain all needed information for creating and order
   * @param responseObserver Return created order ID or -1 if it's failed to create new order
   */
  @Override
  public void createOrder(
      CreateOrderRequest request, StreamObserver<CreateOrderResponse> responseObserver) {
    Order order = new Order();
    order.setCustomer_id(request.getCustomerId());
    // generate unique number
    order.setOrder_number(new Date().getTime());

    order.setAddress(request.getAddress());

    List<CheckoutDataProto> checkoutDataProtos = request.getCheckoutDataProtoList();

    // create order
    if (orderDAO.create(order) > 0) { // create order
      // create order_item
      checkoutDataProtos.stream()
          .forEach(
              entry -> {
                if (createOrderItem(entry, order.getId()) > 0) {
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

  /**
   * This method return all orders information of a customer through OrderProto
   *
   * @param request Customer ID
   * @param responseObserver Return all orders information
   */
  @Override
  public void getOrders(
      GetOrdersRequest request, StreamObserver<GetOrdersResponse> responseObserver) {
    try {
      List<Order> orderList = orderDAO.getOrdersByCustomerId(request.getCustomerId());
      orderList.stream()
          .forEach(
              p ->
                  responseObserver.onNext(
                      p.toProto(orderItemService.calculateTotalPrice(p.getId()))));
      responseObserver.onCompleted();
    } catch (Exception ex) {
      Status status =
          Status.newBuilder().setCode(Code.NOT_FOUND_VALUE).setMessage("No such item").build();
      responseObserver.onError(StatusProto.toStatusRuntimeException(status));
    }
  }

  /**
   * This method return an order information and all order items belong to it of a customer.
   *
   * <p>A customer cannot retrieve another customer order information
   *
   * @param request Customer ID and Order ID
   * @param responseObserver Return an order information and all of its order items information.
   *     Return error code PERMISSION_DENIED if a customer try to retrieve other customer's order
   */
  @Override
  public void trackingOrderDetails(
      TrackingOrderDetailsRequest request,
      StreamObserver<TrackingOrderDetailsResponse> responseObserver) {
      String role = request.getRole();
    if (orderDAO.authenticateOrderOwner(request.getCustomerId(), request.getOrderId())
        || request.getRole().equals("[0]")
        || request.getRole().equals("[1]")) {
      Order order = orderDAO.getOrderByOrderId(request.getOrderId());
      GetOrdersResponse getOrdersResponse =
          order.toProto(orderItemService.calculateTotalPrice(request.getOrderId()));

      List<OrderItem> orderItemsProto = new ArrayList<>();
      List<com.khoi.order.dto.OrderItem> orderItemDTO =
          orderItemService.getOrderItemsByOrderId(request.getOrderId());

      for (com.khoi.order.dto.OrderItem e : orderItemDTO) {
        // get supplier name
        int supplier_id = orderItemService.getSupplierIdByStockId(e.getStock_id());
        String supplier_name = orderItemService.getSupplierNameById(supplier_id);
        // System.out.println(supplier_id);
      }

      orderItemDTO.forEach(
          e -> {
            // get supplier name
            int supplier_id = orderItemService.getSupplierIdByStockId(e.getStock_id());
            String supplier_name = orderItemService.getSupplierNameById(supplier_id);

            // get product name
            String product_name = orderItemService.getProductNameById(e.getProduct_id());
            orderItemsProto.add(e.toProto(supplier_name, product_name));
          });

      responseObserver.onNext(
          TrackingOrderDetailsResponse.newBuilder()
              .setOrder(getOrdersResponse)
              .addAllOrderItem(orderItemsProto)
              .build());
      responseObserver.onCompleted();

    } else {

      Status status =
          Status.newBuilder()
              .setCode(Code.PERMISSION_DENIED_VALUE)
              .setMessage("This is not your order")
              .build();
      responseObserver.onError(StatusProto.toStatusRuntimeException(status));
    }
  }

  /**
   * Pass data to orderItem service to create order items
   *
   * @param checkoutDataProto order items information
   * @param order_id order ID
   * @return A boolean value according to the result
   */
  private int createOrderItem(CheckoutDataProto checkoutDataProto, int order_id) {
    return orderItemService.create(checkoutDataProto, order_id);
  }
}
