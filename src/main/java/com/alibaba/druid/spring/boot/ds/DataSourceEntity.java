/**
 * <p>Coyright (R) 2014 正方软件股份有限公司。<p>
 */
package com.alibaba.druid.spring.boot.ds;

public class DataSourceEntity {

	/**
	 * 数据库名称
	 */
	private String name;
	/**
	 * 数据库连接地址
	 */
	private String url;
	/**
	 * 数据库账号
	 */
	private String username;
	/**
	 * 数据库密码
	 */
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
