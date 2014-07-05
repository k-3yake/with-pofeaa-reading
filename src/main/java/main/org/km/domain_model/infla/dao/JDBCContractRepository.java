package org.km.domain_model.infla.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.km.domain_model.domin.Contract;
import org.km.domain_model.domin.ContractRepository;
import org.km.domain_model.domin.Product;
import org.km.domain_model.domin.RevenueRecognition;
import org.km.transaction_script.infla.AppException;
import org.km.values.MfDate;
import org.km.values.Money;

public class JDBCContractRepository implements ContractRepository {
	private Connection db;

	@Override
	public Contract find(long contractNumber) {
		try {
			ResultSet rs = findContract(contractNumber);
			rs.next();
			Product product = Product.newProduct(rs.getString("type"),
					rs.getString("name"));
			Money revenue = Money.dollars(rs.getBigDecimal("revenue"));
			MfDate recognitionDate = new MfDate(rs.getDate("dateSigned"));
			Contract contract = new Contract(product, revenue, recognitionDate);
			for (RevenueRecognition revenueRecognition : findRecognitions(contractNumber)) {
				Method addRevnueRecognitionMethod = contract.getClass().getDeclaredMethod("addRevnueRecognition");
				addRevnueRecognitionMethod.invoke(contract, revenueRecognition);
			}
			// リフレクションでidを設定。ドメインに関係ないsetterを作成しないため。
			getField(contract, "id").setLong(contract, rs.getLong("c.id"));
			return contract;
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	private Field getField(Object object, String filedName) {
		try {
			Field field = object.getClass().getDeclaredField(filedName);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	private ResultSet findContract(long contractID) throws SQLException {
		PreparedStatement stmt = db.prepareStatement(findContractsStatement);
		stmt.setLong(1, contractID);
		ResultSet resultSet = stmt.executeQuery();
		return resultSet;
	}

	private static final String findContractsStatement = "select * "
			+ " from contracts c, products p "
			+ " where id = ? and c.produc = p.ID";

	private List<RevenueRecognition> findRecognitions(long contractID)
			throws SQLException {
		PreparedStatement stmt = db.prepareStatement(findRecognitionsStatement);
		stmt.setLong(1, contractID);
		ResultSet rs = stmt.executeQuery();
		List<RevenueRecognition> result = new ArrayList<>();
		while (rs.next()) {
			Money amount = Money.dollars(rs.getInt("amount"));
			MfDate redognizedOn = new MfDate(rs.getDate("recognizedOn"));
			result.add(new RevenueRecognition(amount, redognizedOn));
		}
		return result;
	}

	private static final String findRecognitionsStatement = "select *"
			+ "from revenueRecognitions " + "where contract = ? ";

	@Override
	public void update(Contract contract) {
		// トランザクションスクリプトと処理内容を合わせるため、revenueRecognitionの追加のみ実装。
		// 本来ならコントラクトのrevenue再計算等に対応する処理が必要
		try {
			List<RevenueRecognition> revenueRecognitions =
					(List<RevenueRecognition>) getField(contract, "revenueRecognitions").get(contract);
			for (RevenueRecognition revenueRecognition : revenueRecognitions) {
				long id = getField(contract, "id").getLong(contract);
				Money money = revenueRecognition.getAmount();
				MfDate recognitionDate = (MfDate) getField(revenueRecognition,
						"date").get(revenueRecognition);
				insertRecognition(id, money, recognitionDate);
			}
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	public void insertRecognition(long contractNumber, Money money,
			MfDate recognitionDate) throws SQLException {
		PreparedStatement stmt = db
				.prepareStatement(insertRecognitionStatement);
		stmt.setLong(1, contractNumber);
		stmt.setBigDecimal(2, money.amount());
		stmt.setDate(3, recognitionDate.toSqlDate());
		stmt.executeUpdate();
	}
	private static final String insertRecognitionStatement = "insert into revenueRecognitions values (?,?,?)";
}