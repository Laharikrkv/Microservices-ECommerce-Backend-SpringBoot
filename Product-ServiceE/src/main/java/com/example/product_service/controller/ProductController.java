

package com.example.product_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.product_service.dto.ProductDto;
import com.example.product_service.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // CREATE PRODUCT

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')" )
    public ResponseEntity<String> createProduct(@RequestBody ProductDto productDto) {
    	System.out.println("Current Authorities: " + 
    	        SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        String response = productService.createDto(productDto);
        return ResponseEntity.ok(response);
    }

    // GET PRODUCT BY ID

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        ProductDto product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    // GET ALL PRODUCTS
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // UPDATE PRODUCT
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('write:products')")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto productDto) {

        ProductDto updated = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updated);
    }

    // UPDATE STOCK
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('write:products')")
    public ResponseEntity<String> updateStock(
            @PathVariable Long id, 
            @RequestParam Integer quantity) {

        String response = productService.updateQuantity(id, quantity);
        return ResponseEntity.ok(response);
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('delete:products')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
    
    
    @GetMapping("/validate/{id}/{quantity}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> validateProduct(
            @PathVariable Long id,
            @PathVariable Integer quantity) {

        Boolean validated = productService.validateStock(id, quantity);
        if (validated) {
        	return ResponseEntity.ok("Product is available");
        }else {
        	return ResponseEntity.ok("Sorry, Stock is unavailable");
        }
    }	
    
}