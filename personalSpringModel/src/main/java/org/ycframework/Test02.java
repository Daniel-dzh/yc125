package org.ycframework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test02 {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger( Test02.class );

        logger.error("error");
        logger.warn("warn");
        logger.info("info");
        logger.debug("debug");
        logger.trace("trace");
    }
}
