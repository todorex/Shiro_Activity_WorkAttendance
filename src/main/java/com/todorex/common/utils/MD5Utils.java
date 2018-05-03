package com.todorex.common.utils;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/5/3 下午1:18
     *@Description 用MD5对密码进行编码
     */
    public static String encryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // 利用BASE64Encoder防止密码中出现乱码的字符
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String result = base64Encoder.encode(md5.digest(password.getBytes("utf-8")));

        return result;
    }

    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/5/3 下午1:18
     *@Description 验证密码正确性
     */
    public  static boolean  checkPassword(String inputPwd,String dbPwd) throws UnsupportedEncodingException, NoSuchAlgorithmException {
         String result = encryptPassword(inputPwd);
         if(result.equals(dbPwd)){
             return true;
         }else {
             return false;
         }
    }
}
