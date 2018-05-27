package com.alibaba.druid.spring.boot.ds;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.ReflectionUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.DruidProperties;
import com.alibaba.druid.spring.boot.util.DruidDataSourceUtils;


@SuppressWarnings("unchecked")
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

	/**
     * 用于在维护数据源时保证不会被其他线程修改
     */
    private static Lock lock = new ReentrantLock();
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected static Field targetDataSourcesField = ReflectionUtils.findField(DynamicRoutingDataSource.class,
			"targetDataSources");
	protected static Field resolvedDataSourcesField = ReflectionUtils.findField(DynamicRoutingDataSource.class,
			"resolvedDataSources");
	
	@Override
	protected Object determineCurrentLookupKey() {
		 logger.info("Current DataSource is [{}]", DynamicDataSourceContextHolder.getDataSourceKey());
		return DynamicDataSourceContextHolder.getDataSourceKey();
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
	
	public void setTargetDataSource(DataSourceProperties properties, DruidProperties druidProperties,
			String name, String url, String username, String password) {

		lock.lock();
		
		try {
			
			// 动态创建Druid数据源
			DruidDataSource targetDataSource = DruidDataSourceUtils.createDataSource(properties, druidProperties, name, 
					url, username, password);

			getTargetDataSources().put(name, targetDataSource);
			
			// reset resolvedDataSources
			this.afterPropertiesSet();
			
		} finally {
            lock.unlock();
        }
		
	}
	
	/**
	 * 为动态数据源设置新的数据源目标源集
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
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
	 * 为动态数据源设置新的数据源目标源集合
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
	 * @param targetDataSources
	 */
	public void setNewTargetDataSources(Map<Object, Object> targetDataSources) {
		
		lock.lock();
		
		try {
			
			getTargetDataSources().putAll(targetDataSources);
			// reset resolvedDataSources
			this.afterPropertiesSet();
			
		} finally {
	        lock.unlock();
	    }
	}

	public void removeTargetDataSource(String name) {
		
		lock.lock();
		
		try {
			
			getTargetDataSources().remove(name);
			// reset resolvedDataSources
			this.afterPropertiesSet();
		
		} finally {
	        lock.unlock();
	    }
	}
	
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		getTargetDataSources().forEach((key, value) -> {
			Object lookupKey = resolveSpecifiedLookupKey(key);
			DynamicDataSourceContextHolder.dataSourceKeys.add(lookupKey);
		});
	}
	
}
