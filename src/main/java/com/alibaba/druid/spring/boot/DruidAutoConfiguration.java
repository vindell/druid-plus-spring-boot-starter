package com.alibaba.druid.spring.boot;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.ds.DataSourceContextHolder;
import com.alibaba.druid.spring.boot.util.DruidDataSourceUtils;

/**
 * 
 * @className ： DruidAutoConfiguration
 * @description ： 创建DruidAutoConfiguration配置类
 * @author ： <a href="https://github.com/vindell">vindell</a>
 * @date ： 2017年8月19日 下午4:39:23
 * @version V1.0
 */
@Configuration
@ConditionalOnClass({ DruidDataSource.class })
@ConditionalOnProperty(name = { "spring.datasource.druid.enabled" }, havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({ DruidProperties.class})
@AutoConfigureAfter(DruidDynamicAutoConfiguration.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class DruidAutoConfiguration {

	/**
	 * 
	 * @description ： 配置DruidDataSource
	 * @author ： 万大龙（743）
	 * @date ：2017年9月27日 下午2:05:09
	 * @param properties
	 * @param druidProperties
	 * @return
	 */
	@Bean(DataSourceContextHolder.DEFAULT_DATASOURCE)
	@ConditionalOnMissingBean(AbstractRoutingDataSource.class)
	@Primary
	public DruidDataSource druidDataSource(DataSourceProperties properties, DruidProperties druidProperties) {
		return DruidDataSourceUtils.createDataSource(properties, druidProperties, druidProperties.getName(), properties.determineUrl(),
				properties.determineUsername(), properties.determinePassword());
	}

}
