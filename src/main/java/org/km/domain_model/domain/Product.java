package org.km.domain_model.domain;

import org.km.transaction_script.infla.AppException;


public class Product {
	private String name;
	private RecognitionsStrategy recogitinsStrategy;
	
	public Product(String name, RecognitionsStrategy recogitinsStrategy) {
		this.name = name;
		this.recogitinsStrategy = recogitinsStrategy;
	}
	
	public static Product newProduct(String type,String productName){
		if(type.equals("S")){
			return new Product(productName, new RecognitionsStrategyThreeWay(60,90));
		}else if(type.equals("W")){
			return new Product(productName, new RecognitionsStrategyComplete());
		}else if(type.equals("D")){
			return new Product(productName, new RecognitionsStrategyThreeWay(30,60));
		}else{
			throw new AppException("不正なtypeです。type=" + type);
		}
	}

	public void calulateRevenueRecognitons(Contract contract) {
		recogitinsStrategy.calculateRevenueRecognitions(contract);
	}
}