package com.alibaba.druid.spring.boot.ds;

import java.lang.reflect.Field;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.ReflectionUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.DruidProperties;
import com.alibaba.druid.spring.boot.util.DruidDataSourceUtils;


@SuppressWarnings("unchecked")
public class DynamicDataSource extends AbstractRoutingDataSource {

	protected static Field targetDataSourcesField = ReflectionUtils.findField(DynamicDataSource.class,
			"targetDataSources");
	protected static Field resolvedDataSourcesField = ReflectionUtils.findField(DynamicDataSource.class,
			"resolvedDataSources");

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceContextHolder.getDatabaseName();
	}

	public Map<Object, Object> getTargetDataSources() {
		targetDataSourcesField.setAccessible(true);
		Object targetDataSources = ReflectionUtils.getField(targetDataSourcesField, this);
		targetDataSourcesField.setAccessible(false);
		return (Map<Object, Object>) targetDataSources;
	}

	public Map<Object, DataSource> getResolvedDataSources() {
		resolvedDataSourcesField.setAccessible(true);
		Object resolvedDataSources = ReflectionUtils.getField(resolvedDataSourcesField, this);
		resolvedDataSourcesField.setAccessible(false);
		return (Map<Object, DataSource>) resolvedDataSources;
	}

	/**
	 * 
	 * @description	： 为动态数据源设置新的数据源目标源
	 * @author 		： 万大龙（743）
	 * @date 		：2017年10月17日 下午2:34:30
	 * @param properties
	 * @param druidProperties
	 * @param name
	 * @param url
	 * @param username
	 * @param password
	 */
	public void setTargetDataSource(DataSourceProperties properties, DruidProperties druidProperties,
			String name, String url, String username, String password) {

		// 动态创建Druid数据源
		DruidDataSource targetDataSource = DruidDataSourceUtils.createDataSource(properties, druidProperties,
				name + "DataSource", url, username, password);

		getTargetDataSources().put(name, targetDataSource);

		Object lookupKey = resolveSpecifiedLookupKey(name);
		DataSource dataSource = resolveSpecifiedDataSource(targetDataSource);
		getResolvedDataSources().put(lookupKey, dataSource);
		
	}
	
	/**
	 * 
	 * @description	： 为动态数据源设置新的数据源目标源集
	 * @author 		： 万大龙（743）
	 * @date 		：2017年10月17日 下午2:34:21
	 * @param properties
	 * @param druidProperties
	 * @param dsSetting
	 */
	public void setTargetDataSource(DataSourceProperties properties, DruidProperties druidProperties,
			DynamicDataSourceSetting dsSetting) {
		this.setTargetDataSource(properties, druidProperties, druidProperties.getName(), properties.determineUrl(),
				properties.determineUsername(), properties.determinePassword());
	}

	/**
	 * @description	： 为动态数据源设置新的数据源目标源集合
	 * @author 		： 万大龙（743）
	 * @date 		：2017年10月17日 下午2:33:39
	 * @param targetDataSources
	 */
	public void setNewTargetDataSources(Map<Object, Object> targetDataSources) {

		getTargetDataSources().putAll(targetDataSources);

		for (Map.Entry<Object, Object> entry : targetDataSources.entrySet()) {
			Object lookupKey = resolveSpecifiedLookupKey(entry.getKey());
			DataSource dataSource = resolveSpecifiedDataSource(entry.getValue());
			getResolvedDataSources().put(lookupKey, dataSource);
		}

	}

	public void removeTargetDataSource(String name) {

		getTargetDataSources().remove(name);
		Object lookupKey = resolveSpecifiedLookupKey(name);
		getResolvedDataSources().remove(lookupKey);
		
	}
}
