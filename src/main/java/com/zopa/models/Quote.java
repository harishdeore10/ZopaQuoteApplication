package com.zopa.models;

import com.zopa.exceptions.LoanUnavailableException;
import com.zopa.services.CalculatorService;
import com.zopa.services.LenderService;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class Quote {
    private Integer requestedAmount;
    private LenderService lenderService;
    private List<Lender> lenders;
    private double rate;
    private double monthlyRepayment;
    private double totalRepayment;

    public Quote(Integer requestedAmount, String marketData) throws IOException, LoanUnavailableException {
        this.requestedAmount = requestedAmount;
        this.lenderService = new LenderService(marketData);
        this.lenders = addRequiredLenders();
        this.rate = CalculatorService.round(CalculatorService.getTotalRate(lenders).doubleValue() * 100, 1);
        this.monthlyRepayment = CalculatorService.round(calculateMonthlyRepayment().doubleValue(), 2);
        this.totalRepayment = CalculatorService.round(calculateTotalRepayment(), 2);
    }

    public Integer getRequestedAmount() {
        return requestedAmount;
    }

    public double getRate() {
        return rate;
    }

    public double getMonthlyRepayment() {
        return monthlyRepayment;
    }

    public double getTotalRepayment() {
        return totalRepayment;
    }

    private List<Lender> addRequiredLenders() throws LoanUnavailableException {
        return lenderService.getListOfLendersForQuote(requestedAmount);
    }

    private BigDecimal calculateTotalRate() {
        return CalculatorService.getTotalRate(lenders);
    }

    private BigDecimal calculateMonthlyRepayment() {
        return CalculatorService.getMonthlyPayments(requestedAmount, calculateTotalRate());
    }

    private double calculateTotalRepayment() {
        return CalculatorService.getTotalRepayment(requestedAmount, calculateTotalRate());
    }

    @Override
    public String toString() {
        return "Requested Amount: £" + getRequestedAmount() + "\n" +
                "Rate:" + getRate() + "% \n" +
                "Monthly Repayments: £" + getMonthlyRepayment() + " \n" +
                "Total Repayment: £" + getTotalRepayment();
    }
}
