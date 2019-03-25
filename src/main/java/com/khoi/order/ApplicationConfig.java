package com.khoi.order;

import com.khoi.productproto.ProductServiceGrpc;
import com.khoi.proto.PriceServiceGrpc;
import com.khoi.stockproto.StockServiceGrpc;
import com.khoi.supplierproto.SupplierServiceGrpc;
import io.grpc.Channel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class ApplicationConfig {
  private final String priceServiceEndpoint = "localhost:6565";
  private final String stockServiceEndpoint = "localhost:6570";
  private final String supplierServiceEndpoint = "localhost:6580";
  private final String productServiceEndpoint = "localhost:6575";

  private final String priceServerKeyPath = "src/main/java/com/khoi/order/key/ca.crt";
  private final String stockServerKeyPath = "src/main/java/com/khoi/order/key/ca.crt";
  private final String supplierServerKeyPath = "src/main/java/com/khoi/order/key/ca.crt";
  private final String productServerKeyPath = "src/main/java/com/khoi/order/key/ca.crt";

  @Bean(name = "priceChannel")
  Channel priceChannel() throws Exception {
    return NettyChannelBuilder.forTarget(priceServiceEndpoint)
        .negotiationType(NegotiationType.TLS)
        .sslContext(GrpcSslContexts.forClient().trustManager(new File(priceServerKeyPath)).build())
        .build();
  }

  @Bean(name = "stockChannel")
  Channel stockChannel() throws Exception {
    return NettyChannelBuilder.forTarget(stockServiceEndpoint)
        .negotiationType(NegotiationType.TLS)
        .sslContext(GrpcSslContexts.forClient().trustManager(new File(stockServerKeyPath)).build())
        .build();
  }

  @Bean(name = "supplierChannel")
  Channel supplierChannel() throws Exception {
    return NettyChannelBuilder.forTarget(supplierServiceEndpoint)
        .negotiationType(NegotiationType.TLS)
        .sslContext(
            GrpcSslContexts.forClient().trustManager(new File(supplierServerKeyPath)).build())
        .build();
  }

  @Bean(name = "productChannel")
  Channel productChannel() throws Exception {
    return NettyChannelBuilder.forTarget(productServiceEndpoint)
        .negotiationType(NegotiationType.TLS)
        .sslContext(
            GrpcSslContexts.forClient().trustManager(new File(productServerKeyPath)).build())
        .build();
  }

  @Bean(name = "priceService")
  @Qualifier("priceChannel")
  PriceServiceGrpc.PriceServiceBlockingStub priceService(Channel priceChannel) {
    return PriceServiceGrpc.newBlockingStub(priceChannel);
  }

  @Bean(name = "stockService")
  @Qualifier("stockChannel")
  StockServiceGrpc.StockServiceBlockingStub stockService(Channel stockChannel) {
    return StockServiceGrpc.newBlockingStub(stockChannel);
  }

  @Bean(name = "supplierService")
  @Qualifier("supplierChannel")
  SupplierServiceGrpc.SupplierServiceBlockingStub supplierService(Channel supplierChannel) {
    return SupplierServiceGrpc.newBlockingStub(supplierChannel);
  }

  @Bean(name = "productService")
  @Qualifier("productChannel")
  ProductServiceGrpc.ProductServiceBlockingStub productService(Channel productChannel) {
    return ProductServiceGrpc.newBlockingStub(productChannel);
  }
}
