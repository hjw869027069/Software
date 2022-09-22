import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import exceptions.FileAnalyseException;
import exceptions.HashException;
import exceptions.NotFoundFileException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.CalculationUtils;
import util.CommonUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MainTest {
    static String analyseStr;

    static String simpleWord = "今天是星期天，天气晴，今天晚上我要去看电影。";
    static String simpleCopyWord = "今天是周天，天气晴朗，我晚上要去看电影。";
    static String writeFilePath = "C:\\Users\\HJW\\Desktop\\测试文本\\write.txt";


    static String OrigFilePath = "C:\\Users\\HJW\\Desktop\\测试文本\\orig.txt";
    static String CopyFilePath1 = "C:\\Users\\HJW\\Desktop\\测试文本\\orig_0.8_add.txt";
    static String CopyFilePath2 = "C:\\Users\\HJW\\Desktop\\测试文本\\orig_0.8_del.txt";
    static String CopyFilePath3 = "C:\\Users\\HJW\\Desktop\\测试文本\\orig_0.8_dis_1.txt";
    static String CopyFilePath4 = "C:\\Users\\HJW\\Desktop\\测试文本\\orig_0.8_dis_10.txt";
    static String CopyFilePath5 = "C:\\Users\\HJW\\Desktop\\测试文本\\orig_0.8_dis_15.txt";



    /**
     * 测试查看分词里的方法
     */
    @Test
    void testAnalysis(){

        List<Term> termList = StandardTokenizer.segment(simpleWord);
        System.out.println(termList);
        List<Term> termList2 = StandardTokenizer.segment(simpleCopyWord);
        System.out.println(termList2);
        Assertions.assertTrue(termList.size() > 0,"分词结果为空");
        Assertions.assertTrue(termList2.size() > 0,"分词结果为空");

        List<String> list = HanLP.extractKeyword(simpleWord, simpleWord.length());
        System.out.println(list);
        Assertions.assertTrue(list.size() > 0,"分词结果为空");
        List<String> list2 = HanLP.extractKeyword(simpleCopyWord, simpleCopyWord.length());
        System.out.println(list2);
        Assertions.assertTrue(list2.size() > 0,"分词结果为空");
    }


    /**
     * 测试读取不存在的文件
     */
    @Test
    void testReadFileNotFound(){
        try {
            CommonUtils.readFileToStr("C:\\orig123.txt");
            Assertions.fail("没有抛出异常");
        } catch (NotFoundFileException e) {
            e.printStackTrace();
            Assertions.assertTrue(true);
        }
    }

    /**
     * 测试读取文件并看看分词效果
     */
    @Test
    void testReadFile(){
        try {
            analyseStr = CommonUtils.readFileToStr(OrigFilePath);
        } catch (NotFoundFileException e) {
            e.printStackTrace();
            Assertions.fail("文件不存在");
        }
        System.out.println(HanLP.extractKeyword(analyseStr,analyseStr.length()));
    }


    /**
     * 简单测试一下工具类的方法
     */
    @Test
    void testAnalyseString(){
        try {
            System.out.println(CommonUtils.analyseString(simpleWord));
            System.out.println(CommonUtils.analyseString(simpleCopyWord));
            System.out.println(CommonUtils.analyseString("你好，你好，你好，你好，你好，你好，你好，你好，我好，他好，明天，好啊，不好，周日" +
                    "周六，太阳，不大，"));
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("分词结果有错误");
        }
    }

    /**
     * 简单测试下文件分析异常
     */
    @Test
    void testFileAnalyseException(){
        try {
            CommonUtils.analyseString("");
            Assertions.fail("没有抛出异常");
        } catch (FileAnalyseException e) {
            e.printStackTrace();
            Assertions.assertTrue(true);
        }

        try {
            CommonUtils.analyseString(null);
            Assertions.fail("没有抛出异常");
        } catch (FileAnalyseException e) {
            e.printStackTrace();
            Assertions.assertTrue(true);
        }

        try {
            CommonUtils.analyseString("    " +
                    "");
            Assertions.fail("没有抛出异常");
        } catch (FileAnalyseException e) {
            e.printStackTrace();
            Assertions.assertTrue(true);
        }
    }

    /**
     * 测试MD5哈希处理词语，简单检查一下是否是128位
     */
    @Test
    void  testHashWord(){
        HanLP.extractKeyword(simpleWord, simpleWord.length()).forEach(
                word -> {
                    try {
                        String hash = CalculationUtils.wordHash(word);
                        System.out.println(word +" : "+ hash);
                        Assertions.assertEquals(128, hash.length(), "hash值长度不是128");
                    } catch (HashException e) {
                        Assertions.fail("哈希出错");
                        e.printStackTrace();
                    }
                }
        );
    }

    /**
     * 测试哈希异常检查
     */
    @Test
    void testHashException(){
        try {
            CalculationUtils.wordHash("");
            Assertions.fail("没有抛出异常");
        } catch (HashException e) {
            e.printStackTrace();
            Assertions.assertTrue(true);
        }

        try {
            CalculationUtils.wordHash(null);
            Assertions.fail("没有抛出异常");
        } catch (HashException e) {
            e.printStackTrace();
            Assertions.assertTrue(true);
        }

        try {
            CalculationUtils.wordHash("    ");
            Assertions.fail("没有抛出异常");
        } catch (HashException e) {
            e.printStackTrace();
            Assertions.assertTrue(true);
        }
    }

    /**
     * 简单测试加权算法是否正常
     */
    @Test
    void testHashWeight(){
        Map<String, Integer> map = null;
        try {
            map = CommonUtils.analyseString(simpleWord);
        } catch (FileAnalyseException e) {
            e.printStackTrace();
            Assertions.fail("解析错误");
        }

        map.forEach((word, count) -> {
            try {
                String hash = CalculationUtils.wordHash(word);
                System.out.println(word +" : "+ hash);
                Assertions.assertEquals(128, hash.length(), "hash值长度不是128");
                int[] hashWeight = CalculationUtils.hashWeight(hash,count);
                System.out.println(word +" : "+ Arrays.toString(hashWeight));
                Assertions.assertEquals(128, hashWeight.length, "加权后的hash值长度不是128");

            } catch (HashException e) {
                Assertions.fail("哈希出错");
                e.printStackTrace();
            }
        });
    }

    /**
     * 测试合并值得到最终的hash值
     */
    @Test
    void testGetSimHash(){
        int[] hash = new int[128];
        Random r = new Random();
        for (int i = 0; i < hash.length; i++) {
            hash[i] = r.nextInt(4)-1;
        }
        Assertions.assertTrue(CalculationUtils.getSimHash(hash).contains("0"), "没有包含0");
    }
    /**
     * 测试计算simHash
     */
    @Test
    void testCalculateSimHash(){
        try {
            String hash1 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(simpleCopyWord));
            String hash2 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(simpleWord));
            System.out.println(hash1);
            System.out.println(hash2);

            Assertions.assertEquals(hash1.length(),128,"hash值长度不是128");
            Assertions.assertEquals(hash2.length(),128,"hash值长度不是128");

        } catch (FileAnalyseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetSimilarity(){
        String hash1 = null;
        String hash2 = null;
        try {
            hash1 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(simpleCopyWord));
            hash2 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(simpleWord));
        } catch (FileAnalyseException e) {
            e.printStackTrace();
            Assertions.fail("解析错误");
        }
        double similarity = CalculationUtils.getSimilarity(hash1, hash2);
        System.out.println(similarity);
        String format = String.format("相似度为：%.2f", similarity);
        System.out.println(format);
        Assertions.assertTrue(0 <= similarity && similarity <= 1, "相似度不在0-1之间");


    }

    @Test
    void testGetNotSimilarity(){
        String hash1 = null;
        String hash2 = null;
        try {
            hash1 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(simpleCopyWord));
            hash2 = CalculationUtils.calculateSimHash(CommonUtils.analyseString("明天是疯狂星期四，我要去吃肯德基，跟你一起"));
        } catch (FileAnalyseException e) {
            e.printStackTrace();
            Assertions.fail("解析错误");
        }
        double similarity = CalculationUtils.getSimilarity(hash1, hash2);
        System.out.println(similarity);
        String format = String.format("相似度为：%.2f", similarity);
        System.out.println(format);
        Assertions.assertTrue(0 <= similarity && similarity <= 1, "相似度不在0-1之间");


    }

    @Test
    void testGetSimilarity1(){
        String hash1;
        String hash2;

        try {
            hash1 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(CommonUtils.readFileToStr(OrigFilePath)));
            hash2 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(CommonUtils.readFileToStr(CopyFilePath1)));
            double similarity = CalculationUtils.getSimilarity(hash1, hash2);
            System.out.println(similarity);
            Assertions.assertTrue(0 <= similarity && similarity <= 1, "相似度不在0-1之间");
        } catch (FileAnalyseException | NotFoundFileException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetSimilarity2(){
        String hash1;
        String hash2;

        try {
            hash1 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(CommonUtils.readFileToStr(OrigFilePath)));
            hash2 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(CommonUtils.readFileToStr(CopyFilePath2)));
            double similarity = CalculationUtils.getSimilarity(hash1, hash2);
            System.out.println(similarity);
            Assertions.assertTrue(0 < similarity && similarity < 1, "相似度不在0-1之间");
        } catch (FileAnalyseException | NotFoundFileException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetSimilarity3(){
        String hash1;
        String hash2;

        try {
            hash1 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(CommonUtils.readFileToStr(OrigFilePath)));
            hash2 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(CommonUtils.readFileToStr(CopyFilePath3)));
            double similarity = CalculationUtils.getSimilarity(hash1, hash2);
            System.out.println(similarity);
            Assertions.assertTrue(0 < similarity && similarity < 1, "相似度不在0-1之间");
        } catch (FileAnalyseException | NotFoundFileException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetSimilarity4(){
        String hash1;
        String hash2;

        try {
            hash1 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(CommonUtils.readFileToStr(OrigFilePath)));
            hash2 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(CommonUtils.readFileToStr(CopyFilePath4)));
            double similarity = CalculationUtils.getSimilarity(hash1, hash2);
            System.out.println(similarity);
            Assertions.assertTrue(0 < similarity && similarity < 1, "相似度不在0-1之间");
        } catch (FileAnalyseException | NotFoundFileException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetSimilarity5(){
        String hash1;
        String hash2;

        try {
            hash1 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(CommonUtils.readFileToStr(OrigFilePath)));
            hash2 = CalculationUtils.calculateSimHash(CommonUtils.analyseString(CommonUtils.readFileToStr(CopyFilePath5)));
            double similarity = CalculationUtils.getSimilarity(hash1, hash2);
            System.out.println(similarity);
            Assertions.assertTrue(0 < similarity && similarity < 1, "相似度不在0-1之间");
        } catch (FileAnalyseException | NotFoundFileException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试写入文件
     */
    @Test
    void testWriteFile(){
        CommonUtils.writeFile(writeFilePath, "test");
        try {
            String s = CommonUtils.readFileToStr(writeFilePath);
            Assertions.assertTrue(s.contains("test"),"写入文件失败");
        } catch (NotFoundFileException e) {
            e.printStackTrace();
            Assertions.fail("写入文件失败");
        }
    }

    /**
     * 测试主函数
     */
    @Test
    void testMain(){
        String[] args = new String[3];
        args[0] = OrigFilePath;
        args[1] = CopyFilePath1;
        args[2] = writeFilePath;
        Main.main(args);
    }
}
