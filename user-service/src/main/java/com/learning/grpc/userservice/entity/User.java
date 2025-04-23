package com.learning.grpc.userservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "customer")
@Data
public class User {

    @Id
    private int id;
    private String name;
    private int balance;
}
