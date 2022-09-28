package utils;

import cn.hutool.core.io.FileUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author HJW
 * @date 2022-09-20 21:58
 * 代码所需要用到的所有非计算的工具类
 */
public class CommonUtils {

    /**
     * 从文件中读取文本(为方便直接使用工具包)
     * @param filePath 文件路径
     * @return 文本
     */
    public static String readFileToStr(String filePath) {
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            throw new RuntimeException("该绝对路径的文件不存在");
        }
    }

    /**
     * 解析文本内容，转化为表达式
     * @param fileContent 文件内容
     * @return 表达式
     */
    public static List<List<Object>> analyzeExpression(String fileContent) {
        List<List<Object>> lists = new ArrayList<>();
        String[] split = fileContent.split("\n");
        for (int i = 0; i < split.length - 1; i++) {
            String question = split[i].split(". ")[1];
            List<Object> list = new ArrayList<>();
            List<String> rational = new ArrayList<>();
            for (int j = 0; j < question.length(); j++) {
                if (question.charAt(j) == '+') {
                    list.add("+");
                } else if (question.charAt(j) == '-') {
                    list.add("-");
                } else if (question.charAt(j) == '*') {
                    list.add("*");
                } else if (question.charAt(j) == '÷') {
                    list.add("÷");
                } else if (question.charAt(j) == '(') {
                    list.add("(");
                } else if (question.charAt(j) == ')') {
                    list.add(")");
                } else {
                    int index1 = j;
                    int index2 = -1;
                    int index3 = -1;
                    int index4 = -1;
                    boolean flag = false;

                    for (int k = j; k < question.length(); k++) {
                        if (question.charAt(k) == '\''){
                            index2 = k;
                        }
                        if (question.charAt(k) == '/'){
                            index3 = k;
                        }
                        if (question.charAt(k) == '+' || question.charAt(k) == '-' || question.charAt(k) == '*' || question.charAt(k) == '÷' || question.charAt(k) == '(' || question.charAt(k) == ')') {
                            index4 = k;
                            j = k - 1;
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        index4 = question.length();
                        j = question.length() - 1;
                    }

                    if (index1 == index2){
                        break;
                    }

                    if (index2 != -1 && index3 != -1) {
                        int num1  = Integer.parseInt(question.substring(index1, index2));
                        int num2 = Integer.parseInt(question.substring(index2 + 1, index3));
                        int num3 = Integer.parseInt(question.substring(index3 + 1, index4));
                        Rational rational1 = new Rational(num1*num3 + num2, num3);
                        list.add(rational1);
                    } else if (index2 == -1 && index3 != -1) {
                        int num1 = Integer.parseInt(question.substring(index1, index3));
                        int num3 = Integer.parseInt(question.substring(index3 + 1, index4));
                        Rational rational1 = new Rational(num1, num3);
                        list.add(rational1);
                    }else {
                        int num1 = Integer.parseInt(question.substring(index1, index4));
                        Rational rational1 = new Rational(num1, 1);
                        list.add(rational1);
                    }
                }
            }
            lists.add(list);
        }


        return lists;
    }


    /**
     * 覆盖写入文件
     * @param filePath 文件路径
     * @param content 内容
     */
    public static void writeFile(String filePath, String content) {
        FileUtil.writeString(content, filePath, "utf-8");
    }

}
