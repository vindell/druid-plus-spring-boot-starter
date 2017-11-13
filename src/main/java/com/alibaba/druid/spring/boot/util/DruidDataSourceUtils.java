package com.alibaba.druid.spring.boot.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DatabaseDriver;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.LogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.DruidProperties;
import com.alibaba.druid.wall.WallFilter;

public class DruidDataSourceUtils {

	public static <T extends DataSource> DruidDataSource createDataSource(DataSourceProperties properties, DruidProperties druidProperties,
			String name, String url, String username, String password) {
		// 创建 DruidDataSource 数据源对象
		DruidDataSource dataSource = createDataSource(properties, properties.getType());

		// 配置这个属性的意义在于，如果存在多个数据源，监控的时候可以通过名字来区分开来。如果没有配置，将会生成一个名字，格式是：”DataSource-” +
		// System.identityHashCode(this)
		if (StringUtils.isNotEmpty(name)) {
			dataSource.setName(name);
		}
		// 这一项可配可不配，如果不配置druid会根据url自动识别dbType，然后选择相应的driverClassName
		dataSource.setDriverClassName(properties.determineDriverClassName());
		// jdbcUrl: 连接数据库的url
		dataSource.setUrl(url);
		// username: 连接数据库的用户名
		dataSource.setUsername(username);
		// password: 连接数据库的密码
		dataSource.setPassword(password);

		// druid 连接池参数
		dataSource.configFromPropety(druidProperties.toProperties());
		//DruidDataSourceFactory.config(dataSource, druidProperties);
		
		// 配置初始化大小、最小、最大

		// minIdle: 最小空闲连接数量
		dataSource.setMinIdle(druidProperties.getMinIdle());
		// maxActive: 最大连接池数量
		dataSource.setMaxActive(druidProperties.getMaxActive());
		// initialSize: 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
		dataSource.setInitialSize(druidProperties.getInitialSize());
		// 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
		dataSource.setMaxWait(druidProperties.getMaxWait());
		// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒;有两个含义：1) Destroy线程会检测连接的间隔时间 2)
		// testWhileIdle的判断依据，详细看testWhileIdle属性的说明
		dataSource.setTimeBetweenEvictionRunsMillis(druidProperties.getTimeBetweenEvictionRunsMillis());
		// 配置一个连接在池中最小生存的时间，单位是毫秒
		dataSource.setMinEvictableIdleTimeMillis(druidProperties.getMinEvictableIdleTimeMillis());
		// 超过时间限制是否回收
		dataSource.setRemoveAbandoned(druidProperties.getRemoveAbandoned());
		// 超过时间限制多长，单位是毫秒
		dataSource.setRemoveAbandonedTimeoutMillis(druidProperties.getRemoveAbandonedTimeoutMillis());

		if (StringUtils.isNotEmpty(druidProperties.getValidationQuery())) {
			// 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
			dataSource.setTestOnBorrow(druidProperties.getTestOnBorrow());
			// 用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会其作用。
			dataSource.setValidationQuery(druidProperties.getValidationQuery());
		} else {
			DatabaseDriver databaseDriver = DatabaseDriver.fromJdbcUrl(properties.determineUrl());
			String validationQuery = databaseDriver.getValidationQuery();
			if (validationQuery != null) {
				dataSource.setTestOnBorrow(druidProperties.getTestOnBorrow());
				dataSource.setValidationQuery(validationQuery);
			}
		}

		// 申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。建议配置为true，不影响性能，并且保证安全性。
		dataSource.setTestWhileIdle(druidProperties.getTestWhileIdle());

		// 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
		dataSource.setTestOnReturn(druidProperties.getTestOnReturn());

		// 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql5.5以下的版本中没有PSCache功能，建议关闭掉。5.5及以上版本有PSCache，建议开启。
		dataSource.setPoolPreparedStatements(druidProperties.getPoolPreparedStatements());
		// 要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100
		dataSource.setMaxOpenPreparedStatements(druidProperties.getMaxPoolPreparedStatementPerConnectionSize());

		/*
		 * Druid的监控统计功能:属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有： #监控统计用的filter:stat
		 * #日志用的filter:log4j #防御SQL注入的filter:wall
		 */
		try {
			// 开启Druid的监控统计功能
			dataSource.setFilters(druidProperties.getFilters());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 指定过滤器
		if (BooleanUtils.isTrue(druidProperties.getProxyFilter())) {
			dataSource.setProxyFilters(getProxyFilters(druidProperties));
		}

		// 额外的链接参数
		dataSource.setConnectProperties(druidProperties.getConnectionProperties());

		// 注册对象到上下文
		// configurableBeanFactory.registerSingleton(dataSource.getName(), dataSource);

		return dataSource;
	}

	@SuppressWarnings("unchecked")
	public static <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
		return (T) properties.initializeDataSourceBuilder().type(type).build();
	}

	/**
	 * 
	 * @description ： TODO
	 * @author ： 万大龙（743）
	 * @date ：2017年10月16日 下午12:22:30
	 * @param druidProperties
	 * @return
	 * @see https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE-wallfilter
	 */
	public static List<Filter> getProxyFilters(DruidProperties druidProperties) {

		List<Filter> filters = new ArrayList<Filter>();

		WallFilter wallFilter = druidProperties.getWallFilter();
		if (null != wallFilter) {
			filters.add(wallFilter);
		}

		StatFilter statFilter = druidProperties.getStatFilter();
		if (null != statFilter) {
			filters.add(statFilter);
		}

		LogFilter logFilter = druidProperties.getLogFilter();
		if (null != logFilter) {
			filters.add(logFilter);
		}

		return filters;
	}

	
}
