package com.example.project02.controller;

import com.example.project02.dto.ProductDTO;
import com.example.project02.dto.SaleRecord;
import com.example.project02.entity.Product;
import com.example.project02.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    // 쇼핑몰 판매 물품 등록
    @PostMapping("/sellproduct/register")
    public String createProduct(
            @RequestParam String productName,
            @RequestParam String productDescription,
            @RequestParam Double price,
            @RequestParam Integer stockQuantity,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date registrationDate,
            Model model) {

        Product product = new Product();
        product.setName(productName);
        product.setProductDescription(productDescription);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
        product.setRegisterDate(LocalDateTime.now());
        product.setFieldPredictedSaleEnddate(registrationDate);

        productService.registerProduct(product);

        model.addAttribute("message", "물품이 성공적으로 등록되었습니다.");
        return "successPage";
    }

    // 판매 물품 조회
    @GetMapping("/sellproduct/{productname}")
    public ResponseEntity<ProductDTO> getProductByName(@PathVariable String productName) {
        ProductDTO product = productService.getProductByName(productName);
        return ResponseEntity.ok(product);
    }



    // 판매 물품 재고 수정
    @PutMapping("/sellproduct/{productName}/stock")
    public ResponseEntity<String> updateProductStockByProductName(
            @PathVariable String productName,
            @RequestParam int newStockQuantity) {
        boolean success = productService.updateProductStockByProductName(productName, newStockQuantity);

        if (success) {
            return ResponseEntity.ok("재고 수정이 성공적으로 이루어졌습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("재고 수정에 실패했습니다.");
        }
    }

    //이미지 등록
    @PostMapping("/product/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {

            saveImage(file);
            return new ResponseEntity<>("이미지업로드에 성공했습니다:", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("이미지업로드에 실패했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void saveImage(MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
        } else {
            throw new Exception("빈 이미지 파일입니다");
        }
    }

    // 판매종료 날짜가 지난 판매 물품 내역 조회
    @GetMapping("/sellproducts/{productId}/history")
    public List<SaleRecord> getExpiredProductHistory(@PathVariable Long productId) {
        List<SaleRecord> expiredProductHistory = productService.getExpiredProductHistory(productId);
        return expiredProductHistory;
    }
}

