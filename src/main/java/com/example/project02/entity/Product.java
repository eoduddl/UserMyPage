package com.example.project02.entity;

import com.example.project02.constant.ProductSerllStatus;
import com.example.project02.exception.OutOfStockException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", unique = true,length = 20)
    private String name;

    private double price;

    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private LocalDateTime registerDate;

    private LocalDateTime fieldPredictedSaleEndDate;

    private String img1;

    private String img2;

    private String img3;

    private String productDescription;

    @Temporal(TemporalType.DATE)
    @Column(name = "field_predicted_sale_enddate",nullable = false)
    private Date fieldPredictedSaleEnddate;

    @Enumerated(EnumType.STRING)
    private ProductSerllStatus productSerllStatus; //상품 판매상태

    @Column(name = "seller_id")
    private Long sellerId;

    //    조회수
    private int click;


    public void removeStock(int quantity) {
        int stock = stockQuantity - quantity;
        if (stock < 0) {
            throw new OutOfStockException("재고가 부족합니다.");
        } else {
            stockQuantity = stock;
        }
    }



}
