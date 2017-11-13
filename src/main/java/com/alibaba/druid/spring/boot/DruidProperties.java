package com.alibaba.druid.spring.boot;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;

@SuppressWarnings("serial")
@ConfigurationProperties(DruidProperties.PREFIX)
public class DruidProperties {

	public static final String PREFIX = "spring.datasource.druid";
	
	/**
	 * Enable Druid.
	 */
	private boolean enabled = false;
	
	/** 基本属性 url、user、password */
	
	protected String driverClassName;
	/**
	 * 配置这个属性的意义在于，如果存在多个数据源，监控的时候可以通过名字来区分开来。如果没有配置，将会生成一个名字，格式是：”DataSource-” + System.identityHashCode(this)
	 */
	protected String name;
	/** jdbcUrl: 连接数据库的url，不同数据库不一样 */
	protected String url;
	/** username: 连接数据库的用户名 */
	protected String username;
	/** password: 连接数据库的密码 */
	protected String password;
	/** connectionProperties: 连接数据库的额外参数 */
	protected Properties connectionProperties = new Properties() {
		{
			put("druid.stat.mergeSql", "true");
			put("druid.stat.slowSqlMillis", "5000");
		}
	};

	/** druid 连接池参数 */

	/** 配置初始化大小、最小、最大 连接池数量 */

	/** initialSize: 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时 */
	protected Integer initialSize = 15;
	/** minIdle: 连接池最小连接数量 */
	protected Integer minIdle = 5;
	/** maxActive: 连接池最大连接数量 */
	protected Integer maxActive = 50;
	/** 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。 */
	protected Long maxWait = 60000L;
	/**
	 * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒;有两个含义：1) Destroy线程会检测连接的间隔时间 2)
	 * testWhileIdle的判断依据，详细看testWhileIdle属性的说明
	 */
	protected Long timeBetweenEvictionRunsMillis = 60000L;
	/** 配置一个连接在池中最小生存的时间，单位：毫秒 */
	protected Long minEvictableIdleTimeMillis = 300000L;
	/** 配置一个连接在池中最大生存的时间，单位:毫秒 */
	protected Long maxEvictableIdleTimeMillis;
	/** 超过时间限制是否回收 */
	protected Boolean removeAbandoned = true;
	/** 超过时间限制多长，单位：毫秒 ，180000毫秒=3分钟 */
	protected Long removeAbandonedTimeoutMillis = 180000L;
	/** 用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会其作用。 */
	protected String validationQuery = "SELECT 1";
	/** 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。 */
	protected Boolean testWhileIdle = true;
	/** 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
	protected Boolean testOnBorrow = false;
	/** 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能 */
	protected Boolean testOnReturn = false;
	/** 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql5.5以下的版本中没有PSCache功能，建议关闭掉。5.5及以上版本有PSCache，建议开启。 */
	protected Boolean poolPreparedStatements = true;
	/** 要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100 */
	protected Integer maxPoolPreparedStatementPerConnectionSize = 20;
	/**
	 * Druid的监控统计功能:属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有： #监控统计用的filter:stat
	 * #日志用的filter:slf4j #防御SQL注入的filter:wall
	 * 开启Druid的监控统计功能，mergeStat代替stat表示sql合并,wall表示防御SQL注入攻击
	 */
	protected String filters = "mergeStat,wall,slf4j";
	protected Boolean proxyFilter = false;
	
	protected Boolean useGlobalDataSourceStat;
	protected Long timeBetweenLogStatsMillis;
	protected Integer statSqlMaxSize;
	protected Boolean clearFiltersEnable;
	protected Integer notFullTimeoutRetryCount;
	protected Integer maxWaitThreadCount;
	protected Boolean failFast;
	protected Boolean resetStatEnable;
	
	protected Boolean keepAlive;
	protected Long phyTimeoutMillis;
	protected Boolean initVariants;
	protected Boolean initGlobalVariants;
	protected Boolean useUnfairLock;
	protected Boolean killWhenSocketReadTimeout;
	
