package org.km.transaction_script.infla;

import java.sql.SQLException;

public class AppException extends RuntimeException{

	public AppException(Exception e) {
		super(e);
	}

	public AppException(String string) {
		// TODO Auto-generated constructor stub
	}

}
