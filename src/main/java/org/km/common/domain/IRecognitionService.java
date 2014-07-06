package org.km.common.domain;

public interface IRecognitionService {

	Money recognizedRevenue(long contractNumber, MfDate asof);

	void calculateRevenueRecognitions(long contractNumber);

}
