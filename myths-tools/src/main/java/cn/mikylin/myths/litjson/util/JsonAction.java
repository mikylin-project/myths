package cn.mikylin.myths.litjson.util;

/**
 * the static config class for delegate the action
 */
public final class JsonAction {

    /**
     * control type
     */
    public static final int CONTROL_REST = 0;
    public static final int CONTROL_BEGIN_OBJECT = 1;
    public static final int CONTROL_END_OBJECT = 2;

    public static final int CONTROL_BEGIN_COLLECTION = 3;
    public static final int CONTROL_END_COLLECTION = 4;

    /**
     * read tache
     */
    public static final int TACHE_BEGIN_READ_KEY = 20;
    public static final int TACHE_END_READ_KEY = 21;
    public static final int TACHE_BEGIN_READ_VALUE = 30;
    public static final int TACHE_END_READ_VALUE = 31;




}
