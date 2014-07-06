package org.km.domain_model.domain;


public class RecognitionsStrategyComplete extends RecognitionsStrategy {

	@Override
	void calculateRevenueRecognitions(Contract contract) {
		contract.addRevnueRecognition(new RevenueRecognition(contract.getRevenue(), contract.getWhenSigned()));
	}
}
