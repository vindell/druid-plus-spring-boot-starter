package com.alibaba.druid.spring.boot.util;

import java.sql.DriverManager;

import com.alibaba.druid.spring.boot.ds.JDBCDriverEnum;

public class JDBCUtils {

	/**
	 * 
	 * @description	： TODO
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
	 * @date 		：2017年11月27日 下午8:49:25
	 * @param className
	 * @param URL
	 * @param Username
	 * @param Password
	 * @return
	 */
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
		JDBCDriverEnum driverEnum = JDBCDriverEnum.driver(dbtype);
		return driverEnum != null ? driverEnum.getDriverClass() : null; 
	}
	
}
