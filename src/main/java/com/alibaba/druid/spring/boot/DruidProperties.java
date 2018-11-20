package com.alibaba.druid.spring.boot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.alibaba.druid.spring.boot.ds.DruidDataSourceProperties;

@ConfigurationProperties(DruidProperties.PREFIX)
public class DruidProperties extends DruidDataSourceProperties {

	public static final String PREFIX = "spring.datasource.druid";
	
	/**
	 * Enable Druid.
	 */
	private boolean enabled = false;
	/**
	 * Enable Dynamic Routing.
	 */
	private boolean routable = false;
	/** 
	 * Datasource slaves 
	 */
	private List<DruidDataSourceProperties> slaves = new ArrayList<DruidDataSourceProperties>();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isRoutable() {
		return routable;
	}

	public void setRoutable(boolean routable) {
		this.routable = routable;
	}

	public List<DruidDataSourceProperties> getSlaves() {
		return slaves;
	}

	public void setSlaves(List<DruidDataSourceProperties> slaves) {
		this.slaves = slaves;
	}
	
}