package com.nutcracker.wedo.msg.base.bo;

import lombok.Data;

/**
 * 微信消息头
 * Created by huh on 2017/2/21.
 */
@Data
public class MessageHead {
    /**
     * 开发者微信号
     */
    private String toUserName;
    /**
     * 发送方账号
     */
    private String fromUserName;
    /**
     * 消息创建时间
     */
    private String createTime;
    /**
     * 消息类型：text\image\
     */
    private String msgType;
}
