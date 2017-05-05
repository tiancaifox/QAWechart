package com.nutcracker.wedo.msg.base.bo;

/**
 * 微信消息核心接口
 * Created by huh on 2017/2/21.
 */
public interface IMessage {
    /**
     * 输出消息内容，用于日志记录
     * @return 组织好的消息内容字符串
     */
    String getMessageContent();
}
