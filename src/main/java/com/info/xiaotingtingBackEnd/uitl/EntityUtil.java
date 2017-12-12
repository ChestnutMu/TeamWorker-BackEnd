package com.info.xiaotingtingBackEnd.uitl;

import java.sql.Timestamp;
import java.util.Random;

/**
 * Created by king on 2017/9/22.
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

    /**
     * 计算优惠准确值（10 20 30...以整十倍数）
     *
     * @param exchangeCount
     * @return
     * @author chaoking
     */
    public static long calFavourCount(long exchangeCount) {
        if (exchangeCount < 10l) {
            return 10l;
        } else {
            if (exchangeCount % 10 < 5) {
                return (exchangeCount / 10) * 10;
            } else {
                return (exchangeCount / 10 + 1) * 10;
            }
        }
    }

    /**
     * 计算发布速降区商品需要扣除的积分（底价一定比例）
     *
     * @param minPrice
     * @param costIntegralsProportion
     * @return
     */
    public static Long calCostIntegrals(long minPrice, int costIntegralsProportion) {
        Long costIntegrals = minPrice * costIntegralsProportion / 100;
        if (costIntegrals <= 0) {
            return 1l;
        } else {
            return costIntegrals;
        }
    }
}
