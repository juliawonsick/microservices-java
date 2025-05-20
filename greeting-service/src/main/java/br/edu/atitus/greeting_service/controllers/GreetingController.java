package br.edu.atitus.greeting_service.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.greeting_service.configs.GreetingConfig;

@RestController
@RequestMapping("greeting-service")
public class GreetingController {

//	@Value("${greeting-service.greeting}")
//	private String greeting;
//	
//	@Value("${greeting-service.default-name}")
//	private String defaultName;

	private final GreetingConfig greetingConfig;

	public GreetingController(GreetingConfig greetingConfig) {
		super();
		this.greetingConfig = greetingConfig;
	}

	@GetMapping({ "", "/", "/{namePath}" })
	public ResponseEntity<String> getGreetingService(@RequestParam(required = false) String name,
			@PathVariable(required = false) String namePath) {
		if (name == null)
			name = namePath != null ? namePath : greetingConfig.getDefaultName();

		String textReturn = String.format("%s %s!!!", greetingConfig.getGreeting(), name);
		return ResponseEntity.ok(textReturn);
	}

}