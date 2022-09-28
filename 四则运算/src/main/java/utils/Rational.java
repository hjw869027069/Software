package utils;

import cn.hutool.core.util.NumberUtil;

/**
 * @author HJW
 * 方便计算的有理数类
 */
public class Rational {

    private int numerator;
    private int denominator;


    public Rational(int numerator, int denominator) {
        setNumeratorAndDenominator(numerator, denominator);
    }

    /**
     * 设置分子和分母
     *
     * @param a 分子
     * @param b 分母
     */
    void setNumeratorAndDenominator(int a, int b) {
        // 计算最大公约数
        int c = NumberUtil.divisor(Math.abs(a), Math.abs(b));
        // 约分
        numerator = a / c;
        denominator = b / c;

        if (numerator < 0 && denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
    }

    int getNumerator() {
        return numerator;
    }

    int getDenominator() {
        return denominator;
    }

    /**
     * 加法运算
     *
     * @param r 另一个有理数
     * @return 结果
     */
    Rational add(Rational r) {
        int a = r.getNumerator();
        int b = r.getDenominator();
        int newNumerator = numerator * b + denominator * a;
        int newDenominator = denominator * b;
        return new Rational(newNumerator, newDenominator);
    }

    /**
     * 减法运算
     *
     * @param r 另一个有理数
     * @return 结果
     */
    Rational sub(Rational r) {
        int a = r.getNumerator();
        int b = r.getDenominator();
        int newNumerator = numerator * b - denominator * a;
        int newDenominator = denominator * b;
        return new Rational(newNumerator, newDenominator);
    }

    /**
     * 乘法运算
     *
     * @param r 另一个有理数
     * @return 结果
     */
    Rational multi(Rational r) {
        int a = r.getNumerator();
        int b = r.getDenominator();
        int newNumerator = numerator * a;
        int newDenominator = denominator * b;
        return new Rational(newNumerator, newDenominator);
    }

    /**
     * 除法运算
     *
     * @param r 另一个有理数
     * @return 结果
     */
    Rational div(Rational r) {
        int a = r.getNumerator();
        int b = r.getDenominator();
        int newNumerator = numerator * b;
        int newDenominator = denominator * a;
        return new Rational(newNumerator, newDenominator);
    }

    /**
     * 重写toString方法
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        if (numerator == 0) {
            return "0";
        } else if (denominator == 1) {
            return String.valueOf(numerator);
        } else if (denominator == -1) {
            return String.valueOf(-numerator);
        } else if (numerator <= denominator) {
            return numerator + "/" + denominator;
        }else {
            int a = numerator / denominator;
            int b = numerator % denominator;
            return a + "'" + b + "/" + denominator;
        }
    }
}
