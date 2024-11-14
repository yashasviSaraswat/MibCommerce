package com.eCom.mibCommerce.service;

import com.eCom.mibCommerce.entity.Brand;
import com.eCom.mibCommerce.entity.Product;
import com.eCom.mibCommerce.entity.Type;
import com.eCom.mibCommerce.exceptions.ProductNotFoundException;
import com.eCom.mibCommerce.model.ProductResponseDto;
import com.eCom.mibCommerce.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductServiceImpl productService;

    Integer productId = 1;
    Product product = new Product();

    @BeforeEach
    void setup() {
        System.out.println("beforeEach setUp is activated...");
        product.setId(productId);
        product.setName("Test");
        product.setDescription("Test");
        product.setPrice(100L);
        product.setBrand(new Brand(1, "mib", new ArrayList<>()));
        product.setType(new Type(1, "ghost", new ArrayList<>()));
    }

    @AfterEach
    void tearDown() {
        System.out.println("afterEach tearDown is activated...");
        productId = null;
        product = null;
    }

    @Test
    void getProductByIdShouldGetProductById() {
        //When
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        ProductResponseDto productResponseDto = productService.getProductById(productId);
        //Then
        assertNotNull(productResponseDto);
        assertEquals(productId, productResponseDto.getId());
        assertEquals("Test", productResponseDto.getName());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getProductByIdShouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void GetAllProductsShouldGetAllProducts() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
        when(productRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(productPage);
        Page<ProductResponseDto> responseDtos = productService.getAllProducts(pageRequest,null,null,null);
        assertNotNull(responseDtos);
        assertEquals(1, responseDtos.getTotalElements());
        assertEquals("Test", responseDtos.getContent().getFirst().getName());
        verify(productRepository, times(1)).findAll(any(Specification.class), eq(pageRequest));
    }
}