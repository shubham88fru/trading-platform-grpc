package com.learning.grpc.aggregatorservice.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PriceUpdateDto {
    private String ticker;
    private int price;
}
