package org.km.domain_model.domain;

import org.km.common.domain.MfDate;
import org.km.common.domain.Money;

public class RevenueRecognition {
	private Money amount;
	private MfDate date;
	
	public RevenueRecognition(Money money, MfDate date) {
		this.amount = money;
		this.date = date;
	}
	
	public Money getAmount(){
		return amount;
	}
	
	boolean isRecognizableBy(MfDate asOf){
		return asOf.after(date) || asOf.equals(date);
	}
}