import com.hankcs.hanlp.HanLP;
import exceptions.NotFoundFileException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MainTest {
    static String simpleWord = "今天是星期天，天气晴，今天晚上我要去看电影。";
    static String simpleCopyWord = "今天是周天，天气晴朗，我晚上要去看电影。";

    @BeforeEach
    void init(){


    }


    /**
     * 测试查看分词里的方法
     */
    @Test
    void testAnalysis(){
        List<String> keywords = HanLP.extractKeyword(simpleWord,10);
        List<String> keywords1 = HanLP.extractKeyword(simpleCopyWord,10);
        List<String> summaries = HanLP.extractSummary(simpleWord,10);
        List<String> summaries1 = HanLP.extractSummary(simpleCopyWord,10);

        System.out.println(keywords);
        System.out.println(keywords1);
        System.out.println(summaries);
        System.out.println(summaries1);

        Assertions.assertTrue(keywords.size()>0);
        Assertions.assertTrue(keywords1.size()>0);
        Assertions.assertTrue(summaries.size()>0);
        Assertions.assertTrue(summaries1.size()>0);


    }


    /**
     * 测试读取文件并看看分词效果
     */
    @Test
    void testReadFile(){
        String string = null;
        try {
            string = Utils.readFileToStr("C:\\Users\\HJW\\Desktop\\测试文本 (1)\\orig.txt");
        } catch (NotFoundFileException e) {
            e.printStackTrace();
            assert false;
        }
        System.out.println(HanLP.extractKeyword(string,string.length()));
    }

    /**
     * 测试读取不存在的文件
     */
    @Test
    void testReadFileNotFound(){
        assertThrows(NotFoundFileException.class,()->{
            try {
                Utils.readFileToStr("C:\\Users\\HJW\\Desktop\\测试文本 (1)\\orig1.txt");
            } catch (NotFoundFileException e) {
                e.printStackTrace();
                throw e;
            }
        });
    }

}
