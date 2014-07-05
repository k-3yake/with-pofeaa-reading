package org.km.domain_model.domin;


public class CompleteRecognitinStrategy extends RecognitionsStrategy {

	@Override
	void calculateRevenueRecognitions(Contract contract) {
		contract.addRevnueRecognition(new RevenueRecognition(contract.getRevenue(), contract.getWhenSigned()));
	}
}
