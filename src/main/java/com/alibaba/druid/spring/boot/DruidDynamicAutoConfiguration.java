package com.alibaba.druid.spring.boot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.ds.DataSourceContextHolder;
import com.alibaba.druid.spring.boot.ds.DynamicDataSource;
import com.alibaba.druid.spring.boot.ds.DynamicDataSourceSetting;
import com.alibaba.druid.spring.boot.util.DruidDataSourceUtils;


@Configuration
@ConditionalOnClass(com.alibaba.druid.pool.DruidDataSource.class)
@ConditionalOnProperty(name = "spring.datasource.druid.enabled" , havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({ DruidDynamicProperties.class, DataSourceProperties.class })
@AutoConfigureBefore(DruidAutoConfiguration.class)
public class DruidDynamicAutoConfiguration {

	@Autowired(required = false) 
	@Qualifier("targetDataSources") 
	protected Map<Object, Object> targetDataSources;
	
	@Bean("targetDataSources")
	@ConditionalOnProperty(prefix = DruidDynamicProperties.PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
	public Map<Object, Object> targetDataSources() {
		return new HashMap<Object, Object>();
	}
	
	/**
	 * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
	 * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
	 */
	@Bean(DataSourceContextHolder.DEFAULT_DATASOURCE)
	@ConditionalOnProperty(prefix = DruidDynamicProperties.PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
	@Primary
	public DynamicDataSource dynamicDataSource(DataSourceProperties properties, DruidProperties druidProperties,
			DruidDynamicProperties dynamicProperties) {
		
		//基于配置文件的动态数据源信息
		if (!CollectionUtils.isEmpty(dynamicProperties.getDataSources())) {
			for (DynamicDataSourceSetting dsSetting : dynamicProperties.getDataSources()) {
				// 动态创建Druid数据源
				targetDataSources.put(dsSetting.getName(), DruidDataSourceUtils.createDataSource(properties, druidProperties,
						dsSetting.getName(), dsSetting.getUrl(), dsSetting.getUsername(), dsSetting.getPassword()));
			}
		}

		// 动态数据源支持
		DynamicDataSource dataSource = new DynamicDataSource();
		dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
		
		// 默认的数据源
		DruidDataSource defaultTargetDataSource = DruidDataSourceUtils.createDataSource(properties, druidProperties, druidProperties.getName(), properties.determineUrl(),
				properties.determineUsername(), properties.determinePassword());
		
		dataSource.setDefaultTargetDataSource(defaultTargetDataSource);// 默认的datasource设置为myTestDbDataSource
		
		return dataSource;
	}

	
}
