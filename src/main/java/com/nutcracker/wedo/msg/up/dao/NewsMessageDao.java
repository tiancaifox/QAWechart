package com.nutcracker.wedo.msg.up.dao;

import com.nutcracker.wedo.common.annotation.SqlMapper;
import com.nutcracker.wedo.msg.up.bo.NewsMessage;
import org.apache.ibatis.annotations.Param;

/**图文消息Dao
 * Created by huh on 2017/3/16.
 */
@SqlMapper
public interface NewsMessageDao {

    /**
     * 保存消息
     * @param message 消息
     */
    void insert(@Param("message") NewsMessage message);

    /**
     * 删除消息
     * @param id 消息id
     */
    void delete(@Param("id") Long id);

    /**
     * 更新消息
     * @param message 消息
     */
    void update(@Param("message") NewsMessage message);

    /**
     * 通过id获取消息内容
     * @param id 方案id
     * @return 消息
     */
    NewsMessage getById(@Param("id") Long id);

    /**
     * 通过档案id获取消息内容
     * @param schemeId 档案id
     * @return 消息
     */
    NewsMessage getBySchemeId(@Param("schemeId") Long schemeId);
}


