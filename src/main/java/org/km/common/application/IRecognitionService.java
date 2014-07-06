package org.km.common.application;

import org.km.common.domain.MfDate;
import org.km.common.domain.Money;

public interface IRecognitionService {

	Money recognizedRevenue(long contractNumber, MfDate asof);

	void calculateRevenueRecognitions(long contractNumber);

}
