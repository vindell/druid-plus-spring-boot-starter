package com.alibaba.druid.spring.boot.ds;

import java.sql.DriverManager;

public class JDBCUtils {

	/**
	 * 
	 * @description	： TODO
	 * @author 		： 万大龙（743）
	 * @date 		：2017年10月12日 上午11:23:44
	 * @param className
	 * @param URL  
	 * @param Username 用户名
	 * @param Password 密码
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
