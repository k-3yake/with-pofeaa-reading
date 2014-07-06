package org.km.domain_model.domain;

public interface ContractRepository {

	Contract find(long contractNumber);

	void update(Contract contract);

}