package com.zopa.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuoteTest {

    Quote quote;

    @Before
    public void setUp() throws Exception {
        quote = new Quote(1000, "src/test/resources/test_data.csv");
    }

    @Test
    public void shouldCallCalculatorServiceToGetTotalRate() throws Exception {
        assertEquals(30.88, quote.getMonthlyRepayment(), 0);
    }

    @Test
    public void shouldCallCalculatorServiceToGetRate() throws Exception {
        assertEquals(7.0, quote.getRate(), 0);
    }

    @Test
    public void shouldCallCalculatorServiceToGetTotalRepayment() throws Exception {
        assertEquals(1111.58, quote.getTotalRepayment(), 0);
    }
}