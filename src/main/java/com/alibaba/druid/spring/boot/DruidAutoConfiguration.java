package com.alibaba.druid.spring.boot;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.ds.DataSourceContextHolder;
import com.alibaba.druid.spring.boot.util.DruidDataSourceUtils;

/**
 * DruidAutoConfiguration配置类
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
@Configuration
@ConditionalOnClass(com.alibaba.druid.pool.DruidDataSource.class)
@ConditionalOnProperty(name = "spring.datasource.druid.enabled" , havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({ DruidProperties.class, DataSourceProperties.class})
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
public class DruidAutoConfiguration {
	
	@Bean(DataSourceContextHolder.DEFAULT_DATASOURCE)
	@ConditionalOnMissingBean(AbstractRoutingDataSource.class)
	@Primary
	public DruidDataSource druidDataSource(DataSourceProperties properties, DruidProperties druidProperties) {
		return DruidDataSourceUtils.createDataSource(properties, druidProperties, druidProperties.getName(), properties.determineUrl(),
				properties.determineUsername(), properties.determinePassword());
	}

}
