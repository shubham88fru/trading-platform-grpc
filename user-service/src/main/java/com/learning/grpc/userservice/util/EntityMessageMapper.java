package com.learning.grpc.userservice.util;

import com.learning.grpc.userservice.entity.PortfolioItem;
import com.learning.grpc.userservice.entity.User;
import com.learning.user.Holding;
import com.learning.user.StockTradeRequest;
import com.learning.user.StockTradeResponse;
import com.learning.user.UserInformation;

import java.util.List;

public class EntityMessageMapper {

    public static UserInformation toUserInformation(User user, List<PortfolioItem> items) {
        List<Holding> holdings =  items.stream().map(i -> Holding
                .newBuilder()
                .setTicker(i.getTicker())
                .setQuantity(i.getQuantity())
                .build()
        ).toList();

        return UserInformation.newBuilder()
                .setUserId(user.getId())
                .setName(user.getName())
                .setBalance(user.getBalance())
                .addAllHoldings(holdings)
                .build();
    }

    public static PortfolioItem toPortfolioItem(StockTradeRequest request) {
        PortfolioItem item = new PortfolioItem();
        item.setUserId(request.getUserId());
        item.setTicker(request.getTicker());
        item.setQuantity(request.getQuantity());

        return item;
    }

    public static StockTradeResponse toStockTradeResponse(StockTradeRequest request, int balance) {
        return StockTradeResponse.newBuilder()
                .setUserId(request.getUserId())
                .setPrice(request.getPrice())
                .setTicker(request.getTicker())
                .setQuantity(request.getQuantity())
                .setAction(request.getAction())
                .setTotalPrice(request.getPrice() * request.getQuantity())
                .setBalance(balance)
                .build();
    }
}
