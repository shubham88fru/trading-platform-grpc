package com.learning.grpc.userservice.service;

import com.learning.grpc.userservice.service.handler.StockTradeRequestHandler;
import com.learning.grpc.userservice.service.handler.UserInformationRequestHandler;
import com.learning.user.*;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@AllArgsConstructor
@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private final UserInformationRequestHandler userInformationRequestHandler;
    private final StockTradeRequestHandler stockTradeRequestHandler;

    @Override
    public void getUserInformation(UserInformationRequest request, StreamObserver<UserInformation> responseObserver) {
        UserInformation userInformation =
                this.userInformationRequestHandler.getUserInformation(request);

        responseObserver.onNext(userInformation);
        responseObserver.onCompleted();
    }

    @Override
    public void tradeStock(StockTradeRequest request,
                           StreamObserver<StockTradeResponse> responseObserver) {
        StockTradeResponse response
                = TradeAction.SELL.equals(request.getAction()) ?
                stockTradeRequestHandler.sellStock(request) :
                stockTradeRequestHandler.buyStock(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
