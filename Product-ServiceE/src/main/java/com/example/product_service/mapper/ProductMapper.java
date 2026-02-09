package com.example.product_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.product_service.dto.ProductDto;
import com.example.product_service.enity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductDto dto);

    ProductDto toDto(Product entity);

    // for update use-case (PUT)
    void updateEntityFromDto(ProductDto dto, @MappingTarget Product entity);
}