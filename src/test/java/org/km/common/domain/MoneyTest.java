package org.km.common.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class MoneyTest {

	@Test
	public void 分割のテスト_3で分割し割り切れない場合_余剰は配列の最後に足される() {
		Money money = Money.dollars(100);
		Money[] result = money.allocate(3);
		assertThat(result.length, is(3));
		assertThat(result[0].amount(), is(new BigDecimal(33)));
		assertThat(result[1].amount(), is(new BigDecimal(33)));
		assertThat(result[2].amount(), is(new BigDecimal(34)));
	}
}
