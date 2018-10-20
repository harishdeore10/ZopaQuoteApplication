package com.zopa.services;

import com.zopa.models.Lender;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import com.zopa.constant.Constant;

public class CalculatorService {


    public static BigDecimal getTotalRate(List<Lender> lenders) {
        return lenders.stream().map(Lender::getRate).reduce(BigDecimal::add).get()
                .divide(BigDecimal.valueOf(lenders.size()), MathContext.DECIMAL64);
    }

    public static BigDecimal getMonthlyPayments(Integer requestedAmount, BigDecimal rate) {
        return getMonthlyRate(rate).multiply(BigDecimal.valueOf(requestedAmount))
                .divide(BigDecimal.valueOf(1)
                        .subtract((BigDecimal.valueOf(1)
                                .add(getMonthlyRate(rate))
                                .pow(-Constant.LOAN_LENGTH, MathContext.DECIMAL64))), RoundingMode.HALF_UP);
    }

    public static double getTotalRepayment(Integer requestedAmount, BigDecimal monthlyRate) {
        double totalRepayment = getMonthlyPayments(requestedAmount, monthlyRate)
                .multiply(BigDecimal.valueOf(Constant.LOAN_LENGTH)).doubleValue();

        return round(totalRepayment, 2);
    }

    static int getMaxLoanValue(List<Lender> lenders) {
        return lenders.stream().mapToInt(Lender::getAvailableAmount).sum();
    }

    private static BigDecimal getMonthlyRate(BigDecimal rate) {
        return rate.divide(new BigDecimal(12), 100, RoundingMode.HALF_UP);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal decimalValue = new BigDecimal(Double.toString(value));
        decimalValue = decimalValue.setScale(places, RoundingMode.HALF_UP);
        return decimalValue.doubleValue();
    }
}