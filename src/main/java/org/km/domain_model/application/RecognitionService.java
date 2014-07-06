package org.km.domain_model.application;

import org.km.common.domain.IRecognitionService;
import org.km.common.domain.MfDate;
import org.km.common.domain.Money;
import org.km.domain_model.domain.Contract;
import org.km.domain_model.domain.ContractRepository;
import org.km.domain_model.infla.JDBCContractRepository;

public class RecognitionService implements IRecognitionService{
	private ContractRepository contractRepository = new JDBCContractRepository();;

	@Override
	public Money recognizedRevenue(long contractNumber, MfDate asof) {
		Contract contract = contractRepository.find(contractNumber);
		return contract.recongnizedRevenue(asof);
	}
	
	@Override
	public void calculateRevenueRecognitions(long contractNumber){
		Contract contract = contractRepository.find(contractNumber);
		contract.calculateRecognitions();
		contractRepository.update(contract);
	}
}
