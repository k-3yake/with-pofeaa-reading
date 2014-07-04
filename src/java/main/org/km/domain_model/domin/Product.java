package org.km.domain_model.domin;

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
			return Product.newSpreadsheet(productName);
		}else if(type.equals("W")){
			return Product.newWordProcessor(productName);
		}else if(type.equals("D")){
			return Product.newDatabase(productName);
		}else{
			throw new AppException("不正なtypeです。type=" + type);
		}
	}

	public static Product newWordProcessor(String name){
		return new Product(name, new CompleteRecognitinStrategy());
	}
	
	public static Product newSpreadsheet(String name){
		return new Product(name, new ThreeWayRecognintinStrategy(60,90));
	}
	
	public static Product newDatabase(String name){
		return new Product(name, new ThreeWayRecognintinStrategy(30,60));
	}

	public void calulateRevenueRecognitons(Contract contract) {
		recogitinsStrategy.calculateRevenueRecognitions(contract);
	}
}