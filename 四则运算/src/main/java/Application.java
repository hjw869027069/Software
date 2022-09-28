import cn.hutool.core.date.DateUtil;
import utils.CalculatorUtils;
import utils.CommonUtils;
import utils.Rational;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HJW
 * 主方法启动程序
 */
public class Application {
    static String absolutePath = new File("").getAbsolutePath();
    public static void main(String[] args) {
        if (args.length != 4) {
            throw new RuntimeException("请在命令行中正确输入参数");
        }
        int questionNum;
        int questionRange;

        if (args[0].equals("-n") && args[2].equals("-r")){
            try {
                questionNum = Integer.parseInt(args[1]);
                questionRange = Integer.parseInt(args[3]);

                if (questionNum <= 0 || questionRange <= 0) {
                    throw new RuntimeException("参数必须大于0");
                }
            }catch (Exception e){
                throw new RuntimeException("参数必须为数字");
            }

            List<List<Object>> lists = CalculatorUtils.generateQuestion(questionNum, questionRange);
            StringBuilder sb = new StringBuilder();
            StringBuilder answer = new StringBuilder();
            for (int i = 0; i < lists.size(); i++) {
                answer.append(i+1).append(". ")
                                .append(CalculatorUtils.calculateExpression(lists.get(i)).toString())
                                        .append("\n");
                sb.append(i + 1).append(". ");
                for (int j = 0; j < lists.get(i).size(); j++) {
                    sb.append(lists.get(i).get(j));
                }
                sb.append("\n");
            }
            sb.append("-----------------生成时间：").append(DateUtil.now())
                    .append(" 生成题目数量：").append(questionNum).append("\n");
            answer.append("-----------------生成时间：").append(DateUtil.now())
                    .append(" 生成题目数量：").append(questionNum).append("\n");

            CommonUtils.writeFile(absolutePath + "\\Exercises.txt", sb.toString());
            CommonUtils.writeFile(absolutePath + "\\Answers.txt", answer.toString());

            System.out.println("已把生成的题目放入Exercises.txt文件中");
            System.out.println("已把生成的答案放入Answers.txt文件中");

        }else if (args[0].equals("-e") && args[2].equals("-a")){

            String fileContent = CommonUtils.readFileToStr(absolutePath + "\\" + args[1]);
            String fileAnswer = CommonUtils.readFileToStr(absolutePath + "\\" + args[3]);
            List<List<Object>> lists = CommonUtils.analyzeExpression(fileContent);
            StringBuilder sb = new StringBuilder();
            List<Integer> right = new ArrayList<>();
            List<Integer> wrong = new ArrayList<>();

            for (int i = 0; i < lists.size(); i++) {
                List<Object> objects = lists.get(i);
                Rational rational = CalculatorUtils.calculateExpression(objects);
                String answer = fileAnswer.split("\n")[i].split("\\. ")[1];

                System.out.println("第" + (i+1) + "题计算结果：" + answer);
                System.out.println("第" + (i+1) + "题答案：" + rational.toString());

                if (answer.equals(rational.toString())) {
                    right.add(i+1);
                }else {
                    wrong.add(i+1);
                }
            }
            sb.append("Correct: ").append(right.size()).append(" (");
            for (int i = 0; i < right.size(); i++) {
                sb.append(right.get(i));
                if (i != right.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")").append("\n");
            sb.append("Wrong: ").append(wrong.size()).append(" (");
            for (int i = 0; i < wrong.size(); i++) {
                sb.append(wrong.get(i));
                if (i != wrong.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")").append("\n");
            CommonUtils.writeFile(absolutePath + "\\Grade.txt", sb.toString());
            System.out.println("已把生成的统计结果放入Grade.txt文件中");
        }
    }
}
