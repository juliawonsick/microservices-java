package br.edu.atitus.product_service.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.product_service.entites.ProductEntity;
import br.edu.atitus.product_service.repositories.ProductRepository;

@RestController
@RequestMapping("/products")
public class OpenProductController {

    private final ProductRepository repository;

    @Value("${server.port}")
    private String port;


    public OpenProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}/{currency}")
    public ProductEntity findProduct(@PathVariable Long id, @PathVariable String currency) {
        Optional<ProductEntity> optionalProduct = repository.findById(id);

        ProductEntity product = optionalProduct.orElseThrow(() -> new RuntimeException("Product not found"));
        product.setConvertedPrice(product.getPrice()); 
        product.setEnvironment("PORT = " + port);

        return product;
    }
}