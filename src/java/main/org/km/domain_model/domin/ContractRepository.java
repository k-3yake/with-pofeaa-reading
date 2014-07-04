package org.km.domain_model.domin;

public interface ContractRepository {

	Contract find(long contractNumber);

	void update(Contract contract);

}