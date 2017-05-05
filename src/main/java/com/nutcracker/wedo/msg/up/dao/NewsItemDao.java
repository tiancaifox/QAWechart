package com.nutcracker.wedo.msg.up.dao;

import com.nutcracker.wedo.common.annotation.SqlMapper;
import com.nutcracker.wedo.msg.up.bo.NewsItem;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Created by huh on 2017/1/19.
 */
@SqlMapper
public interface NewsItemDao {
	/**
	 * 保存消息
	 * @param item 消息条目
	 */
	void insert(@Param("item") NewsItem item);

	/**
	 * 批量保存消息
	 * @param items 消息条目列表
	 */
	void bathInsert(@Param("items") List<NewsItem> items);

	/**
	 * 通过消息条目id删除消息条目
	 * @param id 消息条目id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 通过消息id删除消息条目
	 * @param messageId 消息id
	 */
	void deleteByMessageId(@Param("messageId") Long messageId);

	/**
	 * 更新消息条目
	 */
	void update(@Param("item") NewsItem item);

	/**
	 * 通过id查询消息条目
	 * @param itemId 消息条目id
	 */
	NewsItem getById(@Param("itemId") Long itemId);

	/**
	 * 通过消息id获取消息条目
	 * @return 消息条目列表
	 */
	List<NewsItem> listByMessageId(@Param("messageId") Long messageId);
}
