package com.example.mycalculatormvp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void testCalculator() {
        CalculatorListener  listener = new MockCalculatorListener();
        SimpleCalculator calc = new SimpleCalculator(listener);

        calc.setOperand(72);
        calc.setOperator("+");
        calc.setOperand(6);

        assertEquals(78,((MockCalculatorListener) listener).result);

        calc.setOperator("-");
        calc.setOperand(3);

        assertEquals(75,((MockCalculatorListener) listener).result);

    }
}