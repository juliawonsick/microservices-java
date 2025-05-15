package br.edu.atitus.product_service.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.product_service.entites.ProductServiceEntity;
import br.edu.atitus.product_service.repositories.ProductServiceRepository;

@RestController
@RequestMapping("/products")
public class ProductServiceController {

    private final ProductServiceRepository repository;

    @Value("${server.port}")
    private String port;


    public ProductServiceController(ProductServiceRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}/{currency}")
    public ProductServiceEntity findProduct(@PathVariable Long id, @PathVariable String currency) {
        Optional<ProductServiceEntity> optionalProduct = repository.findById(id);

        ProductServiceEntity product = optionalProduct.orElseThrow(() -> new RuntimeException("Product not found"));
        product.setConvertedPrice(product.getPrice()); 
        product.setEnvironment("PORT = " + port);

        return product;
    }
}