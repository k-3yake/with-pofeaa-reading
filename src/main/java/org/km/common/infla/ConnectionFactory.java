package org.km.common.infla;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
	public static final String driver = "org.h2.Driver";
//	public static final String url = "jdbc:h2:~/test";
	public static final String url = "jdbc:h2:tcp://localhost/~/test";
	public static final String user = "sa";
	public static final String password = "sa";
	
	public static Connection getConnection(){
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", user);
	    connectionProps.put("password", password);
	    Connection conn;
		try {
			conn = DriverManager.getConnection(url,connectionProps);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	    System.out.println("Connected to database");
	    return conn;
	}
}