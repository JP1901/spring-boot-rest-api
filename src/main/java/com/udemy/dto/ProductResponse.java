package com.udemy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ProductResponse {
    private String name;
    private String description;
    private float price;
    private int quantity;
    private String status;
    private String message;


}
