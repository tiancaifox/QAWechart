package com.nutcracker.wedo.msg.up.service;

import com.nutcracker.wedo.msg.up.bo.NewsItem;
import com.nutcracker.wedo.msg.up.bo.NewsMessage;

/**图文消息服务
 * Created by huh on 2017/3/16.
 */
public interface NewsMessageService extends IMessageService<NewsMessage> {

    /**
     * 通过方案id获取消息内容
     *
     * @param schemeId 方案id
     * @return 消息
     */
    NewsMessage getBySchemeId(Long schemeId);

    /**
     * 通过消息条目id获取消息条目
     *
     * @param itemId
     * @return NewsItem
     */
    NewsItem getItemById(Long itemId);

    /**
     * 更新消息条目
     *
     * @param newsItem
     */
    void updateItem(NewsItem newsItem);
}

