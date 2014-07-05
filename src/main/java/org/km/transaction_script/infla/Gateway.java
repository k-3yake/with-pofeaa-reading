package org.km.transaction_script.infla;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.km.values.MfDate;
import org.km.values.Money;

public class Gateway {
	private Connection db;
	
	public ResultSet findRecognitionsFor(long contractID,MfDate asof) throws SQLException{
		PreparedStatement stmt = db.prepareStatement(findRecognitionsStatement); 
		stmt.setLong(1, contractID);
		stmt.setDate(2, asof.toSqlDate());
		ResultSet resultSet = stmt.executeQuery();
		return resultSet;
	}
	private static final String findRecognitionsStatement =
			"select amount"
			+ "from revenueRecognitions "
			+ "where contract = ? and recognizedOn <= ?";
	
	public ResultSet findContract(long contractID) throws SQLException{
		PreparedStatement stmt = db.prepareStatement(findContractsStatement );
		stmt.setLong(1, contractID);
		ResultSet resultSet = stmt.executeQuery();
		return resultSet;
	}
	private static final String findContractsStatement = 
			"select * "
			+ " from contracts c, products p "
			+ " where id = ? and c.produc = p.ID";
	
	
	public void insertRecognition(long contractNumber, Money money,	MfDate recognitionDate) throws SQLException {
		PreparedStatement stmt = db.prepareStatement(insertRecognitionStatement);
		stmt.setLong(1, contractNumber);
		stmt.setBigDecimal(2, money.amount());
		stmt.setDate(3, recognitionDate.toSqlDate());
		stmt.executeUpdate();
	}
	private static final String insertRecognitionStatement = "insert into revenueRecognitions values (?,?,?)";
}