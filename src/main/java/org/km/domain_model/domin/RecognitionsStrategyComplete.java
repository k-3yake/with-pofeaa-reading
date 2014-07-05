package org.km.domain_model.domin;


public class RecognitionsStrategyComplete extends RecognitionsStrategy {

	@Override
	void calculateRevenueRecognitions(Contract contract) {
		contract.addRevnueRecognition(new RevenueRecognition(contract.getRevenue(), contract.getWhenSigned()));
	}
}
