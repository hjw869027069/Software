package util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import exceptions.FileAnalyseException;
import exceptions.NotFoundFileException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author HJW
 * @date 2022-09-20 21:58
 * 代码所需要用到的所有非计算的工具类
 */
public class CommonUtils {
    public static final int SHORT_WORD_LENGTH = 5;

    /**
     * 从文件中读取文本(为方便直接使用工具包)
     * @param filePath 文件路径
     * @return 文本
     */
    public static String readFileToStr(String filePath) throws NotFoundFileException {
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            throw new NotFoundFileException("该绝对路径的文件不存在");
        }
    }

    /**
     * 把文本解析并过滤后转为map
     * @param text 文本
     * @return map
     */
    public static Map<String,Integer> analyseString(String text) throws FileAnalyseException {
        if (text == null || StrUtil.isBlank(text) || StrUtil.isEmpty(text)) {
            throw new FileAnalyseException("文件解析异常，转换为字符串为空");
        }

        // 提取的关键词
        List<String> keyList = HanLP.extractKeyword(text, text.length());
        if (keyList.size() <= SHORT_WORD_LENGTH) {
            throw new FileAnalyseException("文件解析异常，没有可用词或者可用词太少");
        }
        // 找出所有词语
        List<Term> termList = HanLP.segment(text);
        List<String> allWord = termList.stream().map(term -> term.word).collect(Collectors.toList());
        // 用于存放关键词和词频
        Map<String,Integer> wordCount = new HashMap<>(keyList.size());
        // 遍历关键词 词频填入map中
        for (String s:keyList) {
            wordCount.put(s, Collections.frequency(allWord, s));
        }

        return wordCount;
    }

}
