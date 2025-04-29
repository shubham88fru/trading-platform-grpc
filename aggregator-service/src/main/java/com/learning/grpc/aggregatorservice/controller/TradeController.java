package com.learning.grpc.aggregatorservice.controller;

import com.learning.grpc.aggregatorservice.service.TradeService;
import com.learning.user.StockTradeRequest;
import com.learning.user.StockTradeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public StockTradeResponse trade(@RequestBody StockTradeRequest request) {
        return tradeService.trade(request);
    }
}
