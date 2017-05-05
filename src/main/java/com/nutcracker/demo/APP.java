package com.nutcracker.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;

/**
 * Created by huh on 2016/7/27.
 */
@Slf4j
public class APP {
    public static void main(String[] arg){
        //String host = PropertyHolder.getContextProperty("jdbc.url");
        //log.info(Calendar.DAY_OF_MONTH);
        System.out.print(Calendar.DAY_OF_MONTH);
        //log.info(host);
    }
}


