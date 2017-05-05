package com.nutcracker.wedo.common.attachment.service;

import com.nutcracker.wedo.common.attachment.bo.Attachment;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 类AttachmentManager.java的实现
 * Created by huh on 2017/5/3.
 */
public interface FileRepoService {
    /**
     * 保存一个附件信息
     *
     * @param stream 文件流
     * @return 附件信息ID
     */
    public Long save(InputStream stream, Attachment attachment);

    /**
     * 對文本中的http的下載連接進行替換
     *
     * @param text 文本
     * @return 處理過後的文本
     */
    public String escapeForHttp(String text, String attachmentCategory);

    /**
     * 将输入的包含文件url的html文本, 替换其中的文件url为fid标识并输出.
     *
     * @param html 输入字串
     * @return 输出字串.
     */
    public String escape(String html);

    /**
     * 将从库中取出的包含fid标识的文本串, 渲染其中的fid字段为文件实际url路径.
     *
     * @param text 输入字串
     * @return 输出字串
     */
    public String render(String text);

    /**
     * 通过token值获取附件信息
     *
     * @param token token值
     * @return 附件信息
     */
    public Attachment readMetaByToken(String token);

    /**
     * 根据ID获取附件信息
     *
     * @param fid 文件ID
     * @return 附件实体对象
     */
    public Attachment readMeta(Long fid);

    /**
     * 批量读取文件信息
     *
     * @param fids 文件ID列表
     * @return 文件列表
     */
    public Map<Long, Attachment> batchReadMeta(List<Long> fids);

    /**
     * 下载附件保存
     *
     * @param url 附件http地址
     * @param category 分类
     * @return 附件ID
     */
    public Long downloadAndSave(String url, String category);
}
