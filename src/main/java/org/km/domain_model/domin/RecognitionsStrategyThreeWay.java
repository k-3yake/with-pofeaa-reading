package org.km.domain_model.domin;

import org.km.values.Money;

public class RecognitionsStrategyThreeWay extends RecognitionsStrategy {
	private int firstRecognitionOffset;
	private int secondRecognitionOffset;

	public RecognitionsStrategyThreeWay(int firstRecognitionOffset,int secondRecognitionOffset) {
		this.firstRecognitionOffset = firstRecognitionOffset;
		this.secondRecognitionOffset = secondRecognitionOffset;
	}

	@Override
	void calculateRevenueRecognitions(Contract contract) {
		Money[] allocation = contract.getRevenue().allocate(3);
		contract.addRevnueRecognition(new RevenueRecognition(allocation[0], contract.getWhenSigned()));
		contract.addRevnueRecognition(new RevenueRecognition(allocation[1], contract.getWhenSigned().addDays(firstRecognitionOffset)));
		contract.addRevnueRecognition(new RevenueRecognition(allocation[2], contract.getWhenSigned().addDays(secondRecognitionOffset)));
	}
}