package com.nutcracker.wedo.msg.up.bo;

import com.nutcracker.wedo.common.annotation.NullDeal;
import com.nutcracker.wedo.common.annotation.StructureData;
import com.nutcracker.wedo.common.annotation.UpMessage;
import com.nutcracker.wedo.common.content.MessageType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**图文消息
 * Created by huh on 2017/3/16.
 */
@Data
@UpMessage(messageType = MessageType.NEWS)
public class NewsMessage implements IUpMessage {

    /**
     * 推广方案
     */
    @StructureData
    public static final String IS_SCHEME = "1";
    /**
     * 不是推广方案
     */
    @StructureData
    public static final String IS_NOT_SCHEME = "0";

    /**
     * 消息id
     */
    @StructureData
    private Long id;

    /**
     * 推广方案id,仅当该图文消息为方案时该字段有效
     */
    @StructureData
    @NullDeal
    private Long schemeId;

    /**
     * 是否是推广方案，比如注册方案中需要弹出图文
     * 消息而自动生成的消息，就不属于推广方案(1:是，0:否)
     */
    @StructureData
    private Integer isScheme;

    /**
     * 图文消息个数，限制为10条以内
     */
    private Integer articleCount;
    /**
     * 图文消息的数据
     */
    private List<NewsItem> articles = new ArrayList<NewsItem>(3);

    @Override
    public String getMessageContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("articleCount:").append(articleCount).append(",articles[");
        for (NewsItem item : articles) {
            sb.append("{").append(item.getMessageContent()).append("},");
        }
        sb.deleteCharAt(sb.length() - 1).append("]");
        return sb.toString();
    }

    @Override
    public Long getUpMessageId() {
        return id;
    }

    @Override
    public String getUpMessageType() {
        return MessageType.NEWS;
    }
}

