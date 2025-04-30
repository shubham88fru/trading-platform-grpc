package com.learning.grpc.aggregatorservice.service;

import com.learning.grpc.aggregatorservice.dto.PriceUpdateDto;
import com.learning.stock.PriceUpdate;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class PriceUpdateListener implements StreamObserver<PriceUpdate> {

    private static final Logger log = LoggerFactory.getLogger(PriceUpdateListener.class);
    private final Set<SseEmitter> emitters = Collections.synchronizedSet(new HashSet<>());
    private final long sseTimeout;

    public PriceUpdateListener(@Value("${sse.timeout:300000}") long sseTimeout) {
        this.sseTimeout = sseTimeout;
    }

    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(sseTimeout);
        emitters.add(emitter);
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(ex -> emitters.remove(emitter));
        return emitter;
    }

    @Override
    public void onNext(PriceUpdate priceUpdate) {
        PriceUpdateDto dto = new PriceUpdateDto(
                priceUpdate.getTicker().toString(),
                priceUpdate.getPrice()
        );

        emitters.removeIf(e -> !send(e, dto));
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Streaming error", throwable);
        emitters.forEach(e -> e.completeWithError(throwable));
        emitters.clear();
    }

    @Override
    public void onCompleted() {
        emitters.forEach(ResponseBodyEmitter::complete);
        emitters.clear();
    }

    private boolean send(SseEmitter emitter, Object o) {
        try {
            emitter.send(o);
            return true;
        } catch (IOException e) {
            log.warn("SSE error {}", e.getMessage());
            return false;
        }
    }
}
