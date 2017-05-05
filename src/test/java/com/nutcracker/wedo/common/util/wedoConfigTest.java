package com.nutcracker.wedo.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Properties;

/**
 * Created by huh on 2017/1/12.
 */
@Slf4j
public class wedoConfigTest {

	@Test
	public void test() {
		Properties p = wedoConfig.getProperties();
		log.info(p.getProperty("C"));
		log.info(p.getProperty("D"));
	}
}
