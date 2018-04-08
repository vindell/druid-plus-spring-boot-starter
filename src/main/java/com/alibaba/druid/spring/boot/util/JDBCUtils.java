package com.alibaba.druid.spring.boot.util;

import java.sql.DriverManager;

import com.alibaba.druid.spring.boot.ds.DataSourceEnum;

public class JDBCUtils {

	public static boolean testConnection(String className,String URL,String Username,String Password) {
		try {
			Class.forName(className).newInstance();
			DriverManager.getConnection(URL, Username, Password);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String getDriverClass(String dbtype) {
		DataSourceEnum dsEnum = DataSourceEnum.valueOfIgnoreCase(dbtype);
		return dsEnum != null ? dsEnum.getDriverClass() : null; 
	}
	
}
