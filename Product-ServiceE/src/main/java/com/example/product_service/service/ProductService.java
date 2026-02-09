
package com.example.product_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.product_service.dto.ProductDto;
import com.example.product_service.enity.Product;
import com.example.product_service.mapper.ProductMapper;
import com.example.product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	 private final ProductRepository productRepository;
	 private final ProductMapper productMapper;
	 
	    public String createDto(ProductDto productDto) {
	        Product product = productMapper.toEntity(productDto);
	        product.setId(null); 
	        productRepository.save(product);
	        return "Product created successfully";
	    }
	    
	    public ProductDto getProduct(Long id) {
	        Product product = productRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Product not found"));
	        return productMapper.toDto(product);
	    }
	    
	    public List<ProductDto> getAllProducts() {
	        return productRepository.findAll()
	                .stream()
	                .map(productMapper::toDto)
	                .collect(Collectors.toList());
	    }
	    
	    public ProductDto updateProduct(Long id, ProductDto productDto) {
	        Product product = productRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Product not found"));

	        productMapper.updateEntityFromDto(productDto, product);

	        return productMapper.toDto(productRepository.save(product));
	    }
	    
	    public String updateQuantity(Long id, Integer quantity) {
	        Product product = productRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Product not found"));

	        product.setStock(quantity);
	        productRepository.save(product);
	        return "Stock updated";
	    }
	    
	    public void deleteProduct(Long id) {
	        if (!productRepository.existsById(id)) {
	            throw new RuntimeException("Product not found");
	        }
	        productRepository.deleteById(id);
	    }
	    
	    public Boolean validateStock(Long id, Integer quantity) {
	        Product product = productRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Product not found"));

	        return product.getStock() >= quantity;
	    }
}
