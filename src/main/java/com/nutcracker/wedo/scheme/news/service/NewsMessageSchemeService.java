package com.nutcracker.wedo.scheme.news.service;

import com.nutcracker.wedo.msg.up.bo.NewsItem;
import com.nutcracker.wedo.msg.up.bo.NewsMessage;
import com.nutcracker.wedo.scheme.common.service.IPromotionSchemeService;
import com.nutcracker.wedo.scheme.news.bo.NewsMessageScheme;

/**图文消息推广方案服务
 * Created by huh on 2017/3/16.
 */
public interface NewsMessageSchemeService extends IPromotionSchemeService<NewsMessageScheme> {

    /**
     * 存储消息条目
     * @param item
     */
    void saveNewsMessageItem(NewsItem item, String schemeName);

    /**
     * 通过消息条目id获取消息条目
     * @param itemId 消息条目id
     * @return NewsItem
     */
    NewsItem getItemById(Long itemId);

    /**
     * 通过消息id获取消息
     * @param messageId 消息messageId
     * @return NewsMessage
     */
    NewsMessage getMessageById(Long messageId);
}


