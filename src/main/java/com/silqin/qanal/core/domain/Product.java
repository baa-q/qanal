package com.silqin.qanal.core.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Product {
    private String productId;
    private String productName;
    private double price;
    private double unitPrice;
    private double ratingStar;
    private int ratingCount;
    private String memo;

}
