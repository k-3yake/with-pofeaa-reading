package org.km.domain_model.domain;

import java.util.ArrayList;
import java.util.List;

import org.km.common.domain.MfDate;
import org.km.common.domain.Money;


public class Contract {
	private Long id;
	private List<RevenueRecognition> revenueRecognitions = new ArrayList<>();
	private Product product;
	private Money revenue;
	private MfDate whenSigned;
	
	public Contract(Product product, Money revenue, MfDate whenSigned) {
		this.product = product;
		this.revenue = revenue;
		this.whenSigned = whenSigned;
	}

	public Money recongnizedRevenue(MfDate asOf){
		Money result = Money.dollars(0);
		for (RevenueRecognition r : revenueRecognitions) {
			if(r.isRecognizableBy(asOf)){
				result = result.add(r.getAmount());
			}
		}
		return result;
	}
	
	public void calculateRecognitions(){
		product.calulateRevenueRecognitons(this);
	}

	Money getRevenue() {
		return revenue;
	}

	MfDate getWhenSigned() {
		return whenSigned;
	}

	void addRevnueRecognition(RevenueRecognition revenueRecognition) {
		this.revenueRecognitions.add(revenueRecognition);
	}
}