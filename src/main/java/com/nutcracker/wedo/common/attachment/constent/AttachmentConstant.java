package com.nutcracker.wedo.common.attachment.constent;

import com.nutcracker.wedo.common.util.PropertyHolder;

/**
 * Created by huh on 2017/4/17.
 */
public interface AttachmentConstant {
    /**
     * mfs 路径
     */
    String MFS_PATH = PropertyHolder.getContextProperty("mfs.path");
    /**
     * 文件tokenkey前缀
     */
    String FILE_PREFIX = "[wedo]-fs:f:";
    /**
     * 文件授权value前缀
     */
    String AUTH_PREFIX = "[wedo]-fs:u:";
    /**
     * 空的ucid
     */
    Long EMPTY_UCID = 0L;
    /**
     * mfs 上传授权token时效
     */
    int MFS_AUTHTOKENEXPIRES = Integer.parseInt(PropertyHolder.getContextProperty("mfs.authTokenExpires"));
    /**
     * MFS 下载授权token时效
     */
    int MFS_FILETOKENEXPIRES = Integer.parseInt(PropertyHolder.getContextProperty("mfs.fileTokenExpires"));

    /**
     * 上传URL
     */
    String UPLOAD_URL = PropertyHolder.getContextProperty("domainUrl") + "/rest/fs/upload?upToken=";

    /**
     * uedit附件下载地址
     */
    String UEDITOR_UPLOAD_URL = PropertyHolder.getContextProperty("domainUrl")
            + "/rest/fs/ueditor/download?path=";
    /**
     * 下载URL
     */
    String DOWNLOAD_URL = PropertyHolder.getContextProperty("domainUrl") + "/rest/fs/download?token=";
    /**
     * 富文本文件格式表达式
     */
    String FID_PATTERN_STRING = "###(\\d+)###";
    /**
     * 下载URL
     */
    String URL_PATH_PATTERN = PropertyHolder.getContextProperty("domainUrl")
            + "/fs/download\\?token=(\\w{32})";
    /**
     * 图片上传正则验证表达式
     */
    String TRANSFERRED_IMG_PATTERN = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
    /**
     * 编码图片字符串开始标示
     */
    String DATA_IMG = "data:image";
    /**
     * token 长度
     */
    int TOKEN_SIZE = 32; // 32字节

}

