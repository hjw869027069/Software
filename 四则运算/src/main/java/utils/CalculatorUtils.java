package utils;

import cn.hutool.core.util.NumberUtil;

import java.util.*;

/**
 * @author HJW
 * @date 2022-09-28 01:26
 * 计算工具类
 */
public class CalculatorUtils {
    static final String LEFT_PARENTHESIS = "(";
    static final String RIGHT_PARENTHESIS = ")";
    static final String ADD = "+";
    static final String SUB = "-";
    static final String MUL = "*";
    static final String DIV = "÷";
    static final String OPERATORS = "+-*÷";

    /**
     * 计算表达式
     *
     * @param expression 表达式
     * @return 计算结果
     */
    public static Rational calculateExpression(List<Object> expression) {
        // 1. 将表达式转换为后缀表达式
        List<Object> suffixExpression = getSuffixExpression(expression);
        // 2. 计算后缀表达式
        return calculateSuffixExpression(suffixExpression);
    }

    /**
     * 计算后缀表达式
     *
     * @param suffixExpression 后缀表达式
     * @return 计算结果
     */
    public static Rational calculateSuffixExpression(List<Object> suffixExpression) {
        // 创建栈
        Stack<Rational> stack = new Stack<>();
        // 遍历后缀表达式
        for (Object item : suffixExpression) {
            // 如果是数字,直接入栈
            if (item instanceof Rational) {
                stack.push((Rational) item);
            } else {
                // 如果是运算符,从栈中取出两个数,进行运算,并将结果入栈
                Rational num2 = stack.pop();
                Rational num1 = stack.pop();
                Rational result;
                switch (item.toString()) {
                    case ADD:
                        result = num1.add(num2);
                        break;
                    case SUB:
                        result = num1.sub(num2);
                        break;
                    case MUL:
                        result = num1.multi(num2);
                        break;
                    case DIV:
                        result = num1.div(num2);
                        break;
                    default:
                        throw new RuntimeException("运算符有误");
                }
                stack.push(result);
            }
        }
        // 返回栈顶元素
        return stack.pop();
    }

    /**
     * 将中缀表达式转换为后缀表达式
     *
     * @param expression 中缀表达式
     * @return 后缀表达式
     */
    public static List<Object> getSuffixExpression(List<Object> expression) {
        // 创建两个栈,一个用于存放运算符,一个用于存放中间结果
        Stack<String> operatorStack = new Stack<>();
        Stack<Object> resultStack = new Stack<>();
        // 遍历中缀表达式
        expression.forEach(item -> {
            // 如果是数字,直接入栈
            if (item instanceof Rational) {
                resultStack.push(item);
            } else {
                // 如果是运算符,判断运算符栈是否为空
                if (operatorStack.isEmpty() || LEFT_PARENTHESIS.equals(item.toString())) {
                    // 如果为空或是左括号,直接入栈
                    operatorStack.push(item.toString());
                } else if (RIGHT_PARENTHESIS.equals(item.toString())) {
                    // 如果是右括号,将运算符栈中的运算符弹出,直到遇到左括号
                    while (!LEFT_PARENTHESIS.equals(operatorStack.peek())) {
                        resultStack.push(operatorStack.pop());
                    }
                    // 将左括号出栈
                    operatorStack.pop();
                } else {
                    // 如果不为空,判断运算符栈顶元素的优先级
                    String topOperator = operatorStack.peek();
                    // 如果栈顶元素优先级大于等于当前元素,将栈顶元素出栈,并将其入结果栈
                    if (!topOperator.equals(LEFT_PARENTHESIS) && getPriority(topOperator) >= getPriority(item.toString())) {
                        resultStack.push(operatorStack.pop());
                    }
                    // 将当前元素入栈
                    operatorStack.push(item.toString());
                }
            }
        });


        // 将运算符栈中的元素依次出栈,并入结果栈
        while (!operatorStack.isEmpty()) {
            resultStack.push(operatorStack.pop());
        }
        // 将结果栈中的元素依次出栈,并放入集合中
        List<Object> list = new ArrayList<>();
        while (!resultStack.isEmpty()) {

            list.add(resultStack.pop());
        }
        // 将集合反转
        Collections.reverse(list);
        // 返回集合
        return list;
    }

