package br.edu.atitus.currency_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "tb_currency")
public class CurrencyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "source_currency", nullable = false, length = 3)
	private String source;
	
	@Column(name = "target_currency", nullable = false, length = 3)
	private String target;
	
	@Column(name = "conversion_rate", nullable = false, length = 3)
	private double conversionRate;
	
	@Transient
	private double convertedValue;
	@Transient
	private String enviroment;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public double getConversionRate() {
		return conversionRate;
	}
	public void setConversionRate(double conversionRate) {
		this.conversionRate = conversionRate;
	}
	public double getConvertedValue() {
		return convertedValue;
	}
	public void setConvertedValue(double convertedValue) {
		this.convertedValue = convertedValue;
	}
	public String getEnviroment() {
		return enviroment;
	}
	public void setEnviroment(String enviroment) {
		this.enviroment = enviroment;
	}
	
	
}
