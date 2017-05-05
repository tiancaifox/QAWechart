package com.nutcracker.wedo.common.attachment.dao;

import com.alibaba.fastjson.JSON;
import com.nutcracker.wedo.common.attachment.bo.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import utils.AbstractTestCase;

import javax.annotation.Resource;

/**
 * Created by huh on 2017/4/6.
 */
@Slf4j
public class FileRepoDaoTest extends AbstractTestCase {

    @Resource
    private FileRepoDao fileRepoDao;

    @Test
    public void findTest() {
        Attachment temp = fileRepoDao.readMetaById(5348L);
        String jsonString = JSON.toJSONString(temp);
        log.info(jsonString);
    }
}