    /**
     * 获取运算符的优先级
     *
     * @param operator 运算符
     * @return 优先级
     */
    public static int getPriority(String operator) {
        switch (operator) {
            case ADD:
            case SUB:
            case LEFT_PARENTHESIS:
                return 1;
            case MUL:
            case DIV:
                return 2;
            default:
                throw new RuntimeException("运算符有误");
        }
    }

    /**
     * 生成运算题目
     *
     * @param questionNum   题目数量
     * @param questionRange 题目范围
     * @return 题目列表
     */
    public static List<List<Object>> generateQuestion(int questionNum, int questionRange) {
        // 创建集合,用于存放题目
        List<List<Object>> list = new ArrayList<>();
        // 生成题目
        for (int i = 0; i < questionNum; i++) {
            // 1.先生成一个随机数,用于判断题目的运算符个数和数字个数
            int operationNum = (int) (Math.random() * 3 + 1);
            int numberNum = operationNum + 1;
            // 2.根据运算符个数，生成运算符
            int[] integers = NumberUtil.generateRandomNumber(0, 4, operationNum);
            String[] operations = new String[operationNum];
            boolean flag = true;
            for (int j = 0; j < operationNum; j++) {
                char c = OPERATORS.charAt(integers[j]);
                // 如果是加法或者减法，判断是否需要加括号,为简化运算,只有加法和减法需要加括号，且只能加一次括号
                if (flag && (c == ADD.charAt(0) || c == SUB.charAt(0))) {
                    int b = (int) (Math.random() * 2);
                    if (b == 1) {
                        operations[j] = LEFT_PARENTHESIS + c + RIGHT_PARENTHESIS;
                        flag = false;
                    } else {
                        operations[j] = String.valueOf(c);
                    }
                } else {
                    operations[j] = c + "";
                }

            }
            // 3.根据数字个数,生成数字
            Rational[] numbers = new Rational[numberNum];
            for (int j = 0; j < numberNum; j++) {
                // 循环开始前，指定最小值范围和最大值范围
                int min = 0;
                int max = questionRange;
                // 检查除法和减法
                if (j > 0) {
                    if (operations[j - 1].equals(DIV)) {
                        // 如果是除法，则后一个一定要比前一个大,即为真分数
                        min = numbers[j - 1].getNumerator() / numbers[j - 1].getDenominator();
                    }
                    if (operations[j - 1].equals(SUB)) {
                        // 如果是减法，则后一个一定要比前一个小
                        max = numbers[j - 1].getNumerator() / numbers[j - 1].getDenominator();
                    }

                }
                // 生成随机数 判断是否是分数
                int isFraction = (int) (Math.random() * 2);
                int number = (int) (Math.random() * (max - min + 1) + min);
                if (isFraction == 1) {
                    // 生成分数
                    int denominator = (int) (Math.random() * 100 + 1);
                    int numerator = (int) (Math.random() * (max * denominator - min * denominator + 1) + min * denominator);

                    Rational rational = new Rational(numerator, denominator);
                    numbers[j] = rational;
                } else {
                    // 生成整数

                    numbers[j] = new Rational(number, 1);
                }
            }

            // 4.将运算符和数字交替放入字符串中
            List<Object> objects = new ArrayList<>();
            for (int j = 0; j < operationNum + numberNum; j++) {
                if (j % 2 == 0) {

                    if (j / 2 < operations.length && operations[j / 2].length() == 3) {
                        objects.add(operations[j / 2].charAt(0));
                        objects.add(numbers[j / 2]);
                        objects.add(operations[j / 2].charAt(1));
                        objects.add(numbers[j / 2 + 1]);
                        objects.add(operations[j / 2].charAt(2));
                        if (j / 2 + 1 < numbers.length - 1) {
                            objects.add(operations[j / 2 + 1]);
                        }
                        j += 3;
                    } else {
                        objects.add(numbers[j / 2]);
                    }
                } else {
                    objects.add(operations[j / 2]);
                }
            }
            // 5.将题目添加到集合中

            // 检查是否重复
            boolean hasSame = false;
            for (List<Object> objectList : list) {
                if(checkExpressSame(objectList,objects)){
                    hasSame = true;
                    break;
                }
            }
            if (calculateExpression(objects).getNumerator() > 0 && calculateExpression(objects).getDenominator() > 0 && !hasSame) {
                list.add(objects);
            } else {
                i--;
            }
        }
        return list;
    }

