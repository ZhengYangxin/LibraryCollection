package com.zyx.library.eventbuslibrary;

import java.util.logging.Level;

/**
 * @author pielan
 * @date 14/3/19 下午11:25
 * @dec
 */
public interface Logger {

    public void log(Level level, String msg, Throwable cause);

    public void log(Level level, String msg);

    public static class JavaLogger implements Logger {
        protected final java.util.logging.Logger logger;

        public JavaLogger(String tag) {
            this.logger = java.util.logging.Logger.getLogger(tag);
        }

        @Override
        public void log(Level level, String msg, Throwable cause) {
            logger.log(level, msg, cause);
        }

        @Override
        public void log(Level level, String msg) {
            logger.log(level, msg);
        }
    }

    public static class SystemOutLogger implements Logger {
        @Override
        public void log(Level level, String msg, Throwable cause) {
            System.out.println("[" + level + "] " + msg);
            cause.printStackTrace(System.out);
        }

        @Override
        public void log(Level level, String msg) {
            System.out.println("[" + level + "] " + msg);
        }
    }



}
