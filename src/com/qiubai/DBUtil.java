package com.qiubai;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	
	//oracle
	//db_driver = oracle.jdbc.driver.OracleDriver
	//db_url = jdbc:oracle:thin:@192.168.33.235:1521:mdmtest
	public static String mysql_driver = "com.mysql.jdbc.Driver";
	public static String oracle_driver = "oracle.jdbc.driver.OracleDriver";
	public static String db2_driver = "com.ibm.db2.jcc.DB2Driver";
	public static String mysql_url = "jdbc:mysql://45.78.43.205:3309/data_ipt?useUnicode=true&characterEncoding=utf8";
	public static String driver_class = "";
	public static String USERNAME = "root";
	public static String PASSWORD = "!QAZ2wsx";
	static Connection conn = null;
	
	static{
		try {
				driver_class = mysql_driver;
				Class.forName(driver_class);
			//System.out.println("driver_class:" + driver_class);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		try {
			if (conn != null && !conn.isClosed())
				return conn;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String url = "";
		try {
				url = mysql_url;
			//System.out.println("url:" + url);
			conn = DriverManager.getConnection(url, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void closeConnection(Connection conn){
		//DB connection will be closed after testsuite()

		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static void closeConnection() {
		if(conn != null){
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	public static String getDBNameString(Object object){
		
		// 获取类对象
		@SuppressWarnings("rawtypes")
		Class objectClass = object.getClass();
		Field[] fs = objectClass.getDeclaredFields();
		StringBuffer nameBuf = new StringBuffer();
		try {
			for (int i=1; i<fs.length; i++){
				Field f = fs[i];
				f.setAccessible(true);
				nameBuf.append(f.getName());
				if (i != fs.length-1)
					nameBuf.append(", ");
			}
			System.out.println("SqlTitle：" + nameBuf);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nameBuf.toString();
	}
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	public static String getDBValueString(Object object){
		
		// 获取类对象
		@SuppressWarnings("rawtypes")
		Class objectClass = object.getClass();
		Field[] fs = objectClass.getDeclaredFields();
		StringBuffer valueBuf = new StringBuffer();
		try {
			for (int i=1; i<fs.length; i++){
				Field f = fs[i];
				f.setAccessible(true);
				Object val = f.get(object);
				
				String type  = f.getType().toString();
				if (type.endsWith("String")){
					valueBuf.append("'").append(val).append("'");
				}
				else if (type.endsWith("long")){
					valueBuf.append(val);
				}
				else if (type.endsWith("int")){
					valueBuf.append(val);
				}
				if (i != fs.length-1)
					valueBuf.append(", ");
			}
			System.out.println("SqlVaules：" + valueBuf);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valueBuf.toString();
	}

	/**
	 * 向数据库插入数据
	 * @param object
	 * @return
	 */
	public static long addObject(Connection conn, Object object){
		long id = 0;
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("insert into " + object.getClass().getSimpleName());
		String titles =  " (" + DBUtil.getDBNameString(object) + ") values ( " + DBUtil.getDBValueString(object) + ")"; 
		sqlBuffer.append(titles);
		
		System.out.println("sqlBuffer:" + sqlBuffer);
		try {
			PreparedStatement pstmt = (PreparedStatement)conn.prepareStatement(sqlBuffer.toString(),Statement.RETURN_GENERATED_KEYS);
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				id = rs.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
		return id;
	}
	
}
