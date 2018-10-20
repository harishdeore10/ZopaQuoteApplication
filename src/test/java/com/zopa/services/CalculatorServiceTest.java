package com.zopa.services;

import com.zopa.models.Lender;
import com.zopa.services.CalculatorService;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class CalculatorServiceTest {
    private List<Lender> lenders = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        lenders.add(new Lender("Bob", BigDecimal.valueOf(0.075), 640));
        lenders.add(new Lender("Jane", BigDecimal.valueOf(0.069), 480));
        lenders.add(new Lender("Fred", BigDecimal.valueOf(0.071), 520));
    }

    @Test
    public void shouldGetMaximumPossibleLoanValueForLenders() throws Exception {
        assertEquals(1640, CalculatorService.getMaxLoanValue(lenders));
    }

    @Test
    public void shouldGetTotalRateForListOfLenders() throws Exception {
        lenders.remove(0);
        double expectedRate = 0.07;
        assertEquals(expectedRate, CalculatorService.round(CalculatorService
                .getTotalRate(lenders).doubleValue(), 2), 0);
    }

    @Test
    public void shouldCalculateMonthlyPaymentsFromRateAndRequestedAmount() throws Exception {
        Integer requestedAmount = 1000;
        BigDecimal rate = BigDecimal.valueOf(0.07);
        double expectedMonthlyPayment = 30.88;
        double actualMonthlyPaymentsToTwoDecimalPlaces = CalculatorService
                .round(CalculatorService.getMonthlyPayments(requestedAmount, rate)
                        .doubleValue(), 2);

        assertEquals(expectedMonthlyPayment, actualMonthlyPaymentsToTwoDecimalPlaces, 0);
    }

    @Test
    public void shouldCalculateTotalRepaymentOverSpecifiedLoanPeriod() throws Exception {
        Integer requestedAmount = 1000;
        BigDecimal rate = BigDecimal.valueOf(0.07);
        double expectedTotalRepayment = 1111.58;
        double actualTotalRepayment = CalculatorService.getTotalRepayment(requestedAmount, rate);

        assertEquals(expectedTotalRepayment, actualTotalRepayment, 0);
    }
}