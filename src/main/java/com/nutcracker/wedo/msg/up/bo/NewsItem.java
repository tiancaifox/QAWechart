package com.nutcracker.wedo.msg.up.bo;

import com.nutcracker.wedo.common.annotation.NullDeal;
import com.nutcracker.wedo.common.annotation.StructureData;
import com.nutcracker.wedo.msg.base.bo.IMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * 图文消息条目
 * Created by huh on 2017/1/19.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@XmlRootElement
public class NewsItem  implements IMessage {
	/**
	 * 头条消息
	 */
	@StructureData
	public static final String IS_FIRST_ITEM = "1";

	/**
	 * 非头条
	 */
	@StructureData
	public static final String IS_NOT_FIRST_ITEM = "0";

	/**
	 * 主键
	 */
	@StructureData
	private Long id;

	/**
	 * 消息id
	 */
	@StructureData
	private Long messageId;

	/**
	 * 是否是头条图文(1:是，0:否)
	 */
	@StructureData
	private String isFirstItem;

	/**
	 * 消息详情，富文本
	 */
	@StructureData
	@NullDeal
	private String detail;

	/**
	 * 图文消息标题
	 */
	private String title;

	/**
	 * 作者
	 */
	@StructureData
	@NullDeal
	private String author;

	/**
	 * 创建时间
	 */
	@StructureData
	private Date createTime;
	/**
	 * 图文消息描述
	 */
	@NullDeal
	private String description;
	/**
	 * 图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80。
	 */
	private String picUrl;

	/**
	 * 图片id
	 */
	@StructureData
	private Long picId;
	/**
	 * 点击图文消息跳转链接
	 */
	@NullDeal
	private String url;

	@Override
	public String getMessageContent() {
		return "title:" + title + ",picUrl:" + picUrl + ",url:" + url;
	}

}
