package com.nutcracker.wedo.msg.base.bo;

import lombok.Data;

/**
 * 微信消息封装
 * Created by huh on 2017/2/21.
 */
@Data
public class MessageWrap {
    /**
     * 消息头
     */
    private MessageHead head;
    /**
     * 消息
     */
    private IMessage message;
    /**
     * 构造
     *
     * @param head
     * @param message
     */
    public MessageWrap(MessageHead head, IMessage message){
        super();
        this.head = head;
        this.message = message;
    }
}
