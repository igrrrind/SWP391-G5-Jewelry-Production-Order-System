package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.pojos.designPojos.Product;
import com.swp391.JewelryProduction.pojos.designPojos.ProductSpecification;
import com.swp391.JewelryProduction.services.product.ProductService;
import com.swp391.JewelryProduction.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/get-product-list")
    public ResponseEntity<Response> getProducts() {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request send successfully.")
                .response("product-list", productService.findAll())
                .buildEntity();
    }

    @DeleteMapping("/{productId}/remove")
    public ResponseEntity<Response> removeProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Product with id "+productId+" has been deleted successfully")
                .buildEntity();
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createProduct(@RequestBody Product product) {
        productService.saveProduct(product);
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request send successfully.")
                .buildEntity();
    }

    @PostMapping("/customize")
    public ResponseEntity<Response> saveCustomization(@RequestBody ProductSpecification specs) {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request send successfully.")
                .response("product-specification", productService.save(specs))
                .buildEntity();
    }
}
