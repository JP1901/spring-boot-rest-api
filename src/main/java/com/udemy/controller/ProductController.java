package com.udemy.controller;

import com.udemy.dto.ProductRequest;
import com.udemy.dto.ProductResponse;
import com.udemy.model.Product;
import com.udemy.services.ProductService;
import com.udemy.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private final JWTUtil jwtUtil = new JWTUtil();

    /*
            try {
            if(!this.jwtUtil.isTokenValid(jwt)){
                return ResponseEntity.badRequest().body("JWT inválido.");
            }

            if(!this.jwtUtil.verifyRole(jwt,"SUPER")){
                return ResponseEntity.badRequest().body("Su rol no cumple con los requisitos.");
            }


        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo crear el producto, " + e.getMessage());
        }
    */

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody ProductRequest productRequest, @RequestHeader("Authorization") String jwt) {
        try {
            if (!this.jwtUtil.isTokenValid(jwt)) {
                return ResponseEntity.badRequest().body("JWT inválido.");
            }

            if (!this.jwtUtil.verifyRole(jwt, "SUPER")) {
                return ResponseEntity.badRequest().body("Su rol no cumple con los requisitos.");
            }

            ProductResponse productResponse;
            productResponse = this.productService.create(productRequest);

            return ResponseEntity.ok(productResponse);


        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo crear el producto, " + e.getMessage());
        }

    }

    @GetMapping("/list")
    public ResponseEntity listProducts(@RequestHeader("Authorization") String jwt) {
        try {
            if (!this.jwtUtil.isTokenValid(jwt)) {
                return ResponseEntity.badRequest().body("JWT inválido.");
            }

            if (!this.jwtUtil.verifyRole(jwt, "TEC")) {
                return ResponseEntity.badRequest().body("Su rol no cumple con los requisitos.");
            }

            List<Product> products;
            products = this.productService.listProducts();
            return ResponseEntity.ok(products);


        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo listar los productos, " + e.getMessage());
        }

    }

    @GetMapping("/list-by-price/{price}")
    public ResponseEntity getProductsByPrice(@PathVariable float price, @RequestHeader("Authorization") String jwt) {
        try {
            if (!this.jwtUtil.isTokenValid(jwt)) {
                return ResponseEntity.badRequest().body("JWT inválido.");
            }

            if (!this.jwtUtil.verifyRole(jwt, "SUPER")) {
                return ResponseEntity.badRequest().body("Su rol no cumple con los requisitos.");
            }

            List<Product> products;
            products = this.productService.getProductsByPrice(price);
            return ResponseEntity.ok(products);


        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se encontraron productos que cumplan ese requisito, " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestHeader("Authorization") String jwt, @RequestBody ProductRequest productRequest) {
        try {
            if (!this.jwtUtil.isTokenValid(jwt)) {
                return ResponseEntity.badRequest().body("JWT inválido.");
            }

            if (!this.jwtUtil.verifyRole(jwt, "VIS")) {
                return ResponseEntity.badRequest().body("Su rol no cumple con los requisitos.");
            }

            ProductResponse productResponse;
            productResponse = this.productService.update(id, productRequest);
            return ResponseEntity.ok(productResponse);


        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo actualizar el producto, " + e.getMessage());
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestHeader("Authorization") String jwt) {
        try {
            if (!this.jwtUtil.isTokenValid(jwt)) {
                return ResponseEntity.badRequest().body("JWT inválido.");
            }

            if (!this.jwtUtil.verifyRole(jwt, "ADMIN")) {
                return ResponseEntity.badRequest().body("Su rol no cumple con los requisitos.");
            }

            String message;
            message = this.productService.delete(id);
            return ResponseEntity.ok(message);


        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo crear el producto, " + e.getMessage());
        }
    }


}
