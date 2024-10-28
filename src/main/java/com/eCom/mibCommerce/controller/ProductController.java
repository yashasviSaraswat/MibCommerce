package com.eCom.mibCommerce.controller;

import com.eCom.mibCommerce.model.BrandResponseDto;
import com.eCom.mibCommerce.model.ProductResponseDto;
import com.eCom.mibCommerce.model.TypeResponseDto;
import com.eCom.mibCommerce.service.BrandService;
import com.eCom.mibCommerce.service.ProductService;
import com.eCom.mibCommerce.service.TypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final BrandService brandService;
    private final TypeService typeService;

    public ProductController(ProductService productService, BrandService brandService, TypeService typeService) {
        this.productService = productService;
        this.brandService = brandService;
        this.typeService = typeService;
    }

    @GetMapping("/{Id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable("Id")Integer productId){
        ProductResponseDto response = productService.getProductById(productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "brandId", required = false) Integer brandId,
            @RequestParam(name = "typeId", required = false) Integer typeId,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order
    ){
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sorting = Sort.by(direction,sort);

        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<ProductResponseDto> productsRes = productService.getAllProducts(pageable, brandId, typeId, keyword);
        return new ResponseEntity<>(productsRes, HttpStatus.OK);
    }

    @GetMapping("/brands")
    public ResponseEntity<List<BrandResponseDto>> getBrands(){
        List<BrandResponseDto> brandRes = brandService.getAllBrands();
        return new ResponseEntity<>(brandRes, HttpStatus.OK);
    }

    @GetMapping("/types")
    public ResponseEntity<List<TypeResponseDto>> getTypes(){
        List<TypeResponseDto> typeRes = typeService.getAllTypes();
        return new ResponseEntity<>(typeRes, HttpStatus.OK);
    }
}
