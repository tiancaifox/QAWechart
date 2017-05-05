package com.nutcracker.wedo.common.attachment.service;

/**附件token验证提供者
 * Created by huh on 2017/3/16.
 */
public interface FileTokenProvider {
    /**
     * 获取文件路径
     * @param fid 文件fid
     * @return 下载URL
     */
    public String getDownLoadUrl(Long fid);
    /**
     * 获取上传的URL
     * @return 上传的URL 包含token参数
     */
    public String getUploadUrl();
    /**
     * 获得上传url.
     *
     * @param ucid 用户ID
     * @return url 上传RUL
     */
    public String getUploadUrl(Long ucid);
    /**
     * 通過token獲取文件的fid
     * @param token token值
     * @return 文件fid值
     */
    public Long getFidByToken(String token);
    /**
     * 检查上传的token的正确性
     * @param token token
     * @return 是否验证成功
     */
    public boolean validateUploadAuthToken(String token) ;
    /**
     * 检查上传的token的正确性
     * @param token token
     * @return 是否验证成功
     */
    public boolean validateDownloadAuthToken(String token);
}

