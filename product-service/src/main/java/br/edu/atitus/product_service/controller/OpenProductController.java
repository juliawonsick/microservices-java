package br.edu.atitus.product_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.product_service.clients.CurrencyClient;
import br.edu.atitus.product_service.clients.CurrencyResponse;
import br.edu.atitus.product_service.entites.ProductEntity;
import br.edu.atitus.product_service.repositories.ProductRepository;

@RestController
@RequestMapping("products")
public class OpenProductController {

	private final ProductRepository repository;
	private final CurrencyClient currencyClient;
	private final CacheManager cacheManager;

	@Value("${server.port}")
	private String port;

	public OpenProductController(ProductRepository repository, CurrencyClient currencyClient,
			CacheManager cacheManager) {
		this.repository = repository;
		this.currencyClient = currencyClient;
		this.cacheManager = cacheManager;
	}

	@Value("${server.port}")
	private int serverPort;

	@GetMapping("/{idProduct}/{targetCurrency}")
	public ResponseEntity<ProductEntity> getProduct(@PathVariable Long idProduct, @PathVariable String targetCurrency)
			throws Exception {

		targetCurrency = targetCurrency.toUpperCase();
		String nameCache = "Product";
		String keyCache = idProduct + targetCurrency;

		ProductEntity product = cacheManager.getCache(nameCache).get(keyCache, ProductEntity.class);

		if (product == null) {

			product = repository.findById(idProduct).orElseThrow(() -> new Exception("Product not found"));
			product.setEnvironment("product-service running on Port: " + serverPort);

			if (product.getCurrency().equals(targetCurrency)) {
				product.setConvertedPrice(product.getPrice());

			} else {
				CurrencyResponse currency = currencyClient.getCurrency(product.getPrice(), product.getCurrency(),
						targetCurrency);
				if (currency != null) {
					product.setConvertedPrice(currency.getConvertedValue());
					product.setEnvironment(product.getEnvironment() + " - " + currency.getEnviroment());
					
					cacheManager.getCache(nameCache).put(keyCache, product);
				} else {
					product.setConvertedPrice((double) -1);
					product.setEnvironment(product.getEnvironment() + " - Currency unavalaible");
				}
			}
		} else {
			product.setEnvironment("Product-service running on Port: " + serverPort + " - DataSource: cache");
		}
		return ResponseEntity.ok(product);
	}
}