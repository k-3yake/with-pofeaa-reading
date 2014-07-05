package org.km.domain_model.domin;

import org.km.values.MfDate;
import org.km.values.Money;

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