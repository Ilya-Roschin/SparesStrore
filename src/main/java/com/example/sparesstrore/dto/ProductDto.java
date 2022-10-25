package com.example.sparesstrore.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto {

    private long id;
    private String name;
    private int price;
    private List<String> category;
}
