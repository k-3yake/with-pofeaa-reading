package org.km.common.domain;

import static org.hamcrest.CoreMatchers.is;import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import jp.classmethod.testing.database.DbUnitTester;
import jp.classmethod.testing.database.Fixture;
import jp.classmethod.testing.database.YamlDataSet;

import org.dbunit.dataset.IDataSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.km.common.domain.MfDate;
import org.km.common.domain.Money;
import org.km.common.infla.ConnectionFactory;
//import org.km.transaction_script.domain.RecognitionService;
import org.km.domain_model.application.RecognitionService;


@RunWith(Enclosed.class)
public class IRecognitionServiceTest {
	
	@RunWith(Enclosed.class)
	public static class 収益取得のテスト {
		
		@Fixture(resources={"product.yaml","contract-word.yaml","revenue_recognition-word.yaml"})
		public static class ワードの契約の場合{
			@Rule public DbUnitTester tester = createDbUnitTester();
			@Test
			public void 契約の収益取得テスト() throws ParseException {
				doTest("2014-01-01 01:01:00", 100);
			}
		}
	
		@Fixture(resources={"product.yaml","contract-spredsheet.yaml","revenue_recognition-spredsheet.yaml"})
		public static class 表計算の契約の場合{
			@Rule  public DbUnitTester tester = createDbUnitTester();
			@Test
			public void 特定日が一回目の収益認識前の場合_収益無し() throws ParseException {
				doTest("2013-12-31 23:59:59", 0);
			}
			@Test
			public void 特定日が二回目の収益認識前の場合_１回目の収益を取得() throws ParseException {
				doTest("2014-03-01 23:59:59", 33);
			}
			@Test
			public void 特定日が三回目の収益認識前の場合_１から２回目の収益を取得() throws ParseException {
				doTest("2014-03-31 23:59:59", 66);
			}
			@Test
			public void 特定日が三回目の収益認識後の場合_１から３回目の収益を取得() throws ParseException {
				doTest("2014-04-01 00:00:00", 100);
			}
		}
		
		private static void doTest(String asOf, int expectAmount) throws ParseException {
			IRecognitionService recognitionService = getService();
			MfDate asof = new MfDate(new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(asOf).getTime()));
			Money recognizedRevenue = recognitionService.recognizedRevenue(10, asof);
			assertThat(recognizedRevenue.amount(), is(new BigDecimal(expectAmount)));
		}
	}
	
	@RunWith(Enclosed.class)
	public static class 収益算出のテスト{
		
		@Fixture(resources={"product.yaml","contract-word.yaml","revenue_recognition-zero.yaml"})
		public static class ワードの契約の場合{
			@Rule 
			public DbUnitTester tester = createDbUnitTester();
			@Test
			public void 契約日の収益認識を算出する() throws Exception {
				doTest("revenue_recognition-word.yaml", tester);
			}
		}

		@Fixture(resources={"product.yaml","contract-spredsheet.yaml","revenue_recognition-zero.yaml"})
		public static class 表計算の契約の場合{
			@Rule 
			public DbUnitTester tester = createDbUnitTester();
			@Test
			public void 契約日および契約日から60日90日の収益認識を算出する() throws Exception {
				doTest("revenue_recognition-spredsheet.yaml", tester);
			}
		}

		@Fixture(resources={"product.yaml","contract-db.yaml","revenue_recognition-zero.yaml"})
		public static class DBの契約の場合{
			@Rule 
			public DbUnitTester tester = createDbUnitTester();
			@Test
			public void 契約日および契約日から60日90日の収益認識を算出する() throws Exception {
				doTest("revenue_recognition-db.yaml", tester);
			}
		}
		
		private static void doTest(String expect, DbUnitTester dbUnitTester) throws Exception {
			IRecognitionService recognitionService = getService();
			recognitionService.calculateRevenueRecognitions(10);
			IDataSet expected = YamlDataSet.load(IRecognitionServiceTest.class.getResourceAsStream(expect));
			dbUnitTester.verifyTable("revenue_recognition", expected);
		}
	}

	static DbUnitTester createDbUnitTester() {
		return DbUnitTester.forJdbc(ConnectionFactory.driver, ConnectionFactory.url)
	    .username(ConnectionFactory.user)
	    .password(ConnectionFactory.password)
	    .create();	
	}
	
	static RecognitionService getService() {
		return new RecognitionService();
	}
}