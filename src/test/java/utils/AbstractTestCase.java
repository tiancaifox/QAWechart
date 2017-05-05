package utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author huh
 * @description 测试用例的基类，定义Unitil测试用例的公共行为。 1、定义Spring的配置文件。 2、定义事务管理器，默认自动回滚 3、支持事务管理
 * @created 2016/8/17 16:46
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:conf/applicationContext.xml")
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
@Slf4j
public class AbstractTestCase {

    public void echo(String message) {
        log.info("---------------------------------------------------------");
        log.info(message);
        log.info("---------------------------------------------------------");
    }

    @Test
    public void test() {

    }

}
