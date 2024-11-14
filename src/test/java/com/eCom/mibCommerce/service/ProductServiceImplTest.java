package com.eCom.mibCommerce.service;

import com.eCom.mibCommerce.entity.Brand;
import com.eCom.mibCommerce.entity.Product;
import com.eCom.mibCommerce.entity.Type;
import com.eCom.mibCommerce.exceptions.ProductNotFoundException;
import com.eCom.mibCommerce.model.ProductResponseDto;
import com.eCom.mibCommerce.repository.ProductRepository;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void getProductByIdShouldGetProductById() {
        //Given
        Integer productId = 1;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test");
        product.setDescription("Test");
        product.setPrice(100L);
        product.setBrand(new Brand(1,"mib",new ArrayList<>()));
        product.setType(new Type(1,"ghost",new ArrayList<>()));
        //When
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        ProductResponseDto productResponseDto = productService.getProductById(productId);
        //Then
        assertNotNull(productResponseDto);
        assertEquals(productId, productResponseDto.getId());
        assertEquals("Test", productResponseDto.getName());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getProductByIdShouldThrowExceptionWhenProductNotFound() {
        Integer productId = 1;
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void GetAllProductsShouldGetAllProducts() {
        Integer productId = 1;
        PageRequest pageRequest = PageRequest.of(0, 10);
        Product product = new Product();
        product.setId(productId);
        product.setName("Test");
        product.setDescription("Test");
        product.setPrice(100L);
        product.setDescription("Test");
        product.setBrand(new Brand(1,"mib",new ArrayList<>()));
        product.setType(new Type(1,"ghost",new ArrayList<>()));
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
        Mockito.when(productRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(productPage);
        Page<ProductResponseDto> responseDtos = productService.getAllProducts(pageRequest,null,null,null);
        assertNotNull(responseDtos);
        assertEquals(1, responseDtos.getTotalElements());
        assertEquals("Test", responseDtos.getContent().getFirst().getName());
        verify(productRepository, times(1)).findAll(any(Specification.class), eq(pageRequest));
    }
}