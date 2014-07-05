package org.km.transaction_script.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.km.transaction_script.infla.AppException;
import org.km.transaction_script.infla.Gateway;
import org.km.values.MfDate;
import org.km.values.Money;

public class RecognitionService {
	private Gateway db;

	public Money recognizedRevenue(long contractNumber, MfDate asof) {
		Money result = Money.dollars(0);
		try {
			ResultSet resultSet = db.findRecognitionsFor(contractNumber, asof);
			while(resultSet.next()){
				result = result.add(Money.dollars(resultSet.getBigDecimal("amount")));
			}
			return result;
		} catch (SQLException e) {
			throw new AppException(e);
		}
	}
	
	public void calculateRevenueRecognitions(long contractNumber){
		try {
			ResultSet contacts = db.findContract(contractNumber);
			contacts.next();
			Money totalRevenue = Money.dollars(contacts.getBigDecimal("revenue"));
			MfDate recognitionDate = new MfDate(contacts.getDate("dateSigned"));
			String type = contacts.getString("type");
			if(type.equals("S")){
				Money[] allocation = totalRevenue.allocate(3);
				db.insertRecognition(contractNumber,allocation[0],recognitionDate);
				db.insertRecognition(contractNumber,allocation[1],recognitionDate.addDays(60));
				db.insertRecognition(contractNumber,allocation[2],recognitionDate.addDays(90));
			}else if(type.equals("W")){
				db.insertRecognition(contractNumber, totalRevenue, recognitionDate);
			}else if(type.equals("D")){
				Money[] allocation = totalRevenue.allocate(3);
				db.insertRecognition(contractNumber, allocation[0], recognitionDate);
				db.insertRecognition(contractNumber, allocation[1], recognitionDate.addDays(30));
				db.insertRecognition(contractNumber, allocation[2], recognitionDate.addDays(60));
			}
		} catch (SQLException e) {
			throw new AppException(e);
		}
	}
}
