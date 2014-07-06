package org.km.common.domain;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

	public MfDate addDays(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return new MfDate(new Date(calendar.getTime().getTime()));
	}

	public boolean after(MfDate date) {
		return getDate(this.date) > getDate(date.date);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MfDate other = (MfDate) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!getDate(date).equals(getDate(other.date)))
			return false;
		return true;
	}
	
	private Integer getDate(java.util.Date date){
		return new Integer(new SimpleDateFormat("yyyyMMdd").format(date));
	}
}