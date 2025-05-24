 package br.edu.atitus.currency_service.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.currency_service.CurrencyRepository;
import br.edu.atitus.currency_service.clients.CurrencyBCClient;
import br.edu.atitus.currency_service.clients.CurrencyBCResponse;
import br.edu.atitus.currency_service.entities.CurrencyEntity;

@RestController
@RequestMapping("currency")
public class CurrencyController {
	
	private final CurrencyRepository repository;
	private final CurrencyBCClient currencyBCClient;

	public CurrencyController(CurrencyRepository repository, CurrencyBCClient currencyBCClient) {
		super();
		this.repository = repository;
		this.currencyBCClient = currencyBCClient;
	}
	
	@Value("${server.port}")
	private int serverPort;
	
	@GetMapping("/{value}/{source}/{target}")
	public ResponseEntity<CurrencyEntity> getCurrency(
			
			@PathVariable double value,
			@PathVariable String source,
			@PathVariable String target
			
			) throws Exception {
		//CurrencyEntity currency = repository.findBySourceAndTarget(source, target)
				//.orElseThrow(() -> new Exception("Currency Unsupported"));
		
		source = source.toUpperCase();
		target = target.toUpperCase();
		String dataSource = "None";
		
		CurrencyEntity currency = new CurrencyEntity();
		currency.setSource(source);
		currency.setTarget(target);
		if (source.equals(target)) {
			currency.setConversionRate(1);
		} else {
			try {
				double curSource = 1;
				double curTarget =1;
				if (!source.equals("BRL")) {
					CurrencyBCResponse resp = currencyBCClient.getCurrency(source);
					if (resp.getValue().isEmpty()) throw new Exception("Currency not found for " + source);
					curSource = resp.getValue().get(0).getCotacaoVenda();
					}
				if(!target.equals("BRL")) {
					CurrencyBCResponse resp = currencyBCClient.getCurrency(target);
					if (resp.getValue().isEmpty()) throw new Exception("Currency not found for " + target);
					curTarget = resp.getValue().get(0).getCotacaoVenda();
				}
				currency.setConversionRate(curSource / curTarget);
				dataSource = "API BCB";
			} catch (Exception e) {
				currency = repository.findBySourceAndTarget(source, target).orElseThrow(() -> new Exception("Currency Unsupported"));
				dataSource = "Local Database";
			}
		}
		
		currency.setConvertedValue(value * currency.getConversionRate());
		currency.setEnviroment("Currency-service running on port: " + serverPort + " - DataSource: " + dataSource);
		
		return ResponseEntity.ok(currency);
	}
}
