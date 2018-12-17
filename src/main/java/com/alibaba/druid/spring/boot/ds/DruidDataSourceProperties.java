package com.alibaba.druid.spring.boot.ds;

import java.util.Properties;

import org.springframework.util.StringUtils;

import com.alibaba.druid.pool.DruidAbstractDataSource;
import com.alibaba.druid.util.JdbcUtils;

@SuppressWarnings("serial")
public class DruidDataSourceProperties {

	/** 基本属性 url、user、password */

	private String driverClassName;
	/**
	 * 配置这个属性的意义在于，如果存在多个数据源，监控的时候可以通过名字来区分开来。如果没有配置，将会生成一个名字，格式是：”DataSource-” +
	 * System.identityHashCode(this)
	 */
	private String name;
	/** jdbcUrl: 连接数据库的url，不同数据库不一样 */
	private String url;
	/** username: 连接数据库的用户名 */
	private String username;
	/** password: 连接数据库的密码 */
	private String password;

	/** connectionProperties: 连接数据库的额外参数 */
	private Properties connectionProperties = new Properties() {
		{
			put("druid.stat.mergeSql", "true");
			put("druid.stat.slowSqlMillis", "5000");
		}
	};

	private String dbType;

	/** druid 连接池参数 */

	/** 配置初始化大小、最小、最大 连接池数量 */

	/**
	 * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒;有两个含义：1) Destroy线程会检测连接的间隔时间 2)
	 * testWhileIdle的判断依据，详细看testWhileIdle属性的说明
	 */
	private Long timeBetweenEvictionRunsMillis = 60000L;
	private Integer maxOpenPreparedStatements = -1;
	private Integer numTestsPerEvictionRun = DruidAbstractDataSource.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;

	/** 超过时间限制是否回收 */
	private boolean removeAbandoned = true;
	/** 超过时间限制多久触发回收逻辑，单位：毫秒 ，180000毫秒=3分钟 */
	private Long removeAbandonedTimeoutMillis = 180 * 1000L;
	private boolean logAbandoned;
	private Integer connectionErrorRetryAttempts = 1;
	private boolean breakAfterAcquireFailure = false;

	private boolean clearFiltersEnable;
	private String connectProperties;
	/** maxActive: 连接池最大连接数量 */
	private Integer maxActive = 50;
	/**
	 * 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
	 */
	private Long maxWait = 60000L;
	private Integer maxWaitThreadCount = -1;

	private Integer maxIdle = DruidAbstractDataSource.DEFAULT_MAX_IDLE;
	/** minIdle: 连接池最小连接数量 */
	private Integer minIdle = 5;
	/**
	 * 要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100
	 */
	private Integer maxPoolPreparedStatementPerConnectionSize = 20;

	/** 配置一个连接在池中最大生存的时间，单位:毫秒 */
	private Long maxEvictableIdleTimeMillis;
	/** 配置一个连接在池中最小生存的时间，单位：毫秒 */
	private Long minEvictableIdleTimeMillis = 300000L;

	private Integer notFullTimeoutRetryCount = 0;

	private boolean failFast;
	/**
	 * Druid的监控统计功能:属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有： #监控统计用的filter:stat
	 * #日志用的filter:slf4j #防御SQL注入的filter:wall
	 * 开启Druid的监控统计功能，mergeStat代替stat表示sql合并,wall表示防御SQL注入攻击
	 */
	private String filters = "mergeStat,wall,slf4j";
	private Long phyTimeoutMillis;
	private Long phyMaxUseCount = -1L;
	/**
	 * 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql5.5以下的版本中没有PSCache功能，建议关闭掉。5.5及以上版本有PSCache，建议开启。
	 */
	private boolean poolPreparedStatements = true;
	private boolean resetStatEnable;
	private Integer statSqlMaxSize;
	private boolean sharePreparedStatements = false;
	/**
	 * 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
	 */
	private boolean testWhileIdle = true;
	/** 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
	private boolean testOnBorrow = false;
	/** 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能 */
	private boolean testOnReturn = false;
	private Long timeBetweenConnectErrorMillis = DruidAbstractDataSource.DEFAULT_TIME_BETWEEN_CONNECT_ERROR_MILLIS;
	private Long timeBetweenLogStatsMillis;
	private Long transactionThresholdMillis = 0L;
	private boolean keepAlive;
	private boolean killWhenSocketReadTimeout;
	private String initConnectionSqls;
	private boolean initGlobalVariants;
	private boolean initVariants;
	/** initialSize: 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时 */
	private Integer initialSize = 15;
	private boolean useUnfairLock;
	private boolean useGlobalDataSourceStat;
	/**
	 * 用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会其作用。
	 */
	private String validationQuery = "SELECT 1";
	private Integer validationQueryTimeout = -1;

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
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

