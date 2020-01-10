package com.qiubai;

import java.io.BufferedWriter;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBOperator {
	
	/**
	 * 通过数据库表名创建相应对象
	 * @param dbName
	 */
	public void creatObjectFromDB(String dbName){
		Connection con = DBUtil.getConnection();
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select COLUMN_NAME, COLUMN_TYPE, COLUMN_COMMENT from information_schema.columns where table_name='" + dbName + "' " + "and IS_NULLABLE = 'NO'");
		//System.out.println(sqlStr);
		try {
			PreparedStatement ps = con.prepareStatement(sqlStr.toString());
			ResultSet rs = ps.executeQuery();
			
			File file = createFile(dbName);
			writeDataToFile(file, "package com.mdm.dbOperate.dbObject;  \n", true);
			writeDataToFile(file, "public class " + dbName + "{ \n", true);
			
			while (rs.next()) {

				String name = rs.getString(1);
				String type = rs.getString(2);
				String comment = rs.getString(3);
				
				writeDataToFile(file, "\t public \t", false);
				if (type.contains("varchare"))
					writeDataToFile(file, " String \t", false);
				else if (type.contains("bigint(20)"))
					writeDataToFile(file, "long \t", false);
				else if (type.contains("int"))
					writeDataToFile(file, "int \t", false);
				else
					writeDataToFile(file, "String \t", false);
				
				writeDataToFile(file, name + ";" + "\t//" + comment, true);
					
			}
			writeDataToFile(file, "\n }", true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(con);
		}
	}
	
	/**
	 * 通过表名创建数据库操作对象
	 * @param dbName	表名
	 * @param allKey	是否创建所以字段属性		true：创建所有字段属性	false：只创建不允许为空字段的属性
	 */
	public void creatObjectFromDB(String dbName, boolean allKey){
		Connection con = DBUtil.getConnection();
		StringBuffer sqlStr = new StringBuffer();
		if (allKey){
			sqlStr.append("select COLUMN_NAME, COLUMN_TYPE, COLUMN_COMMENT from information_schema.columns where table_name='" + dbName + "'");
		}
		else{
			sqlStr.append("select COLUMN_NAME, COLUMN_TYPE, COLUMN_COMMENT from information_schema.columns where table_name='" + dbName + "' " + "and IS_NULLABLE = 'NO'");
		}
		
		//System.out.println(sqlStr);
		try {
			PreparedStatement ps = con.prepareStatement(sqlStr.toString());
			ResultSet rs = ps.executeQuery();
			
			File file = createFile(dbName);
			writeDataToFile(file, "package com.qiubai;  \n", true);
			writeDataToFile(file, "public class " + dbName + " implements Cloneable { \n", true);
			writeDataToFile(file, "\t @Override", true);
			writeDataToFile(file, "\t public Object clone() throws CloneNotSupportedException {", true);
			writeDataToFile(file, "\t\t return super.clone();", true);
			writeDataToFile(file, "\t }", true);
			writeDataToFile(file, "\t ", true);
			while (rs.next()) {

				String name = rs.getString(1);
				String type = rs.getString(2);
				String comment = rs.getString(3);
				
				writeDataToFile(file, "\t public \t", false);
				if (type.contains("varchare"))
					writeDataToFile(file, " String \t", false);
				else if (type.contains("bigint(20)"))
					writeDataToFile(file, "long \t", false);
				else if (type.contains("int"))
					writeDataToFile(file, "int \t", false);
				else
					writeDataToFile(file, "String \t", false);
				
				writeDataToFile(file, name + ";" + "\t//" + comment, true);
					
			}
			writeDataToFile(file, "\n }", true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(con);
		}
	}
	
	
	/**
	 * 向数据库插入数据
	 * @param object
	 * @return
	 */
	public long addObject(Object object){
		return addObject(object, false);
	}
	public long addObject(Object object, boolean containsId){
		long id = 0;
		Connection conn = DBUtil.getConnection();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("insert into " + object.getClass().getSimpleName());
		String titles =  " (" + getDBNameString(object, containsId) + ") values ( " + getDBValueString(object, containsId) + ")"; 
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
		}
		return id;
	}
	
	
	/**
	 * 从数据库表查询对象
	 * @param objectClass	对象类
	 * @param condition		查询条件
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object selectObjectFromDB(Class objectClass, String condition){
		
		Object obj = null;//实例化对象
		Field[] fs = objectClass.getDeclaredFields();
		StringBuffer nameBuf = new StringBuffer();
		try {
			for (int i=0; i<fs.length; i++){
				Field field = fs[i];
				field.setAccessible(true);
				nameBuf.append(field.getName());
				if (i != fs.length-1)
					nameBuf.append(", ");
			}
			System.out.println("SqlTitle：" + nameBuf);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Connection conn = DBUtil.getConnection();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select " + nameBuf + " from " + objectClass.getSimpleName());
		sqlBuffer.append(" where " + condition);
		
		try {
			PreparedStatement ps = conn.prepareStatement(sqlBuffer.toString());
			ResultSet rs = ps.executeQuery();
			try{
				obj = objectClass.newInstance();
				while (rs.next()) {
					for (int i=0; i<fs.length; i++){
						Field field = fs[i];
						field.setAccessible(true);
						String type = field.getType().toString();
						String value = rs.getString(i+1);
						if (type.endsWith("String")){
							field.set(obj, value);
						}
						else if (type.endsWith("long") || type.endsWith("Long")){
							field.set(obj, Long.parseLong(value));
						}
						else if (type.endsWith("int") || type.endsWith("Integer")){
							field.set(obj, Integer.parseInt(rs.getString(i+1)));
						}
						else 
							field.set(obj, value);
					}
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
		return obj;
	}
	
	/**
	 * 从数据库删除所有数据
//	 * @param tableName		表名
	 */
	public void deleteDataFromTable(String tableName){
		System.out.print(tableName);
		Connection conn = DBUtil.getConnection();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("delete from " + tableName);
		try {
			Statement statement = conn.createStatement();
			System.out.println(sqlBuffer.toString());
			statement.executeUpdate(sqlBuffer.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
	}
	
	/**
	 * 从数据库删除一条数据
	 * @param tableName		表名
	 * @param dataId		数据id
	 */
	public void deleteDataFromTable(String tableName, long dataId){
		Connection conn = DBUtil.getConnection();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("delete from " + tableName);
		sqlBuffer.append(" where id = " + dataId);
		try {
			Statement statement = conn.createStatement();
			System.out.println(sqlBuffer.toString());
			statement.executeUpdate(sqlBuffer.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
	}
	
	/**
	 * 从数据库删除一条数据
	 * @param tableName		表名
	 * @param condition	 	删除条件
	 */
	public void deleteDataFromTable(String tableName, String condition){
		Connection conn = DBUtil.getConnection();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("delete from " + tableName);
		sqlBuffer.append(" where " + condition);
		try {
			Statement statement = conn.createStatement();
			System.out.println(sqlBuffer.toString());
			statement.executeUpdate(sqlBuffer.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
	}
	
	/**
	 * 从数据库更新一条数据
	 * @param tableName		表名
	 * @param condition	 	更新条件
	 */
	public void updateDataFromTable(String tableName, String condition){
		Connection conn = DBUtil.getConnection();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("update " + tableName + " "+condition);
		try {
			Statement statement = conn.createStatement();
			System.out.println(sqlBuffer.toString());
			statement.executeUpdate(sqlBuffer.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 创建对象文件，不覆盖文件
	 * @param fileName
	 * @return
	 */
	public File createFile(String fileName){
		String path = System.getProperty("user.dir") + "/src/com/qiubai/";
		File file = new File(path + fileName+".java");
		try{
			if (file.exists())
				file.delete();
			file.createNewFile();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	/**
	 * 向文件写入数据
	 * @param fileName
	 * @param data
	 * @param newLine	是否换行
	 */
	public void writeDataToFile(File fileName, String data, boolean newLine){
		BufferedWriter fw = null;
		try {
			fw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName, true), "UTF-8"));
			fw.append(data);
			if (newLine)
				fw.newLine();
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	public String getDBNameString(Object object, boolean containsId){
		
		// 获取类对象
		@SuppressWarnings("rawtypes")
		Class objectClass = object.getClass();
		Field[] fs = objectClass.getDeclaredFields();
		StringBuffer nameBuf = new StringBuffer();
		try {
			int flag = 1;
			if(containsId){
				flag = 0;
			}
			for (int i=flag; i<fs.length; i++){
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
	public String getDBValueString(Object object, boolean containsId){
		
		// 获取类对象
		@SuppressWarnings("rawtypes")
		Class objectClass = object.getClass();
		Field[] fs = objectClass.getDeclaredFields();
		StringBuffer valueBuf = new StringBuffer();
		try {
			int flag = 1;
			if(containsId){
				flag = 0;
			}
			for (int i=flag; i<fs.length; i++){
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
}
