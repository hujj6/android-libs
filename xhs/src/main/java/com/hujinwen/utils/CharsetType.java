package com.hujinwen.utils;

import java.nio.charset.Charset;

public enum CharsetType {
    UTF8("UTF-8"),
    GBK("gbk"),
    GB2312("GB2312");

    public final Charset charset;
    public final String charsetName;


    CharsetType(String charset) {
        this.charsetName = charset;
        this.charset = Charset.forName(charset);
    }

    public static CharsetType forType(String value) {
        for (CharsetType charsetType : CharsetType.values()) {
            if (charsetType.charsetName.equalsIgnoreCase(value)) {
                return charsetType;
            }
        }
        return null;
    }

}
