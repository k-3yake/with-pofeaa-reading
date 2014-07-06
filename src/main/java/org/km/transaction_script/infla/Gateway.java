package org.km.transaction_script.infla;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.km.common.domain.MfDate;
import org.km.common.domain.Money;
import org.km.common.infla.ConnectionFactory;

public class Gateway {
	private Connection db = ConnectionFactory.getConnection();
	
	public ResultSet findRecognitionsFor(long contractID,MfDate asof) throws SQLException{
		PreparedStatement stmt = db.prepareStatement(findRecognitionsStatement); 
		stmt.setLong(1, contractID);
		stmt.setDate(2, asof.toSqlDate());
		ResultSet resultSet = stmt.executeQuery();
		return resultSet;
	}
	private static final String findRecognitionsStatement =
			"select amount "
			+ " from revenue_recognition "
			+ " where contract = ? and recognized_on <= ?";
	
	public ResultSet findContract(long contractID) throws SQLException{
		PreparedStatement stmt = db.prepareStatement(findContractsStatement );
		stmt.setLong(1, contractID);
		ResultSet resultSet = stmt.executeQuery();
		return resultSet;
	}
	private static final String findContractsStatement = 
			"select * "
			+ " from contract c, product p "
			+ " where c.id = ? and c.product = p.ID";
	
	
	public void insertRecognition(long contractNumber, Money money,	MfDate recognitionDate) throws SQLException {
		PreparedStatement stmt = db.prepareStatement(insertRecognitionStatement);
		stmt.setLong(1, contractNumber);
		stmt.setDate(2, recognitionDate.toSqlDate());
		stmt.setBigDecimal(3, money.amount());
		stmt.executeUpdate();
	}
	private static final String insertRecognitionStatement = "insert into revenue_recognition values (?,?,?)";
}