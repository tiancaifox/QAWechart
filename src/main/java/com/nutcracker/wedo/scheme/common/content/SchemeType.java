package com.nutcracker.wedo.scheme.common.content;

import java.util.HashMap;
import java.util.Map;

/**推广方案类型
 * Created by huh on 2017/3/16.
 */
public interface SchemeType {

    /** 图文 */
    public static final String NEWS = "01";
    /** 用户注册 */
    public static final String USER_REGISTRY = "02";
    /** 活动推广 */
    public static final String ACTIVITY = "03";

    @SuppressWarnings("serial")
    public static final Map<String, String> TYPE = new HashMap<String, String>() {
        {
            put(NEWS, "图文消息");
            put(USER_REGISTRY, "用户注册");
            put(ACTIVITY, "活动推广");
        }
    };
}

