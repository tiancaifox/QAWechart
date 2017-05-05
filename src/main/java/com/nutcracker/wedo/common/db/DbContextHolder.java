package com.nutcracker.wedo.common.db;

/**
 * 动态切换数据源
 * Created by huh on 2017/1/17.
 */
public class DbContextHolder {

	/**
	 * 每个线程都有一个副本
	 */
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	/**
	 * 设置当前数据库
	 * @param dbType
	 */
	public static void setDbType(String dbType)
	{
		contextHolder.set(dbType);
	}

	/**
	 * 取得当前数据源
	 * @return
	 */
	public static String getDbType()
	{
		String str = (String) contextHolder.get();
		return str;
	}

	/**
	 * 清除上下文数据
	 */
	public static void clearDbType()
	{
		contextHolder.remove();
	}
}
