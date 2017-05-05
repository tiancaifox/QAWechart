package com.nutcracker.wedo.scheme.news.bo;

import com.nutcracker.wedo.msg.up.bo.IUpMessage;
import com.nutcracker.wedo.msg.up.bo.NewsMessage;
import com.nutcracker.wedo.scheme.common.MessageGenerator;
import com.nutcracker.wedo.scheme.common.bo.PromotionScheme;
import com.nutcracker.wedo.scheme.common.content.SchemeType;

/**图文消息推广方案
 * Created by huh on 2017/3/16.
 */
public class NewsMessageScheme extends PromotionScheme implements MessageGenerator {

    private NewsMessage newsMessage;

    public NewsMessage getNewsMessage() {
        return newsMessage;
    }

    public void setNewsMessage(NewsMessage newsMessage) {
        this.newsMessage = newsMessage;
        newsMessage.setIsScheme(Integer.valueOf(NewsMessage.IS_SCHEME));
    }

    public NewsMessageScheme() {
        super();
        this.schemeType = SchemeType.NEWS;
    }

    @Override
    public IUpMessage generatorMessage() {
        return newsMessage;
    }
}