	public String getDbType() {
		return StringUtils.hasText(dbType) ? dbType : JdbcUtils.getDbType(this.getUrl(), null);
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public Long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public Integer getMaxOpenPreparedStatements() {
		return maxOpenPreparedStatements;
	}

	public void setMaxOpenPreparedStatements(Integer maxOpenPreparedStatements) {
		this.maxOpenPreparedStatements = maxOpenPreparedStatements;
	}

	public Integer getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}

	public void setNumTestsPerEvictionRun(Integer numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public boolean isRemoveAbandoned() {
		return removeAbandoned;
	}

	public void setRemoveAbandoned(boolean removeAbandoned) {
		this.removeAbandoned = removeAbandoned;
	}

	public Long getRemoveAbandonedTimeoutMillis() {
		return removeAbandonedTimeoutMillis;
	}

	public void setRemoveAbandonedTimeoutMillis(Long removeAbandonedTimeoutMillis) {
		this.removeAbandonedTimeoutMillis = removeAbandonedTimeoutMillis;
	}

	public boolean isLogAbandoned() {
		return logAbandoned;
	}

	public void setLogAbandoned(boolean logAbandoned) {
		this.logAbandoned = logAbandoned;
	}

	public Integer getConnectionErrorRetryAttempts() {
		return connectionErrorRetryAttempts;
	}

	public void setConnectionErrorRetryAttempts(Integer connectionErrorRetryAttempts) {
		this.connectionErrorRetryAttempts = connectionErrorRetryAttempts;
	}

	public boolean isBreakAfterAcquireFailure() {
		return breakAfterAcquireFailure;
	}

	public void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) {
		this.breakAfterAcquireFailure = breakAfterAcquireFailure;
	}

	public boolean isClearFiltersEnable() {
		return clearFiltersEnable;
	}

	public void setClearFiltersEnable(boolean clearFiltersEnable) {
		this.clearFiltersEnable = clearFiltersEnable;
	}

	public String getConnectProperties() {
		return connectProperties;
	}

