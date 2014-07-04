package org.km.domain_model.application.sevice;

import java.math.BigDecimal;
import java.util.Date;

import org.km.domain_model.domin.Contract;
import org.km.domain_model.domin.ContractRepository;
import org.km.domain_model.domin.Product;
import org.km.transaction_script.infla.AppException;
import org.km.values.MfDate;
import org.km.values.Money;

public class ContractService {
	private ContractRepository contractRepository; 

	public void addContract(String type,String productName,BigDecimal revenue){
		final Product product;
		if(type.equals("S")){
			product = Product.newSpreadsheet(productName);
		}else if(type.equals("W")){
			product = Product.newWordProcessor(productName);
		}else if(type.equals("D")){
			product = Product.newDatabase(productName);
		}else{
			throw new AppException("不正なtypeです。type=" + type);
		}
		Contract contract = new Contract(product , Money.dollars(revenue), new MfDate());
		contractRepository.add(contract);
	}
}
