package com.learning.grpc.aggregatorservice.mockservice;

import com.google.protobuf.Empty;
import com.learning.common.Ticker;
import com.learning.stock.PriceUpdate;
import com.learning.stock.StockPriceRequest;
import com.learning.stock.StockPriceResponse;
import com.learning.stock.StockServiceGrpc;
import io.grpc.stub.StreamObserver;

public class StockMockService extends StockServiceGrpc.StockServiceImplBase {

    @Override
    public void getStockPrice(StockPriceRequest request, StreamObserver<StockPriceResponse> responseObserver) {
        StockPriceResponse stockPriceResponse = StockPriceResponse.newBuilder()
                .setPrice(15)
                .build();

        responseObserver.onNext(stockPriceResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getPriceUpdates(Empty request, StreamObserver<PriceUpdate> responseObserver) {
        for (int i=1; i<=5; i++) {
            PriceUpdate priceUpdate = PriceUpdate.newBuilder()
                    .setPrice(i).setTicker(Ticker.AMAZON)
                    .build();
            responseObserver.onNext(priceUpdate);
        }

        responseObserver.onCompleted();
    }
}
