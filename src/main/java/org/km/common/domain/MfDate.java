package org.km.common.domain;

import java.sql.Date;

public class MfDate {
	private java.util.Date date;
	
	public MfDate(){
		date = new java.util.Date();
	}

	public MfDate(Date date) {
		this.date = new Date(date.getTime());
	}

	public Date toSqlDate() {
		return new Date(date.getTime());
	}

	public MfDate addDays(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean after(MfDate date) {
		// TODO Auto-generated method stub
		return false;
	}
}