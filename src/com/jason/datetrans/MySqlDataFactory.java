package com.jason.datetrans;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MySqlDataFactory {

	public String getDBNameString(Object object, int start) {
		// 获取类对象
		@SuppressWarnings("rawtypes")
		Class objectClass = object.getClass();
		Field[] fs = objectClass.getDeclaredFields();
		StringBuffer nameBuf = new StringBuffer();
		try {
			for (int i = start; i < fs.length; i++) {
				Field f = fs[i];
				f.setAccessible(true);
				nameBuf.append(f.getName());
				if (i != fs.length - 1)
					nameBuf.append(", ");
			}
			System.out.println("SqlTitle：" + nameBuf);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nameBuf.toString();
	}

	public String getDBValueString(Object object, int start) {
		// 获取类对象
		@SuppressWarnings("rawtypes")
		Class objectClass = object.getClass();
		Field[] fs = objectClass.getDeclaredFields();
		StringBuffer valueBuf = new StringBuffer();
		try {
			for (int i = start; i < fs.length; i++) {
				Field f = fs[i];
				f.setAccessible(true);
				Object val = f.get(object);

				String type = f.getType().toString();
				if (type.endsWith("String")) {
					valueBuf.append("'").append(val).append("'");
				} else if (type.endsWith("long")) {
					valueBuf.append(val);
				} else if (type.endsWith("int")) {
					valueBuf.append(val);
				}
				if (i != fs.length - 1)
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

	public void insertM3u8(String name, String url) {
		Connection conn = DBUtil.getConnection();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("insert into m3u8(name, url, insertTime) values('" + name + "', '" + url + "','"+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())+"')");
		try {
			Statement ps = conn.createStatement();
			System.out.println(sqlBuffer.toString());
			ps.executeUpdate(sqlBuffer.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean selectM3u8ByName(String name) {
		Connection conn = DBUtil.getConnection();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select name from m3u8 where name='" + name + "'");
		try {
			Statement ps = conn.createStatement();
			System.out.println(sqlBuffer.toString());
			ResultSet rs = ps.executeQuery(sqlBuffer.toString());
			if(rs.next()) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void main(String[] args) {
		MySqlDataFactory dataFactory = new MySqlDataFactory();
		dataFactory.selectM3u8ByName("aaa");
		dataFactory.insertM3u8("aaa","bbb");
		dataFactory.insertM3u8("aaa1","bbb");
//		System.out.println(dataFactory.selectM3u8ByName("bbb"));
		DBUtil.closeConnection(DBUtil.getConnection());
	}

}
