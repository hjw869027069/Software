import cn.hutool.core.date.DateUtil;
import exceptions.FileAnalyseException;
import exceptions.NotFoundFileException;
import util.CalculationUtils;
import util.CommonUtils;

import java.util.Map;

/**
 * @author HJW
 * @date 2022-09-22 21:58
 * 主函数
 */
public class Main {
    static final int CORRECT_ARGS = 3;
    public static void main(String[] args){

        // 读取并解析参数
        if (args.length != CORRECT_ARGS) {
            throw new IllegalArgumentException("参数个数不正确");
        }

        // 先解析文件 处理分词
        Map<String, Integer> originWordCount = null;
        Map<String, Integer> compareWordCount = null;
        try {
            originWordCount = CommonUtils.analyseString(CommonUtils.readFileToStr(args[0]));
            compareWordCount = CommonUtils.analyseString(CommonUtils.readFileToStr(args[1]));
        } catch (FileAnalyseException | NotFoundFileException e) {
            e.printStackTrace();
        }

        // 获取simHash值,并计算相似度,保留两位小数
        String simHash1 = CalculationUtils.calculateSimHash(originWordCount);
        String simHash2 = CalculationUtils.calculateSimHash(compareWordCount);

        double result = CalculationUtils.getSimilarity(simHash1, simHash2);
        String format = String.format("相似度为：%.2f", result);


        String writeFileContent = "---------------------------------------" + "\n" +
                "原文件：" + args[0] + "\n" +
                "对比文件：" + args[1] + "\n" +
                format + "\n" +
                "比较时间为：" + DateUtil.now() + "\n";
        ;
        CommonUtils.writeFile(args[2],writeFileContent);



    }
}
