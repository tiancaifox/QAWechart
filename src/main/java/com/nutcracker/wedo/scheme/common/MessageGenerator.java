package com.nutcracker.wedo.scheme.common;

import com.nutcracker.wedo.msg.up.bo.IUpMessage;

/**推广方案接口-实现该接口的方案可以弹出消息
 * Created by huh on 2017/3/16.
 */
public interface MessageGenerator {

    /**
     * 生成消息
     * @return Message
     */
    IUpMessage generatorMessage();

}
