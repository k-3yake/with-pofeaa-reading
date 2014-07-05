package org.km.domain_model.application.sevice;

import org.km.domain_model.domin.Contract;
import org.km.domain_model.domin.ContractRepository;
import org.km.values.MfDate;
import org.km.values.Money;

public class RecognitionService {
	private ContractRepository contractRepository;

	public Money recognizedRevenue(long contractNumber, MfDate asof) {
		Contract contract = contractRepository.find(contractNumber);
		return contract.recongnizedRevenue(asof);
	}
	
	public void calculateRevenueRecognitions(long contractNumber){
		Contract contract = contractRepository.find(contractNumber);
		contract.calculateRecognitions();
		contractRepository.update(contract);
	}
}
