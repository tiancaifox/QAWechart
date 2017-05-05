package com.nutcracker.wedo.msg.up.bo;
import com.nutcracker.wedo.msg.base.bo.IMessage;

/**上行消息接口，需要持久化的消息，需要实现该接口，标记<code>UpMessage<code>注解并实现返回消息id的方法
 * Created by huh on 2017/3/16.
 */
public interface IUpMessage extends IMessage{
    /**
     * 返回消息id
     * @return
     */
    Long getUpMessageId();
    /**
     * 返回消息类型
     * @return
     */
    String getUpMessageType();
}