	public void setConnectProperties(String connectProperties) {
		this.connectProperties = connectProperties;
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

	public Integer getMaxWaitThreadCount() {
		return maxWaitThreadCount;
	}

	public void setMaxWaitThreadCount(Integer maxWaitThreadCount) {
		this.maxWaitThreadCount = maxWaitThreadCount;
	}

	public Integer getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public Integer getMaxPoolPreparedStatementPerConnectionSize() {
		return maxPoolPreparedStatementPerConnectionSize;
	}

	public void setMaxPoolPreparedStatementPerConnectionSize(Integer maxPoolPreparedStatementPerConnectionSize) {
		this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
	}

	public Long getMaxEvictableIdleTimeMillis() {
		return maxEvictableIdleTimeMillis;
	}

	public void setMaxEvictableIdleTimeMillis(Long maxEvictableIdleTimeMillis) {
		this.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
	}

	public Long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(Long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public Integer getNotFullTimeoutRetryCount() {
		return notFullTimeoutRetryCount;
	}

	public void setNotFullTimeoutRetryCount(Integer notFullTimeoutRetryCount) {
		this.notFullTimeoutRetryCount = notFullTimeoutRetryCount;
	}

	public boolean isFailFast() {
		return failFast;
	}

	public void setFailFast(boolean failFast) {
		this.failFast = failFast;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public Long getPhyTimeoutMillis() {
		return phyTimeoutMillis;
	}

	public void setPhyTimeoutMillis(Long phyTimeoutMillis) {
		this.phyTimeoutMillis = phyTimeoutMillis;
	}

	public Long getPhyMaxUseCount() {
		return phyMaxUseCount;
	}

	public void setPhyMaxUseCount(Long phyMaxUseCount) {
		this.phyMaxUseCount = phyMaxUseCount;
	}

	public boolean isPoolPreparedStatements() {
		return poolPreparedStatements;
	}

	public void setPoolPreparedStatements(boolean poolPreparedStatements) {
		this.poolPreparedStatements = poolPreparedStatements;
	}

	public boolean isResetStatEnable() {
		return resetStatEnable;
	}

	public void setResetStatEnable(boolean resetStatEnable) {
		this.resetStatEnable = resetStatEnable;
	}

	public Integer getStatSqlMaxSize() {
		return statSqlMaxSize;
	}

	public void setStatSqlMaxSize(Integer statSqlMaxSize) {
		this.statSqlMaxSize = statSqlMaxSize;
	}

	public boolean isSharePreparedStatements() {
		return sharePreparedStatements;
	}

	public void setSharePreparedStatements(boolean sharePreparedStatements) {
		this.sharePreparedStatements = sharePreparedStatements;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public Long getTimeBetweenConnectErrorMillis() {
		return timeBetweenConnectErrorMillis;
	}

	public void setTimeBetweenConnectErrorMillis(Long timeBetweenConnectErrorMillis) {
		this.timeBetweenConnectErrorMillis = timeBetweenConnectErrorMillis;
	}

	public Long getTimeBetweenLogStatsMillis() {
		return timeBetweenLogStatsMillis;
	}

	public void setTimeBetweenLogStatsMillis(Long timeBetweenLogStatsMillis) {
		this.timeBetweenLogStatsMillis = timeBetweenLogStatsMillis;
	}

	public Long getTransactionThresholdMillis() {
		return transactionThresholdMillis;
	}

	public void setTransactionThresholdMillis(Long transactionThresholdMillis) {
		this.transactionThresholdMillis = transactionThresholdMillis;
	}

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public boolean isKillWhenSocketReadTimeout() {
		return killWhenSocketReadTimeout;
	}

	public void setKillWhenSocketReadTimeout(boolean killWhenSocketReadTimeout) {
		this.killWhenSocketReadTimeout = killWhenSocketReadTimeout;
	}

	public String getInitConnectionSqls() {
		return initConnectionSqls;
	}

	public void setInitConnectionSqls(String initConnectionSqls) {
		this.initConnectionSqls = initConnectionSqls;
	}

	public boolean isInitGlobalVariants() {
		return initGlobalVariants;
	}

	public void setInitGlobalVariants(boolean initGlobalVariants) {
		this.initGlobalVariants = initGlobalVariants;
	}

	public boolean isInitVariants() {
		return initVariants;
	}

	public void setInitVariants(boolean initVariants) {
		this.initVariants = initVariants;
	}

	public Integer getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}

	public boolean isUseUnfairLock() {
		return useUnfairLock;
	}

	public void setUseUnfairLock(boolean useUnfairLock) {
		this.useUnfairLock = useUnfairLock;
	}

	public boolean isUseGlobalDataSourceStat() {
		return useGlobalDataSourceStat;
	}

	public void setUseGlobalDataSourceStat(boolean useGlobalDataSourceStat) {
		this.useGlobalDataSourceStat = useGlobalDataSourceStat;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public Integer getValidationQueryTimeout() {
		return validationQueryTimeout;
	}

	public void setValidationQueryTimeout(Integer validationQueryTimeout) {
		this.validationQueryTimeout = validationQueryTimeout;
	}

	public Properties toProperties() {

		Properties properties = new Properties();

		notNullAdd(properties, "clearFiltersEnable", this.clearFiltersEnable);
		notNullAdd(properties, "connectProperties", this.connectProperties);
		notNullAdd(properties, "driverClassName", this.driverClassName);
		notNullAdd(properties, "maxActive", this.maxActive);
		notNullAdd(properties, "maxEvictableIdleTimeMillis", this.maxEvictableIdleTimeMillis);
		notNullAdd(properties, "maxPoolPreparedStatementPerConnectionSize",
				this.maxPoolPreparedStatementPerConnectionSize);
		notNullAdd(properties, "maxWaitThreadCount", this.maxWaitThreadCount);
		notNullAdd(properties, "minIdle", this.minIdle);
		notNullAdd(properties, "minEvictableIdleTimeMillis", this.minEvictableIdleTimeMillis);
		notNullAdd(properties, "name", this.name);
		notNullAdd(properties, "notFullTimeoutRetryCount", this.notFullTimeoutRetryCount);
		notNullAdd(properties, "failFast", this.failFast);
		notNullAdd(properties, "filters", this.filters);
		notNullAdd(properties, "initConnectionSqls", this.initConnectionSqls);
		notNullAdd(properties, "initVariants", this.initVariants);
		notNullAdd(properties, "initGlobalVariants", this.initGlobalVariants);
		notNullAdd(properties, "initialSize", this.initialSize);
		notNullAdd(properties, "keepAlive", this.keepAlive);
		notNullAdd(properties, "killWhenSocketReadTimeout", this.killWhenSocketReadTimeout);
		notNullAdd(properties, "password", this.password);
		notNullAdd(properties, "phyMaxUseCount", this.phyMaxUseCount);
		notNullAdd(properties, "phyTimeoutMillis", this.phyTimeoutMillis);
		notNullAdd(properties, "poolPreparedStatements", this.poolPreparedStatements);
		notNullAdd(properties, "resetStatEnable", this.resetStatEnable);
		notNullAdd(properties, "stat.sql.MaxSize", this.statSqlMaxSize);
		notNullAdd(properties, "testOnBorrow", this.testOnBorrow);
		notNullAdd(properties, "testWhileIdle", this.testWhileIdle);
		notNullAdd(properties, "timeBetweenEvictionRunsMillis", this.timeBetweenEvictionRunsMillis);
		notNullAdd(properties, "timeBetweenLogStatsMillis", this.timeBetweenLogStatsMillis);
		notNullAdd(properties, "url", this.url);
		notNullAdd(properties, "username", this.username);
		notNullAdd(properties, "useUnfairLock", this.useUnfairLock);
		notNullAdd(properties, "useGlobalDataSourceStat", this.useGlobalDataSourceStat);
		notNullAdd(properties, "validationQuery", this.validationQuery);
		return properties;
	}

	protected void notNullAdd(Properties properties, String key, Object value) {
		if (value != null) {
			properties.setProperty("druid." + key, value.toString());
		}
	}

}