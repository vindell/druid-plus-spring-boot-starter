package com.alibaba.druid.spring.boot;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

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
import com.alibaba.druid.spring.boot.ds.DynamicDataSourceSetting;
import com.alibaba.druid.spring.boot.ds.DynamicRoutingDataSource;
import com.alibaba.druid.spring.boot.util.DruidDataSourceUtils;

/**
 * DruidAutoConfiguration配置类
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
@Configuration
@ConditionalOnClass(com.alibaba.druid.pool.DruidDataSource.class)
@ConditionalOnProperty(prefix = DruidProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({ DruidProperties.class, DataSourceProperties.class})
@AutoConfigureBefore(name = {
	"org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
	"com.baomidou.mybatisplus.spring.boot.starter.MybatisPlusAutoConfiguration"
})
public class DruidAutoConfiguration {
	
	/*
	 * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
	 * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
	 */
	@Bean
	@Primary
	public DataSource dataSource(DataSourceProperties properties, DruidProperties druidProperties) {
		
		// 启用动态数据源
		if(druidProperties.isDynamic()) {
		
			Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
			
			//基于配置文件的动态数据源信息
			if (!CollectionUtils.isEmpty(druidProperties.getSlaves())) {
				for (DynamicDataSourceSetting slave : druidProperties.getSlaves()) {
					// 动态创建Druid数据源
					targetDataSources.put(slave.getName(), DruidDataSourceUtils.createDataSource(properties, druidProperties,
							slave.getName(), slave.getUrl(), slave.getUsername(), slave.getPassword()));
				}
			}

			// 动态数据源支持
			DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
			dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
			
			// 默认的数据源
			DruidDataSource masterDataSource = DruidDataSourceUtils.createDataSource(properties, druidProperties, druidProperties.getName(), properties.determineUrl(),
					properties.determineUsername(), properties.determinePassword());
			
			dataSource.setDefaultTargetDataSource(masterDataSource);// 默认的datasource设置为myTestDbDataSource
			
			return dataSource;
			
		}
		
		return DruidDataSourceUtils.createDataSource(properties, druidProperties, druidProperties.getName(), properties.determineUrl(),
				properties.determineUsername(), properties.determinePassword());
	}

}
