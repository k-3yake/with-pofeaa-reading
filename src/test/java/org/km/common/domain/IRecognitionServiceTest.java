package org.km.common.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import jp.classmethod.testing.database.DbUnitTester;
import jp.classmethod.testing.database.Fixture;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.km.common.domain.MfDate;
import org.km.common.domain.Money;
import org.km.common.infla.ConnectionFactory;
import org.km.transaction_script.domain.RecognitionService;

@RunWith(Enclosed.class)
public class IRecognitionServiceTest {
	
	
	@Fixture(resources={"product.yaml","contract-word.yaml"})
	public static class ワードの契約の場合{
		@Rule public DbUnitTester tester = createDbUnitTester();
		@Test
		public void 契約の収益取得テスト() throws ParseException {
			doTest("2014-01-01 01:01:00", 100);
		}
	}

	@Fixture(resources={"product.yaml","contract-spredsheet.yaml"})
	public static class 表計算の契約の場合{
		@Rule  public DbUnitTester tester = createDbUnitTester();
		@Test
		public void 特定日が一回目の収益認識前の場合() throws ParseException {
			doTest("2013-12-31 23:59:59", 0);
		}
		@Test
		public void 特定日が二回目の収益認識前の場合() throws ParseException {
			doTest("2014-02-28 23:59:59", 33);
		}
		@Test
		public void 特定日が三回目の収益認識前の場合() throws ParseException {
			doTest("2014-05-31 23:59:59", 66);
		}
		@Test
		public void 三回目の収益認識後の場合() throws ParseException {
			doTest("2014-06-01 00:00:01", 100);
		}
	}
	
	static DbUnitTester createDbUnitTester() {
		return DbUnitTester.forJdbc(ConnectionFactory.driver, ConnectionFactory.url)
	    .username(ConnectionFactory.user)
	    .password(ConnectionFactory.password)
	    .create();
	}
	
	private static void doTest(String asOf, int expectAmount) throws ParseException {
		IRecognitionService recognitionService = new RecognitionService();
		MfDate asof = new MfDate(new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(asOf).getTime()));
		Money recognizedRevenue = recognitionService.recognizedRevenue(10, asof);
		assertThat(recognizedRevenue.amount(), is(new BigDecimal(expectAmount)));
	}
}