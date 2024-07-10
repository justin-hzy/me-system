package com.me.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SeqHelper {
    private static long seq = 0L;
    private static int mod = 10000;

    public SeqHelper() {
    }

    public static long genSeq() {
        return ++seq;
    }

    public static String genNumber(String pre) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMHHmmss");
        String id = sdf.format(new Date());
        return pre == null ? id + (genSeq() + (long)mod) : pre + id + (genSeq() + (long)mod);
    }
}