    /**
     * 检查题目是否重复
     *
     * @param express1 题目1
     * @param express2 题目2
     * @return 是否重复
     */
    public static boolean checkExpressSame(List<Object> express1, List<Object> express2) {
        boolean flag = true;
        // 如果结果不一样，肯定不一样
        if (!calculateExpression(express1).toString().equals(calculateExpression(express2).toString())) {
            return false;
        }
        // 如果长度一样，结果一样，那么就比较每个元素是否一样,直接转为后缀表达式
        List<Object> express1Suffix = getSuffixExpression(express1);
        List<Object> express2Suffix = getSuffixExpression(express2);
        if (express1Suffix.size() != express2Suffix.size()) {
            return false;
        }

        // 变换成其它的后缀表达式
        Object[] changeExpress1 = express1Suffix.toArray();
        Map<String, Integer> countOperator = new HashMap<>(2);
        countOperator.put(ADD, 0);
        countOperator.put(MUL, 0);
        // 如果比较完全一样，则直接返回true
        for (int i = 0; i < express1Suffix.size(); i++) {
            if (!express1Suffix.get(i).toString().equals(express2Suffix.get(i).toString())) {
                flag = false;
                break;
            }
        }
        if (flag) {
            return true;
        }

        // 暂时只考虑单层加法乘法和两层加法乘法，其它的情况概率非常低
        for (int i = 0; i < express1Suffix.size(); i++) {
            if (express1Suffix.get(i).equals(ADD) || express1Suffix.get(i).equals(MUL)) {
                countOperator.put(express1Suffix.get(i).toString(), countOperator.get(express1Suffix.get(i).toString()) + 1);
            }
        }

        // 单层加法乘法,两边交换一下
        if (countOperator.get(ADD) == 1) {
            for (int i = 0; i < express1Suffix.size(); i++) {
                if (express1Suffix.get(i).equals(ADD)) {
                    changeExpress1[i - 2] = express1Suffix.get(i - 1);
                    changeExpress1[i - 1] = express1Suffix.get(i - 2);
                }
            }
            for (int i = 0; i < express2Suffix.size(); i++) {
                if (!express2Suffix.get(i).toString().equals(changeExpress1[i].toString())) {
                    return false;
                }
            }
        }

        if (countOperator.get(MUL) == 1) {
            for (int i = 0; i < express1Suffix.size(); i++) {
                if (express1Suffix.get(i).equals(MUL)) {
                    changeExpress1[i - 2] = express1Suffix.get(i - 1);
                    changeExpress1[i - 1] = express1Suffix.get(i - 2);
                }
            }
            for (int i = 0; i < express2Suffix.size(); i++) {
                if (!express2Suffix.get(i).toString().equals(changeExpress1[i].toString())) {
                    return false;
                }
            }
        }

        // 记录加法乘法的位置
        int index = 0;
        if (countOperator.get(ADD) == 2) {
            // 两层加法乘法，碰到第一个加法乘法直接交换一下
            for (int i = 0; i < express1Suffix.size(); i++) {
                if (express1Suffix.get(i).equals(ADD)) {
                    index = i;
                    changeExpress1[i - 2] = express1Suffix.get(i - 1);
                    changeExpress1[i - 1] = express1Suffix.get(i - 2);
                    break;
                }
            }
            flag = true;
            // 比较一下
            for (int i = 0; i < express2Suffix.size(); i++) {
                if (!express2Suffix.get(i).toString().equals(changeExpress1[i].toString())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return true;
            }

            // 交换一次不行，再交换一次,当为连续的两个加法时，将远离的那个换到前面
            if (changeExpress1[index + 1].equals(ADD)) {
                changeExpress1[index - 3] = express1Suffix.get(index - 2);
                changeExpress1[index - 2] = express1Suffix.get(index - 1);
                changeExpress1[index - 1] = express1Suffix.get(index);
                changeExpress1[index] = express1Suffix.get(index - 3);
            }else {
                changeExpress1[index - 2] = express1Suffix.get(index + 1);
                changeExpress1[index - 1] = express1Suffix.get(index-2);
                changeExpress1[index] = express1Suffix.get(index - 1);
                changeExpress1[index + 1] = express1Suffix.get(index);
            }

            flag = true;

            // 比较一下
            for (int i = 0; i < express2Suffix.size(); i++) {
                if (!express2Suffix.get(i).toString().equals(changeExpress1[i].toString())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return true;
            }

            flag = true;
            if (changeExpress1[index + 1].equals(ADD)) {
                changeExpress1[index - 3] = express1Suffix.get(index - 1);
                changeExpress1[index - 2] = express1Suffix.get(index - 2);
                changeExpress1[index - 1] = express1Suffix.get(index);
                changeExpress1[index] = express1Suffix.get(index - 3);
            }else {
                changeExpress1[index - 2] = express1Suffix.get(index + 1);
                changeExpress1[index - 1] = express1Suffix.get(index-1);
                changeExpress1[index] = express1Suffix.get(index - 2);
                changeExpress1[index + 1] = express1Suffix.get(index);
            }

            // 比较一下
            for (int i = 0; i < express2Suffix.size(); i++) {
                if (!express2Suffix.get(i).toString().equals(changeExpress1[i].toString())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return true;
            }
        }



        index = 0;
        if (countOperator.get(MUL) == 2) {
            // 两层加法乘法，碰到第一个加法乘法直接交换一下
            for (int i = 0; i < express1Suffix.size(); i++) {
                if (express1Suffix.get(i).equals(MUL)) {
                    index = i;
                    changeExpress1[i - 2] = express1Suffix.get(i - 1);
                    changeExpress1[i - 1] = express1Suffix.get(i - 2);
                    break;
                }
            }
            flag = true;
            // 比较一下
            for (int i = 0; i < express2Suffix.size(); i++) {
                if (!express2Suffix.get(i).equals(changeExpress1[i])) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return true;
            }

            // 交换一次不行，再交换一次,当为连续的两个乘法时，将远离的那个换到前面
            if (changeExpress1[index + 1].equals(MUL)) {
                changeExpress1[index - 3] = express1Suffix.get(index - 2);
                changeExpress1[index - 2] = express1Suffix.get(index - 1);
                changeExpress1[index - 1] = express1Suffix.get(index);
                changeExpress1[index] = express1Suffix.get(index - 3);
            }else {
                changeExpress1[index - 2] = express1Suffix.get(index + 1);
                changeExpress1[index - 1] = express1Suffix.get(index-2);
                changeExpress1[index] = express1Suffix.get(index - 1);
                changeExpress1[index + 1] = express1Suffix.get(index);
            }

            flag = true;
            // 比较一下
            for (int i = 0; i < express2Suffix.size(); i++) {
                if (!express2Suffix.get(i).equals(changeExpress1[i])) {
                    flag = false;
                    break;
                }
            }
            return flag;
        }


        return false;
    }


}
