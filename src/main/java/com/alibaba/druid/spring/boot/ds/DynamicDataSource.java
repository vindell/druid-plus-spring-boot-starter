/**
 * <p>Coyright (R) 2014 正方软件股份有限公司。<p>
 */
package com.alibaba.druid.spring.boot.ds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceContextHolder.getDatabaseName();
	}

}
