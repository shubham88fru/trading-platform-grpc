package com.learning.grpc.userservice.entity;

import com.learning.common.Ticker;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PortfolioItem {
    @Id
    @GeneratedValue
    private int id;

    @Column(name="customer_id")
    private int userId;
    private Ticker ticker;
    private int quantity;
}
