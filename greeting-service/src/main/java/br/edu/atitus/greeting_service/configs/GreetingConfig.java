package br.edu.atitus.greeting_service.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("greeting-service")
public class GreetingConfig {

	private String greeting;
	private String defaultName;
	
	public String getGreeting() {
		return greeting;
	}
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}
	public String getDefaultName1() {
		return defaultName;
	}
	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
	}
	public String getDefaultName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
