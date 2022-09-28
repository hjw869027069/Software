import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.CalculatorUtils;
import utils.Rational;

import java.util.List;

public class MainTest {

    List<Object> expression;
    List<Object> test1;
    List<Object> test2;
    List<Object> test3;

    @BeforeEach
    void setUp() {
        // 1/2 + 1/3 * 1/4 - 1/5 ÷ 1/6 = 1/2 + 1/12 - 6/5 = 7/12 - 6/5 = 35/60 - 72/60 = -37/60
        expression = List.of(
                new Rational(1, 2),
                "+",
                new Rational(1, 3),
                "*",
                new Rational(1, 4),
                "-",
                new Rational(1, 5),
                "÷",
                new Rational(1, 6)
        );

        test1 = List.of(
                new Rational(3, 1),
                "+",
                "(",
                new Rational(2, 1),
                "+",
                new Rational(1, 1),
                ")"
        );

        test2 = List.of(
                new Rational(1, 1),
                "+",
                new Rational(2, 1),
                "+",
                new Rational(3, 1)
        );

        test3 = List.of(
                new Rational(3, 1),
                "+",
                new Rational(2, 1),
                "+",
                new Rational(1, 1)
        );


    }

    @Test
    void testResult() {
        String result = CalculatorUtils.calculateExpression(expression).toString();
        Assertions.assertEquals("-37/60", result);
    }


    @Test
    void testSuffixExpression() {
        List<Object> suffixExpression = CalculatorUtils.getSuffixExpression(test1);
        List<Object> suffixExpression1 = CalculatorUtils.getSuffixExpression(test2);
        List<Object> suffixExpression2 = CalculatorUtils.getSuffixExpression(test3);

        System.out.println(suffixExpression);
        System.out.println(suffixExpression1);
        System.out.println(suffixExpression2);


    }

    @Test
    void testCheckSame(){
        Assertions.assertTrue(CalculatorUtils.checkExpressSame(test1, test2));
        System.out.println(CalculatorUtils.checkExpressSame(test1, test2));
        Assertions.assertFalse(CalculatorUtils.checkExpressSame(test1, test3));
        System.out.println(CalculatorUtils.checkExpressSame(test1, test3));
    }

    @Test
    void testGenerateRandomExpression(){
        List<List<Object>> lists = CalculatorUtils.generateQuestion(10, 10);
        System.out.println(lists);

    }

}
