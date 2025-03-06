package com.me.common.utils;

import java.security.MessageDigest;

public class Md5Util {

    public static String getSign(String str)  {
        String r="";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[]=md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if(i<0) i+= 256;
                if(i<16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            r=buf.toString();
        }catch(Exception e){
        }
        return r;
    }
}
