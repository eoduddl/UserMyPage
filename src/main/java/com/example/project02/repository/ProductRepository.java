package com.example.project02.repository;

import com.example.project02.entity.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // name과 일치하는 Data
    Optional<Product> findByName(String name);

    // stockQuantity가 0보다 큰 Data registerDate 내림차순
    @Query("SELECT p FROM Product p WHERE p.stockQuantity > :stockQuantity ORDER BY p.registerDate DESC ")
    List<Product> findByStockQuantityGreaterThan(@Param("stockQuantity") Integer stockQuantity);

    // Containing - keyword가 포함된 Name의 Data
    List<Product> findByNameContaining(String keyword);

    // Category Entity의 CategoryId값과 일치하는 Data
    List<Product> findByCategoryCategoryName(String categoryName);


//    조회수 오름차순 내림차순
    @Query("SELECT p FROM Product p WHERE p.stockQuantity > :stockQuantity ORDER BY p.click DESC")
    List<Product> findByOrderByClickDesc(@Param("stockQuantity") int stockQuantity);

    @Query("SELECT p FROM Product p WHERE p.stockQuantity > :stockQuantity ORDER BY p.click ASC")
    List<Product> findByOrderByClickAsc(@Param("stockQuantity") int stockQuantity);

    @Query("SELECT p FROM Product p WHERE p.name = :name")
    List<Product> findProductByProduct_name(@Param("name") String name);

    Product findByNameIgnoreCase(String name);


    List<Product> findExpiredProductsById(Long id);

}