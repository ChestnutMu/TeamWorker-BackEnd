package com.info.xiaotingtingBackEnd.util;

import java.sql.Timestamp;
import java.util.Random;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/11 16:24:10
 * Description：实体工具类
 * Email: xiaoting233zhang@126.com
 */
public class EntityUtil {
    private static Random rand;

    static {
        rand = new Random();
    }

    /**
     * <p>
     * 网页使用
     * 根据登录信息生成token MD5(用户名+时间戳)
     */
    public static String generateToken(String userName) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String token = MD5Util.MD5(userName + timestamp.getTime());
        return token;
    }

    /**
     * 默认6位
     *
     * @return
     */
    public static String getIdByTimeStampAndRandom() {
        return getIdByTimeStampAndRandom(6);
    }


    /**
     * 由时间戳和随机字符串组成id
     *
     * @param
     * @return
     */
    public static String getIdByTimeStampAndRandom(int digit) {
        if (digit < 0) return "";
        return String.valueOf(System.currentTimeMillis()) + getRandomString(digit);
    }

    /**
     * 生成随机字符串
     *
     * @param length 字符串长度
     * @return
     */
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
