package com.nutcracker.wedo.common.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态切换数据源
 * Created by huh on 2017/1/17.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	/**
	 * 取得当前使用那个数据源。
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		return DbContextHolder.getDbType();
	}

}
