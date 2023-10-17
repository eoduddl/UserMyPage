package com.example.project02.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleRecord {
    private Long productId;
    private String productName;
    private Date saleEndDate;
    private double salePrice;

}
