package com.learning.grpc.aggregatorservice.controller;

import com.google.common.util.concurrent.Uninterruptibles;
import com.learning.grpc.aggregatorservice.service.PriceUpdateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("stock")
public class StockController {

    @Autowired
    private PriceUpdateListener listener;

    @GetMapping(value = "updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter priceUpdates() {
        return listener.createEmitter();
    }
}
