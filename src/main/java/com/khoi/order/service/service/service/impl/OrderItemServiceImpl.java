package com.khoi.order.service.service.service.impl;

import com.khoi.basecrud.service.service.impl.BaseServiceImpl;
import com.khoi.order.dao.IOrderItemDAO;
import com.khoi.order.dto.OrderItem;
import com.khoi.order.service.IOrderItemService;
import com.khoi.orderproto.CheckoutDataProto;
import com.khoi.productproto.GetProductNameByIdRequest;
import com.khoi.productproto.ProductServiceGrpc;
import com.khoi.proto.GetPriceRequest;
import com.khoi.proto.PriceServiceGrpc;
import com.khoi.stockproto.GetBestStockRequest;
import com.khoi.stockproto.GetSupplierIdByStockIdRequest;
import com.khoi.stockproto.StockServiceGrpc;
import com.khoi.stockproto.SubtractRequest;
import com.khoi.supplierproto.GetSupplierNameByIdRequest;
import com.khoi.supplierproto.SupplierServiceGrpc;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem, Integer>
    implements IOrderItemService {

  @Qualifier("priceService")
  private final PriceServiceGrpc.PriceServiceBlockingStub priceService;
  @Qualifier("stockService")
  private final StockServiceGrpc.StockServiceBlockingStub stockService;
  @Qualifier("supplierService")
  private final SupplierServiceGrpc.SupplierServiceBlockingStub supplierService;
  @Qualifier("productService")
  private final ProductServiceGrpc.ProductServiceBlockingStub productService;
  @Autowired
  IOrderItemDAO orderItemDAO;

  public OrderItemServiceImpl(
      PriceServiceGrpc.PriceServiceBlockingStub priceService,
      StockServiceGrpc.StockServiceBlockingStub stockService,
      SupplierServiceGrpc.SupplierServiceBlockingStub supplierService,
      ProductServiceGrpc.ProductServiceBlockingStub productService) {
    this.priceService = priceService;
    this.stockService = stockService;
    this.supplierService = supplierService;
    this.productService = productService;
  }

  /**
   * <p>This method gives stock ID of a product with the largest stock through stock gRPC server</p>
   * @param product_id Product ID that is bought
   * @param amount Amount that customer bought
   * @return stock ID fit with condition or -1 if amount is bigger than stock
   */
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

  /**
   * <p>This method return price of a product through price gRPC server</p>
   * @param product_id product ID need to retrieve its price
   * @return price of that product
   */
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

  /**
   * <p>This method subtracts stock from database through stock gRPC server</p>
   * @param stock_id Stock ID need to be subtracted
   * @param amount Amount subtract
   * @return a boolean value according to result
   */
  private Boolean subtract(int stock_id, int amount) {
    try {
      if (stockService
          .subtract(SubtractRequest.newBuilder().setStockId(stock_id).setAmount(amount).build())
          .getStockId()
          > 0) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      System.out.println(ex);
      return false;
    }
  }

  /**
   * <p>This methods creates order items belong to an order</p>
   * @param checkoutDataProto Contain order items information
   * @param order_id Order ID of all order items
   * @return Return a boolean value according to result
   */
  @Override
  public Boolean create(CheckoutDataProto checkoutDataProto, int order_id) {
    int product_id = checkoutDataProto.getProductId();
    int amount = checkoutDataProto.getAmount();
    int price = getPrice(product_id);
    int bestStockId = getBestStockId(product_id, amount);
    Boolean isSubtractCompleted = subtract(bestStockId, amount);
    if (price > 0 && bestStockId > 0 && isSubtractCompleted == true) {
      OrderItem orderItem = new OrderItem(order_id, product_id, bestStockId, amount, price);
      if (orderItemDAO.create(orderItem)) {
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  /**
   * <p>This method calculates total price of an order</p>
   * @param order_id Order ID need to be calculated
   * @return Return total price of provided order
   */
  @Override
  public int calculateTotalPrice(int order_id) {
    return orderItemDAO.calculateTotalPrice(order_id);
  }

  /**
   * <p>This method returns supplier ID of provided stock ID through supplier gRPC server</p>
   * @param stock_id stock ID need to be got supplier ID
   * @return supplier ID of provided stock ID
   */
  @Override
  public int getSupplierIdByStockId(int stock_id) {
    return stockService.getSupplierIdByStockId(
        GetSupplierIdByStockIdRequest.newBuilder().setStockId(stock_id).build()).getSupplierId();
  }

  /**
   * <p>This method return all order items of an order </p>
   * @param order_id Order ID that needs to be got order items
   * @return All order items information of provided order ID
   */
  @Override
  public List<OrderItem> getOrderItemsByOrderId(int order_id) {
    return orderItemDAO.getOrderItemsByOrderId(order_id);
  }

  /**
   * <p>This method gets supplier name of a given supplier ID through supplier gRPC server</p>
   * @param supplier_id supplier ID needs to be retrieved its name
   * @return supplier name of given supplier ID
   */
  @Override
  public String getSupplierNameById(int supplier_id) {
    return supplierService.getSupplierNameById(
        GetSupplierNameByIdRequest.newBuilder().setSupplierId(supplier_id).build())
        .getSupplierName();
  }

  /**
   * <p>This method gets product name of a given product ID through product gRPC server</p>
   * @param product_id product ID needs to be retrieved its name
   * @return product name of given product ID
   */
  @Override
  public String getProductNameById(int product_id) {
    return productService
        .getProductNameById(GetProductNameByIdRequest.newBuilder().setProductId(product_id).build())
        .getProductName();
  }
}
