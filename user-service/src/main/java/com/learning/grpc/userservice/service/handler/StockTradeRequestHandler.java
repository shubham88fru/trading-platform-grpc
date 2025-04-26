package com.learning.grpc.userservice.service.handler;

import com.learning.common.Ticker;
import com.learning.grpc.userservice.entity.PortfolioItem;
import com.learning.grpc.userservice.entity.User;
import com.learning.grpc.userservice.exception.InsufficientBalanceException;
import com.learning.grpc.userservice.exception.InsufficientSharesException;
import com.learning.grpc.userservice.exception.UnknownTickerException;
import com.learning.grpc.userservice.exception.UnknownUserException;
import com.learning.grpc.userservice.repository.PortfolioItemRepository;
import com.learning.grpc.userservice.repository.UserRepository;
import com.learning.grpc.userservice.util.EntityMessageMapper;
import com.learning.user.StockTradeRequest;
import com.learning.user.StockTradeResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class StockTradeRequestHandler {

    private final UserRepository userRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    @Transactional
    public StockTradeResponse buyStock(StockTradeRequest stockTradeRequest) {
        //validate
        this.validateTicker(stockTradeRequest.getTicker());
        User user = this.userRepository
                .findById(stockTradeRequest.getUserId())
                .orElseThrow(() -> new UnknownUserException(stockTradeRequest.getUserId()));

        int totalPrice = stockTradeRequest.getQuantity() * stockTradeRequest.getPrice();
        this.validateUserBalance(user.getId(), user.getBalance(), totalPrice);

        //valid request here.
        user.setBalance(user.getBalance() - totalPrice);

        this.portfolioItemRepository
                .findByUserIdAndTicker(user.getId(), stockTradeRequest.getTicker())
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + stockTradeRequest.getQuantity()),
                        () -> portfolioItemRepository.save(EntityMessageMapper.toPortfolioItem(stockTradeRequest))
                );

        return EntityMessageMapper.toStockTradeResponse(stockTradeRequest, user.getBalance());
    }

    @Transactional
    public StockTradeResponse sellStock(StockTradeRequest stockTradeRequest) {
        //validate
        this.validateTicker(stockTradeRequest.getTicker());
        User user = this.userRepository
                .findById(stockTradeRequest.getUserId())
                .orElseThrow(() -> new UnknownUserException(stockTradeRequest.getUserId()));
        PortfolioItem portfolioItem = this.portfolioItemRepository
                .findByUserIdAndTicker(user.getId(), stockTradeRequest.getTicker())
                .filter(pi -> pi.getQuantity() >= stockTradeRequest.getQuantity())
                .orElseThrow(() -> new InsufficientSharesException(user.getId()));

        int totalPrice = stockTradeRequest.getQuantity() * stockTradeRequest.getPrice();

        //valid request here.
        user.setBalance(user.getBalance() + totalPrice);
        portfolioItem.setQuantity(portfolioItem.getQuantity() - stockTradeRequest.getQuantity());

        return EntityMessageMapper.toStockTradeResponse(stockTradeRequest, user.getBalance());
    }

    private void validateTicker(Ticker ticker) {
        if (Ticker.UNKNOWN.equals(ticker)) {
            throw new UnknownTickerException();
        }
    }

    private void validateUserBalance(int userId, int userBalance, int totalPrice) {
        if (totalPrice > userBalance) {
            throw new InsufficientBalanceException(userId);
        }
    }
}
