package com.nutcracker.wedo.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import utils.AbstractTestCase;

/**
 * Created by huh on 2017/4/17.
 */
@Slf4j
public class PropertyHolderTest extends AbstractTestCase {
    @Test
    public void test(){
        String host = com.nutcracker.wedo.common.util.PropertyHolder.getContextProperty("domain");
        log.info("========================================================================");
        log.info(host);
    }
}
