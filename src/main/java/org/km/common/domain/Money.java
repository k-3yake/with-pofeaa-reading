package org.km.common.domain;

import java.math.BigDecimal;

public class Money {
	private int amount;
	private Money(){
		
	}

	public static Money dollars(int amount) {
		Money money = new Money();
		money.amount = amount;
		return money;
	}

	public static Money dollars(BigDecimal bigDecimal) {
		Money result = new Money();
		result.amount = bigDecimal.intValue();
		return result;
	}

	public Money add(Money dollars) {
		Money result = new Money();
		result.amount += dollars.amount;
		result.amount += this.amount;
		return result;
	}

	public Money[] allocate(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal amount() {
		return new BigDecimal(amount);
	}

}
