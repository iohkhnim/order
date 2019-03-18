package com.khoi.order.service.service.service.impl;

import com.khoi.basecrud.service.service.impl.BaseServiceImpl;
import com.khoi.order.dto.OrderItem;
import com.khoi.order.service.IOrderItemService;
import com.khoi.orderproto.CheckoutDataProto;
import com.khoi.proto.GetPriceRequest;
import com.khoi.proto.PriceServiceGrpc;
import com.khoi.stockproto.GetBestStockRequest;
import com.khoi.stockproto.StockServiceGrpc;
import com.khoi.stockproto.SubtractRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem, Integer>
    implements IOrderItemService {

  @Qualifier("priceService")
  private final PriceServiceGrpc.PriceServiceBlockingStub priceService;

  @Qualifier("stockService")
  private final StockServiceGrpc.StockServiceBlockingStub stockService;

  public OrderItemServiceImpl(
      PriceServiceGrpc.PriceServiceBlockingStub priceService,
      StockServiceGrpc.StockServiceBlockingStub stockService) {
    this.priceService = priceService;
    this.stockService = stockService;
  }

  private int getBestStockId(int product_id, int amount) {
    try {
      return stockService
          .getBestStock(
              GetBestStockRequest.newBuilder().setProductId(product_id).setAmount(amount).build())
          .getStockId();
    } catch (Exception ex) {
      System.out.println(ex);
      return -1;
    }
  }

  private int getPrice(int product_id) {
    try {
      return priceService
          .getPrice(GetPriceRequest.newBuilder().setProductId(product_id).build())
          .getPrice();
    } catch (Exception ex) {
      System.out.println(ex);
      return -1;
    }
  }

  private Boolean subtract(int stock_id, int amount) {
    try {
      if (stockService
              .subtract(SubtractRequest.newBuilder().setStockId(stock_id).setAmount(amount).build())
              .getStockId()
          > 0) return true;
      else {
        return false;
      }
    } catch (Exception ex) {
      System.out.println(ex);
      return false;
    }
  }

  @Override
  public Boolean create(CheckoutDataProto checkoutDataProto, int order_id) {
    int product_id = checkoutDataProto.getProductId();
    int amount = checkoutDataProto.getAmount();
    int price = getPrice(product_id);
    int bestStockId = getBestStockId(product_id, amount);
    Boolean isSubtractCompleted = subtract(bestStockId, amount);
    if (price > 0 && bestStockId > 0 && isSubtractCompleted == true) {
      OrderItem orderItem = new OrderItem(order_id, product_id, bestStockId, amount, price);
      if (create(orderItem)) {
          return true;
      } else {
          return false;
      }
    }
    return false;
  }
}
