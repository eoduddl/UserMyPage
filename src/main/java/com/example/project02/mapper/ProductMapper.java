package com.example.project02.mapper;

import com.example.project02.dto.ProductDTO;
import com.example.project02.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "productId", source = "id")
    ProductDTO toDTO(Product product);

    @Mapping(target = "id", source = "productId")
    Product toEntity(ProductDTO productDTO);

}