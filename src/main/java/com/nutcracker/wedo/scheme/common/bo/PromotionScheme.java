package com.nutcracker.wedo.scheme.common.bo;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**推广方案基类
 * Created by huh on 2017/3/16.
 */
@Data
@XmlRootElement
public class PromotionScheme {

    /** 生成url参数：高级接口 */
    public static final String URLGEN_ARG_ADV_INTER = "1";
    /** 生成url参数：无高级接口 */
    public static final String URLGEN_ARG_NOT_ADV_INTER = "2";

    /** id */
    protected Long schemeId;
    /** 推广方案名称 */
    protected String schemeName;
    /** 微信公众号appid */
    protected String wxAppId;
    /** 创建人 */
    protected Long creator;
    /** 创建人 name */
    protected Long creatorName;

    /** 创建时间 */
    protected Date createTime;
    /** 修改人 */
    protected Long modifier;
    /** 修改人姓名 */
    protected String modifierName;
    /** 修改时间 */
    protected Date updateTime;
    /** 推广方案类型 */
    protected String schemeType;

    /**
     * 使用另一方案的数据初始化本方案数据
     *
     * @param scheme scheme
     */
    public void initData(PromotionScheme scheme) {
        this.schemeId = scheme.getSchemeId();
        this.schemeName = scheme.getSchemeName();
        this.wxAppId = scheme.getWxAppId();
        this.creator = scheme.getCreator();
        this.createTime = scheme.getCreateTime();
        this.modifier = scheme.getModifier();
        this.modifierName = scheme.getModifierName();
        this.updateTime = scheme.getUpdateTime();
        this.schemeType = scheme.getSchemeType();
    }
}

