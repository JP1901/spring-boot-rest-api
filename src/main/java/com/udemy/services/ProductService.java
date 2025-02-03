package com.udemy.services;

import com.udemy.dao.ProductRepository;
import com.udemy.dto.ProductRequest;
import com.udemy.dto.ProductResponse;
import com.udemy.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> listProducts() throws Exception {
        if(this.productRepository.findAll().isEmpty()){
            throw new Exception("Sin registros.");
        }
        return this.productRepository.findAll();
    }

    //List<Product> findByPriceGreaterThanEqual (float price);

    public List<Product> getProductsByPrice(float price) throws Exception {
        if(this.productRepository.findByPriceGreaterThanEqual(price).isEmpty()){
            throw new Exception("Sin registros.");
        }
        return this.productRepository.findByPriceGreaterThanEqual(price);
    }

    public ProductResponse create(ProductRequest productRequest) throws Exception {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setDescription(productRequest.getDescription());
        product.setStatus(productRequest.getStatus());

        this.productRepository.save(product);

        return new ProductResponse(product.getName(), product.getDescription(), product.getPrice(), product.getQuantity(), product.getStatus(), "Producto creado.");

    }

    public ProductResponse update(Long id, ProductRequest productRequest) throws Exception{
        Product existingProduct = this.productRepository.findById(id)
                .orElseThrow(() -> new Exception("Producto no encontrado"));
        existingProduct.setName(productRequest.getName());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setQuantity(productRequest.getQuantity());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setStatus(productRequest.getStatus());

        this.productRepository.save(existingProduct);

        return new ProductResponse(existingProduct.getName(), existingProduct.getDescription(), existingProduct.getPrice(), existingProduct.getQuantity(), existingProduct.getStatus(), "Producto actualizado");

    }

    public String delete(Long id) throws Exception{
        Product existingProduct = this.productRepository.findById(id)
                .orElseThrow(() -> new Exception("Producto no encontrado"));


        this.productRepository.delete(existingProduct);

        return "El producto con id: " + id + " ha sido eliminado";
    }
}
