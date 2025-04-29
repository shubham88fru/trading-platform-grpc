package com.learning.grpc.aggregatorservice.service;

import com.learning.stock.StockPriceRequest;
import com.learning.stock.StockPriceResponse;
import com.learning.stock.StockServiceGrpc;
import com.learning.user.StockTradeRequest;
import com.learning.user.StockTradeResponse;
import com.learning.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userClient;

    @GrpcClient("stock-service")
    private StockServiceGrpc.StockServiceBlockingStub stockClient;

    public StockTradeResponse trade(StockTradeRequest request) {
        StockPriceRequest priceRequest =
                StockPriceRequest.newBuilder().setTicker(request.getTicker()).build();

        StockPriceResponse priceResponse = stockClient.getStockPrice(priceRequest);
        StockTradeRequest tradeRequest = request.toBuilder()
                .setPrice(priceResponse.getPrice()).build();

        return userClient.tradeStock(tradeRequest);
    }
}
