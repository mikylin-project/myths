package cn.mikylin.myths.common;

/**
 * myths common constant
 *
 * @author mikylin
 * @date 20191108
 */
public final class Constants {

    /**
     * blank constant.
     */
    public static final class Blank {
        public static final char NULL_CHAR = '\u0000';
    }

    /**
     * system constant.
     */
    public static final class System {
        public static final String CLASS = "class";
        public static final String POINT = ".";
        public static final String POINT_CLASS = ".class";
        public static final String SLASH = "/";
        public static final String UNDER_LINE = "_";

        public static final int COMPUTER_CORE = Runtime.getRuntime().availableProcessors() * 2;
    }

    /**
     * http constant.
     */
    public static final class Http {
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String APPLICATION_JSON = "application/json";
        public static final int STATUS_OK = 200;
        public static final int STATUS_404 = 404;
    }

    /**
     * date constant.
     */
    public static final class Date {
        public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String TIME_FORMAT = "HH:mm:ss";
        public static final String DATE_FORMAT = "yyyy-MM-dd";
    }

    /**
     * charset constant.
     */
    public static final class Charset {
        public static final String UTF8 = "utf-8";
        public static final String GBK = "gbk";
    }
}