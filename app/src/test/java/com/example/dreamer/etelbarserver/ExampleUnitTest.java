package com.example.dreamer.etelbarserver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private Order order;

    @Before
    public void init(){
        order = new Order();
    }

    @Test
    public void setStatus_isCorrect(){
        order.setStatus("paid");
        assertEquals("paid",order.getStatus());
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}