	@NestedConfigurationProperty
	protected WallFilter wallFilter;

	@NestedConfigurationProperty
	protected StatFilter statFilter;

	@NestedConfigurationProperty
	protected Slf4jLogFilter logFilter;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getFailFast() {
		return failFast;
	}

	public void setFailFast(Boolean failFast) {
		this.failFast = failFast;
	}

	public Boolean getResetStatEnable() {
		return resetStatEnable;
	}

	public void setResetStatEnable(Boolean resetStatEnable) {
		this.resetStatEnable = resetStatEnable;
	}

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

	public Properties getConnectionProperties() {
		return connectionProperties;
	}

	public void setConnectionProperties(Properties connectionProperties) {
		this.connectionProperties = connectionProperties;
	}

	public Integer getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public Integer getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}

	public Long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(Long maxWait) {
		this.maxWait = maxWait;
	}

	public Long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public Long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(Long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public Long getMaxEvictableIdleTimeMillis() {
		return maxEvictableIdleTimeMillis;
	}

	public void setMaxEvictableIdleTimeMillis(Long maxEvictableIdleTimeMillis) {
		this.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
	}

	public Boolean getRemoveAbandoned() {
		return removeAbandoned;
	}

	public void setRemoveAbandoned(Boolean removeAbandoned) {
		this.removeAbandoned = removeAbandoned;
	}

	public Long getRemoveAbandonedTimeoutMillis() {
		return removeAbandonedTimeoutMillis;
	}

	public void setRemoveAbandonedTimeoutMillis(Long removeAbandonedTimeoutMillis) {
		this.removeAbandonedTimeoutMillis = removeAbandonedTimeoutMillis;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public Boolean getTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(Boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public Boolean getTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(Boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public Boolean getPoolPreparedStatements() {
		return poolPreparedStatements;
	}

	public void setPoolPreparedStatements(Boolean poolPreparedStatements) {
		this.poolPreparedStatements = poolPreparedStatements;
	}

	public Integer getMaxPoolPreparedStatementPerConnectionSize() {
		return maxPoolPreparedStatementPerConnectionSize;
	}

	public void setMaxPoolPreparedStatementPerConnectionSize(Integer maxPoolPreparedStatementPerConnectionSize) {
		this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}
	
	public Boolean getProxyFilter() {
		return proxyFilter;
	}

	public void setProxyFilter(Boolean proxyFilter) {
		this.proxyFilter = proxyFilter;
	}

	public Boolean getUseGlobalDataSourceStat() {
		return useGlobalDataSourceStat;
	}

	public void setUseGlobalDataSourceStat(Boolean useGlobalDataSourceStat) {
		this.useGlobalDataSourceStat = useGlobalDataSourceStat;
	}

	public Long getTimeBetweenLogStatsMillis() {
		return timeBetweenLogStatsMillis;
	}

	public void setTimeBetweenLogStatsMillis(Long timeBetweenLogStatsMillis) {
		this.timeBetweenLogStatsMillis = timeBetweenLogStatsMillis;
	}

	public Integer getStatSqlMaxSize() {
		return statSqlMaxSize;
	}

	public void setStatSqlMaxSize(Integer statSqlMaxSize) {
		this.statSqlMaxSize = statSqlMaxSize;
	}

	public Boolean getClearFiltersEnable() {
		return clearFiltersEnable;
	}

	public void setClearFiltersEnable(Boolean clearFiltersEnable) {
		this.clearFiltersEnable = clearFiltersEnable;
	}

	public Integer getNotFullTimeoutRetryCount() {
		return notFullTimeoutRetryCount;
	}

	public void setNotFullTimeoutRetryCount(Integer notFullTimeoutRetryCount) {
		this.notFullTimeoutRetryCount = notFullTimeoutRetryCount;
	}

	public Integer getMaxWaitThreadCount() {
		return maxWaitThreadCount;
	}

	public void setMaxWaitThreadCount(Integer maxWaitThreadCount) {
		this.maxWaitThreadCount = maxWaitThreadCount;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public Boolean getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(Boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public Long getPhyTimeoutMillis() {
		return phyTimeoutMillis;
	}

	public void setPhyTimeoutMillis(Long phyTimeoutMillis) {
		this.phyTimeoutMillis = phyTimeoutMillis;
	}

	public Boolean getInitVariants() {
		return initVariants;
	}

	public void setInitVariants(Boolean initVariants) {
		this.initVariants = initVariants;
	}

	public Boolean getInitGlobalVariants() {
		return initGlobalVariants;
	}

	public void setInitGlobalVariants(Boolean initGlobalVariants) {
		this.initGlobalVariants = initGlobalVariants;
	}

	public Boolean getUseUnfairLock() {
		return useUnfairLock;
	}

	public void setUseUnfairLock(Boolean useUnfairLock) {
		this.useUnfairLock = useUnfairLock;
	}

	public Boolean getKillWhenSocketReadTimeout() {
		return killWhenSocketReadTimeout;
	}

	public void setKillWhenSocketReadTimeout(Boolean killWhenSocketReadTimeout) {
		this.killWhenSocketReadTimeout = killWhenSocketReadTimeout;
	}

	public WallFilter getWallFilter() {
		return wallFilter;
	}

	public void setWallFilter(WallFilter wallFilter) {
		this.wallFilter = wallFilter;
	}

	public StatFilter getStatFilter() {
		return statFilter;
	}

	public void setStatFilter(StatFilter statFilter) {
		this.statFilter = statFilter;
	}

	public Slf4jLogFilter getLogFilter() {
		return logFilter;
	}

	public void setLogFilter(Slf4jLogFilter logFilter) {
		this.logFilter = logFilter;
	}

	public Properties toProperties() {
		
		Properties properties = new Properties();
		
		//notNullAdd(properties, "driverClassName", this.driverClassName);
		notNullAdd(properties, "testWhileIdle", this.testWhileIdle);
		notNullAdd(properties, "testOnBorrow", this.testOnBorrow);
		notNullAdd(properties, "validationQuery", this.validationQuery);
		notNullAdd(properties, "useGlobalDataSourceStat", this.useGlobalDataSourceStat);
		notNullAdd(properties, "filters", this.filters);
		notNullAdd(properties, "timeBetweenLogStatsMillis", this.timeBetweenLogStatsMillis);
		notNullAdd(properties, "stat.sql.MaxSize", this.statSqlMaxSize);
		notNullAdd(properties, "clearFiltersEnable", this.clearFiltersEnable);
		notNullAdd(properties, "resetStatEnable", this.resetStatEnable);
		notNullAdd(properties, "notFullTimeoutRetryCount", this.notFullTimeoutRetryCount);
		notNullAdd(properties, "maxWaitThreadCount", this.maxWaitThreadCount);
		notNullAdd(properties, "failFast", this.failFast);
		notNullAdd(properties, "phyTimeoutMillis", this.phyTimeoutMillis);
		notNullAdd(properties, "minEvictableIdleTimeMillis", this.minEvictableIdleTimeMillis);
		notNullAdd(properties, "maxEvictableIdleTimeMillis", this.maxEvictableIdleTimeMillis);
		notNullAdd(properties, "keepAlive", this.keepAlive);
		notNullAdd(properties, "poolPreparedStatements", this.poolPreparedStatements);
		notNullAdd(properties, "initVariants", this.initVariants);
		notNullAdd(properties, "initGlobalVariants", this.initGlobalVariants);
		notNullAdd(properties, "useUnfairLock", this.useUnfairLock);
		notNullAdd(properties, "initialSize", this.initialSize);
		notNullAdd(properties, "killWhenSocketReadTimeout", this.killWhenSocketReadTimeout);
		
		return properties;
	}

	protected void notNullAdd(Properties properties, String key, Object value) {
		if (value != null) {
			properties.setProperty("druid." + key, value.toString());
		}
	}

}