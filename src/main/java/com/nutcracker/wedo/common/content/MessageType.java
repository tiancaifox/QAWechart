package com.nutcracker.wedo.common.content;

/**消息类型
 * Created by huh on 2017/3/16.
 */
public interface MessageType {
    /** 文本消息 */
    String TEXT = "text";
    /** 图片消息 */
    String IMAGE = "image";
    /** 视频消息 */
    String VIDEO = "video";
    /** 语音消息 */
    String VOICE = "voice";

    /** 地理位置消息 */
    String LOCATION = "location";
    /** 链接消息 */
    String LINK = "link";
    /** 事件消息 */
    String EVENT = "event";

    /** 图文消息 */
    String NEWS = "news";
    /** 音乐消息 */
    String MUSIC = "music";

    /** 返回消息为空的情况 */
    String NOMSG = "nomsg";

}

