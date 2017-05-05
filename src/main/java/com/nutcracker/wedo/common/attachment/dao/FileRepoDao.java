package com.nutcracker.wedo.common.attachment.dao;


import com.nutcracker.wedo.common.annotation.SqlMapper;
import com.nutcracker.wedo.common.attachment.bo.Attachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**上传Dao层实现 类FileRepoMapper.java的实现描述
 * Created by huh on 2017/4/5.
 */
@SqlMapper
public interface FileRepoDao {

    /**
     * 根据fid获取MFS存储路径
     *
     * @param id 文件FID
     * @return 文件路径
     */
    String getFilePath(@Param("id") Long id);

    /**
     * 保存附件元数据信息
     *
     * @param meta 附件元数据信息
     */
    void saveAttachment(Attachment meta);

    /**
     * 获取附件元数据信息
     *
     * @param id 附件信息对应的条目ID
     * @return 附件元数据信息
     */
    Attachment readMetaById(@Param("id") Long id);

    /**
     * 根据文件条目ID列表获取附件元数据列表信息
     *
     * @param ids 条目信息列表
     * @return 元数据列表
     */
    List<Attachment> batchReadMeta(@Param("ids") List<Long> ids);

    /**
     * 获取附件元数据信息
     *
     * @param token 附件信息对应的条目token
     * @return 附件元数据信息
     */
    Attachment readMetaByToken(@Param("token") String token);
}


