package org.km.domain_model.infla;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.km.common.domain.MfDate;
import org.km.common.domain.Money;
import org.km.common.infla.ConnectionFactory;
import org.km.domain_model.domain.Contract;
import org.km.domain_model.domain.ContractRepository;
import org.km.domain_model.domain.Product;
import org.km.domain_model.domain.RevenueRecognition;
import org.km.transaction_script.infla.AppException;

public class JDBCContractRepository implements ContractRepository {
	private Connection db = ConnectionFactory.getConnection();
	
	// ドメイン層ではアクセスさせたくないが、インフラ層でアクセスする必要のあるメソッド、フィールドにはリフレクションを使用

	@Override
	public Contract find(long contractNumber) {
		try {
			ResultSet rs = findContract(contractNumber);
			rs.next();
			Product product = Product.newProduct(rs.getString("type"),rs.getString("name"));
			Money revenue = Money.dollars(rs.getBigDecimal("amount"));
			MfDate recognitionDate = new MfDate(rs.getDate("when_signed"));
			Contract contract = new Contract(product, revenue, recognitionDate);
			for (RevenueRecognition revenueRecognition : findRevenueRecognitions(contractNumber)) {
				getMethod(contract, "addRevnueRecognition",RevenueRecognition.class).invoke(contract, revenueRecognition);
			}
			getField(contract, "id").set(contract, new Long(rs.getLong("id")));
			return contract;
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	private Method getMethod(Contract contract, String name,Class... parameterType)
			throws NoSuchMethodException {
		Method method = contract.getClass().getDeclaredMethod(name,parameterType);
		method.setAccessible(true);
		return method;
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
			+ " from contract c, product p "
			+ " where c.ID = ? and c.product = p.ID";

	private List<RevenueRecognition> findRevenueRecognitions(long contractID)
			throws SQLException {
		PreparedStatement stmt = db.prepareStatement(findRecognitionsStatement);
		stmt.setLong(1, contractID);
		ResultSet rs = stmt.executeQuery();
		List<RevenueRecognition> result = new ArrayList<>();
		while (rs.next()) {
			Money amount = Money.dollars(rs.getInt("amount"));
			MfDate redognizedOn = new MfDate(rs.getDate("recognized_on"));
			result.add(new RevenueRecognition(amount, redognizedOn));
		}
		return result;
	}
	private static final String findRecognitionsStatement 
			= "select *"
			+ "from revenue_recognition " 
			+ "where contract = ? ";

	@Override
	public void update(Contract contract) {
		// トランザクションスクリプトと処理内容を合わせるため、revenueRecognitionの追加のみ実装。
		// 本来ならコントラクトのrevenue再計算等に対応する処理が必要
		try {
			List<RevenueRecognition> revenueRecognitions =
					(List<RevenueRecognition>) getField(contract, "revenueRecognitions").get(contract);
			for (RevenueRecognition revenueRecognition : revenueRecognitions) {
				long id = (Long)getField(contract, "id").get(contract);
				Money money = revenueRecognition.getAmount();
				MfDate recognitionDate = (MfDate) getField(revenueRecognition,"date").get(revenueRecognition);
				insertRecognition(id, money, recognitionDate);
			}
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	public void insertRecognition(long contractNumber, Money money,MfDate recognitionDate) throws SQLException {
		PreparedStatement stmt = db.prepareStatement(insertRecognitionStatement);
		stmt.setLong(1, contractNumber);
		stmt.setDate(2, recognitionDate.toSqlDate());
		stmt.setBigDecimal(3, money.amount());
		stmt.executeUpdate();
	}
	private static final String insertRecognitionStatement = "insert into revenue_recognition values (?,?,?)";
}