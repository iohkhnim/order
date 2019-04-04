package com.khoi.order.config;

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

  private final String productServiceEndpoint = "172.17.0.2:6565";
  private final String priceServiceEndpoint = "172.17.0.3:6565";
  private final String stockServiceEndpoint = "172.17.0.4:6565";
  private final String supplierServiceEndpoint = "172.17.0.5:6565";

  private final String productServerKeyPath = "key/product.crt";
  private final String priceServerKeyPath = "key/price.crt";
  private final String stockServerKeyPath = "key/stock.crt";
  private final String supplierServerKeyPath = "key/supplier.crt";

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
