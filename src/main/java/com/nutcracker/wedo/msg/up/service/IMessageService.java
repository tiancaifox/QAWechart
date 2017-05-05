package com.nutcracker.wedo.msg.up.service;

import com.nutcracker.wedo.msg.base.bo.IMessage;

/**消息服务接口
 * Created by huh on 2017/3/16.
 */
public interface IMessageService<T extends IMessage> {

    /**
     * 保存消息
     * @param message 消息
     */
    void insert (T message);

    /**
     * 删除消息
     * @param id 消息id
     */
    void delete(Long id);

    /**
     * 更新消息
     * @param message 消息
     */
    void update(T message);

    /**
     * 获取消息内容
     * @param id 消息id
     * @return 消息
     */
    T getById(Long id);
}
