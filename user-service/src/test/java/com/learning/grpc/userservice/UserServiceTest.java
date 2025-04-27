package com.learning.grpc.userservice;

import com.learning.common.Ticker;
import com.learning.user.*;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "grpc.server.port=-1",
        "grpc.server.in-process-name=integration-test",
        "grpc.client.user-service.address=in-process:integration-test"
})
public class UserServiceTest {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub stub;

    @Test
    public void userInformationTest() {
        UserInformationRequest request =
                UserInformationRequest.newBuilder()
                        .setUserId(1)
                .build();

        UserInformation userInformation = this.stub.getUserInformation(request);

        Assertions.assertEquals(10000, userInformation.getBalance());
        Assertions.assertEquals("Sam", userInformation.getName());
        Assertions.assertEquals(1, userInformation.getUserId());
        Assertions.assertTrue(userInformation.getHoldingsList().isEmpty());
    }

    @Test
    public void unknownUserExceptionTest() {
        StatusRuntimeException e =  Assertions.assertThrows(StatusRuntimeException.class, () -> {
            UserInformationRequest request =
                    UserInformationRequest.newBuilder()
                            .setUserId(-1)
                            .build();

            this.stub.getUserInformation(request);
        });

        Assertions.assertEquals(Status.Code.NOT_FOUND, e.getStatus().getCode());
    }

    @Test
    public void unknownTickerBuyTest() {
        StatusRuntimeException e =  Assertions.assertThrows(StatusRuntimeException.class, () -> {
            StockTradeRequest request =
                    StockTradeRequest.newBuilder()
                            .setUserId(1)
                            .setAction(TradeAction.BUY)
                            .setTicker(Ticker.UNKNOWN)
                            .build();


            this.stub.tradeStock(request);
        });

        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, e.getStatus().getCode());
    }

    @Test
    public void insufficientSharesTest() {
        StatusRuntimeException e =  Assertions.assertThrows(StatusRuntimeException.class, () -> {
            StockTradeRequest request =
                    StockTradeRequest.newBuilder()
                            .setUserId(1)
                            .setAction(TradeAction.SELL)
                            .setQuantity(5)
                            .setTicker(Ticker.APPLE)
                            .build();


            this.stub.tradeStock(request);
        });

        Assertions.assertEquals("User [id=1] does not have enough shares to complete the transaction", e.getStatus().getDescription());
    }

    @Test
    public void insufficientBalanceTest() {
        StatusRuntimeException e =  Assertions.assertThrows(StatusRuntimeException.class, () -> {
            StockTradeRequest request =
                    StockTradeRequest.newBuilder()
                            .setUserId(1)
                            .setAction(TradeAction.BUY)
                            .setPrice(5000)
                            .setQuantity(500000000)
                            .setTicker(Ticker.APPLE)
                            .build();


            this.stub.tradeStock(request);
        });

        Assertions.assertEquals("User [id=1] does not have enough fund to complete the transaction", e.getStatus().getDescription());
    }

    @Test
    public void buySellTest() {
        //buy
        StockTradeRequest buyRequest =
                StockTradeRequest.newBuilder()
                        .setUserId(2)
                        .setAction(TradeAction.BUY)
                        .setPrice(100)
                        .setQuantity(5)
                        .setTicker(Ticker.APPLE)
                        .build();

        StockTradeResponse buyResponse = this.stub.tradeStock(buyRequest);

        //validate balance
        Assertions.assertEquals(9500, buyResponse.getBalance());

        //check holding
        UserInformationRequest informationRequest =
                UserInformationRequest.newBuilder()
                        .setUserId(2)
                        .build();
        UserInformation userInformation = this.stub.getUserInformation(informationRequest);
        Assertions.assertEquals(1, userInformation.getHoldingsCount());
        Assertions.assertEquals(Ticker.APPLE,
                userInformation.getHoldingsList().get(0).getTicker());

        //sell
        StockTradeRequest sellRequest =
                StockTradeRequest.newBuilder()
                        .setUserId(2)
                        .setAction(TradeAction.SELL)
                        .setPrice(102)
                        .setQuantity(5)
                        .setTicker(Ticker.APPLE)
                        .build();
        StockTradeResponse sellResponse = this.stub.tradeStock(sellRequest);

        //validate balance
        Assertions.assertEquals(10010, sellResponse.getBalance());
    }
}
