package com.nutcracker.wedo.common.attachment.bo;

import lombok.Data;

import java.util.Date;

/**文件元信息
 * Created by huh on 2017/4/5.
 */
@Data
public class Attachment {
    /**
     * 文件原信息ID
     */
    private Long id;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件格式
     */
    private String fileType;
    /**
     * 上传时间
     */
    private Date uploadTime;
    /**
     * 存储路径
     */
    private String path;
    /**
     * 下载URL
     */
    private String downloadUrl;
    /**
     * 唯一token
     */
    private String token;
}

