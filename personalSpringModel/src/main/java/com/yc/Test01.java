package com.yc;

import com.yc.service.UserBiz;
import org.ycframework.context.YcAnnotationConfigApplicationContext;
import org.ycframework.context.YcApplicationContext;

public class Test01 {
    public static void main(String[] args) {
//        Logger logger = LoggerFactory.getLogger( Test01.class );
//
//        logger.error("error");
//        logger.warn("warn");
//        logger.info("info");
//        logger.debug("debug");
//        logger.trace("trace");
        YcApplicationContext yac = new YcAnnotationConfigApplicationContext( MyConfig.class );

        UserBiz ub = (UserBiz) yac.getBean("userBizImpl");
        ub.add("张无忌");
    }
}
