package com.nutcracker.wedo.msg.up.dao;

import com.nutcracker.wedo.common.annotation.NullDeal;
import com.nutcracker.wedo.common.util.NullValueUtil;
import com.nutcracker.wedo.msg.up.bo.NewsItem;
import org.junit.Test;
import utils.AbstractTestCase;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by huh on 2017/1/19.
 */
public class NewsItemDaoTest extends AbstractTestCase {

	@Resource
	private NewsItemDao newsItemDao;

	@Test
	public void insertTest() {
		NewsItem temp = new NewsItem();
		//temp.setAuthor("huhao");
		temp.setCreateTime(new Date());
		temp.setDetail("详情");
		temp.setDescription("description");
		temp.setIsFirstItem("0");
		temp.setTitle("题目");
		temp.setMessageId((long)999);
		temp.setPicId((long)999);
		temp.setPicUrl("http://");
		temp.setUrl("http://");
		NullValueUtil.dealNullValue(temp);
		newsItemDao.insert(temp);
	}
}
