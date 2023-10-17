package com.example.project02.dto;

import com.example.project02.constant.ProductSerllStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private double price;
    private String productDescription;
    private int stockQuantity;
    private LocalDateTime registerDate;
    private Date fieldPredictedSaleEndDate;
    private ProductSerllStatus productSellStatus;
    private String img1;
    private String img2;
    private String img3;
    private CategoryDTO category; // Product와 Category 간의 관계를 표현하는 DTOe
    private Long SellerId; //판매자 정보 추가

}
