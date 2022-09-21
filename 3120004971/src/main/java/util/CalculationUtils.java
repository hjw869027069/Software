package util;


import cn.hutool.core.util.StrUtil;
import exceptions.HashException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author HJW
 * @date 2022-09-21 13:03
 * 代码用到的所有计算有关的工具类
 */
public class CalculationUtils {

    static final int HASH_BIT = 128;

    /**
     * 采用MD5进行对词语进行hash，得到的hash值使用16进制解析 再利用算法取128位二进制
     * @param word 词语
     * @return 128位二进制
     */
    public static String wordHash(String word) throws HashException {
        if (word == null || StrUtil.isBlank(word) || StrUtil.isEmpty(word)) {
            throw new HashException("词语为空");
        }

        try {
            // 采用MD5进行hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(word.getBytes(StandardCharsets.UTF_8));

            // hash值转为32位16进制
            StringBuilder hash = new StringBuilder();
            for (byte b : digest.digest()) {
                hash.append(String.format("%02x", b));
            }

            // 16进制转为128位2进制码
            StringBuilder finalHash = new StringBuilder();
            String strTemp;
            for (int i = 0; i < hash.length(); i ++) {
                // 每一位16进制数加上0000 最后截取后面的4位 得到便是这位数的二进制
                strTemp = "0000" + Integer.toBinaryString(Integer.parseInt(hash.substring(i, i + 1), 16));
                finalHash.append(strTemp.substring(strTemp.length() - 4));
            }

            // 不为128直接报错
            if (finalHash.length() != HASH_BIT) {
                throw new HashException("hash值长度不为128");
            }

            return finalHash.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new HashException("MD5算法异常");
        }

    }

    /**
     * 给二进制哈希值加权
     * @param hash 二进制哈希值
     * @param weight 权重
     * @return 加权后的二进制哈希值
     */
    public static int[] hashWeight(String hash, int weight) {
        // 新建一个数组用于存放加权后的二进制哈希值
        int[] hashArray = new int[HASH_BIT];
        // 遍历二进制哈希值，0则是-1，1则是1，将每一位加权后存入数组
        for (int i = 0; i < hash.length(); i++) {
            if (hash.charAt(i) == '1') {
                hashArray[i] = weight;
            } else {
                hashArray[i] = -1 * weight;
            }
        }

        return hashArray;
    }

    /**
     * 得到的合并后的hash值进行降维，最终得到simHash
     * @param mergeHash 合并后的hash值
     * @return sim哈希值
     */
    public static String getSimHash(int[] mergeHash){
        // 使用StringBuilder存储simHash
        StringBuilder simHash = new StringBuilder();
        // 遍历合并后的hash值，大于0则是1，小于0则是0
        for (int hash : mergeHash) {
            if (hash > 0) {
                simHash.append("1");
            } else {
                simHash.append("0");
            }
        }
        return simHash.toString();
    }


    /**
     * 根据词语得到simHash
     * @param wordCount 词语及其出现次数
     * @return simHash
     */
    public static String calculateSimHash(Map<String,Integer> wordCount){
        // 新建一个数组用于存放合并后的hash值,初始值为0
        int[] mergeHash = new int[HASH_BIT];
        for (int i = 0; i < HASH_BIT; i++) {
            mergeHash[i] = 0;
        }
        // 遍历词语及其出现次数,对每一个词语进行hash加权，然后合并
        wordCount.forEach((word,count) -> {
            try {
                int[] tempHash = hashWeight(wordHash(word),count);
                for (int i = 0; i < tempHash.length; i++) {
                    mergeHash[i] += tempHash[i];
                }
            } catch (HashException e) {
                e.printStackTrace();
            }
        });

        // 降维得到simHash
        return getSimHash(mergeHash);
    }

    /**
     * 计算两个simHash的相似度（杰卡德相似系数）
     * @param simHash1 simHash1
     * @param simHash2 simHash2
     * @return 相似度
     */
    public static double getSimilarity(String simHash1, String simHash2) {
        // 汉明距离
        int distance = 0;
        // 遍历simHash1和simHash2，不相同则汉明距离加1
        for (int i = 0; i < simHash1.length(); i++) {
            if (simHash1.charAt(i) != simHash2.charAt(i)) {
                distance++;
            }
        }
        // 杰卡德相似系数 = 1 - 汉明距离 / simHash长度
        return 1-((double)distance/256);
    }

}
