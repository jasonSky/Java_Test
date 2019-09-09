package com.jason.datetrans;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	
	//oracle
	//db_driver = oracle.jdbc.driver.OracleDriver
	//db_url = jdbc:oracle:thin:@192.168.33.235:1521:mdmtest
	//public static String driver_class = PropertiesUtil.getOptValue("db_driver");
	public static String mysql_driver = "com.mysql.jdbc.Driver";
	public static String oracle_driver = "oracle.jdbc.driver.OracleDriver";
	public static String db2_driver = "com.ibm.db2.jcc.DB2Driver";
	public static String mysql_url = "jdbc:mysql://45.78.43.205:3309/SpiderData?characterEncoding=utf8&autoReconnect=true";
	public static String USERNAME = "root";
	public static String PASSWORD = "!QAZ2wsx";
	public static Connection conn = null;
	
	static{
		String driver_class = "";
		try {
			driver_class = mysql_driver;
			Class.forName(driver_class);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection(){
		try {
			if(conn != null && !conn.isClosed()) {
				return conn;
			}else {
				conn = DriverManager.getConnection(mysql_url, USERNAME, PASSWORD);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void closeConnection(Connection conn){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
