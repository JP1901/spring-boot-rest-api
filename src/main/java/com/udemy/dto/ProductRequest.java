package com.udemy.dto;

import lombok.Data;

@Data

public class ProductRequest {
    private String name;
    private String description;
    private float price;
    private int quantity;
    private String status;
}
