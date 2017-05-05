package com.nutcracker.wedo.common.attachment.service.impl;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.nutcracker.wedo.common.attachment.bo.Attachment;
import com.nutcracker.wedo.common.attachment.constent.AttachmentConstant;
import com.nutcracker.wedo.common.attachment.dao.FileRepoDao;
import com.nutcracker.wedo.common.attachment.service.FileTokenProvider;
import com.nutcracker.wedo.common.exception.ExceptionUtil;
import com.nutcracker.wedo.common.framework.cache.redis.HaRedisCacheManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 附件token验证提供者
 * Created by huh on 2017/5/3.
 */
@Service("fileTokenProvider")
public class FileTokenProviderImpl implements FileTokenProvider {

    @Resource(name = "fileRepoDao")
    private FileRepoDao fileRepoDao;

    @Resource(name = "myCacheManager")
    private HaRedisCacheManager myCacheManager;

    /**
     * 获取文件路径
     *
     * @param fid 文件fid
     * @return 下载URL
     */
    public String getDownLoadUrl(Long fid) {
        if (fid <= 0) {
            ExceptionUtil.wrapException("文件id为空！");
        }
        Attachment attachment = fileRepoDao.readMetaById(fid);
        return AttachmentConstant.DOWNLOAD_URL + attachment.getToken();
    }

    /**
     * 获取上传的URL
     *
     * @return 上传的URL 包含token参数
     */
    public String getUploadUrl() {
        return this.getUploadUrl(AttachmentConstant.EMPTY_UCID);
    }

    /**
     * 获得上传url.
     *
     * @param ucid 用户ID
     * @return url 上传RUL
     */
    public String getUploadUrl(Long ucid) {
        return AttachmentConstant.UPLOAD_URL + getUploadAuthToken(ucid);
    }

    /**
     * 通過token獲取文件的fid
     *
     * @param token token值
     * @return 文件fid值
     */
    public Long getFidByToken(String token) {
        if (StringUtils.isBlank(token)) {
            ExceptionUtil.wrapException("非法的token数值！");
        }

        String key = AttachmentConstant.FILE_PREFIX + token;
        String sFid = (String) myCacheManager.get(key);
        ;
        Long fid = -1L;
        if (StringUtils.isBlank(sFid)) {
            ExceptionUtil.wrapException("根据文件token找不到对应的ID信息！");
        }
        try {
            fid = Long.parseLong(sFid);
        } catch (NumberFormatException e) {
            ExceptionUtil.wrapException("ID信息不是数字！");
        }

        return fid;
    }

    /**
     * 检查上传的token的正确性
     *
     * @param token token
     * @return 是否验证成功
     */
    public boolean validateUploadAuthToken(String token) {
        return this.validateAuthToken(token, AttachmentConstant.EMPTY_UCID, AttachmentConstant.AUTH_PREFIX);
    }

    /**
     * 检查上传的token的正确性
     *
     * @param token token
     * @return 是否验证成功
     */
    public boolean validateDownloadAuthToken(String token) {
        return this.validateAuthToken(token, AttachmentConstant.EMPTY_UCID, AttachmentConstant.FILE_PREFIX);
    }

    /**
     * 获取上传的授权token
     *
     * @param ucid ucID
     * @return 上传包含token的url
     */
    private String getUploadAuthToken(Long ucid) {
        String tk = token(ucid);
        String key = AttachmentConstant.AUTH_PREFIX + tk;
        myCacheManager.put(key, AttachmentConstant.MFS_AUTHTOKENEXPIRES, Long.toString(ucid));
        return tk;
    }

    /**
     * 获取下载的授权token
     *
     * @param fid 文件ID
     * @return token值
     */
    @SuppressWarnings("unused")
    private String getDownloadAuthToken(Long fid) {
        String tk = token(fid);
        String key = AttachmentConstant.FILE_PREFIX + tk;
        myCacheManager.put(key, AttachmentConstant.MFS_FILETOKENEXPIRES, Long.toString(fid));
        return tk;
    }

    /**
     * token 计算方法 计算规则为: code = md5(md5(id) + now)
     *
     * @param id key
     * @return token值
     */
    private String token(Long id) {
        HashCode codeL1 = Hashing.md5().newHasher().putLong(id).hash();
        HashCode codeL2 =
                Hashing.md5().newHasher().putString(codeL1.toString()).putString(UUID.randomUUID().toString()).hash();
        return codeL2.toString();
    }

    /**
     * 检查token的有效性.
     *
     * @param token token值
     * @param ucid ucid
     * @param prefix 前缀
     * @return 是否有效
     */
    private boolean validateAuthToken(String token, Long ucid, String prefix) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        String key = prefix + token;
        String val = (String) myCacheManager.get(key);
        if (StringUtils.isBlank(val)) {
            return false;
        }
        if (ucid != AttachmentConstant.EMPTY_UCID) {
            if (val.equals(Long.toString(ucid))) {
                return true;
            } else {
                return false;
            }
        }
        // 如果ucid == EMPTY_UCID, 则为无ucid输入的情况, 不做UCID的判断.
        return true;
    }
}
