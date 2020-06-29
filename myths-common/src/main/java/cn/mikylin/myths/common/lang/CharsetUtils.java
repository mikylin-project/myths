package cn.mikylin.myths.common.lang;

import java.nio.charset.*;

public final class CharsetUtils {

    public static Charset getDefault() {
        return getUtf8();
    }

    public static Charset getUtf8() {
        return StandardCharsets.UTF_8;
    }

    public static Charset getUtf16() {
        return StandardCharsets.UTF_16;
    }
}
