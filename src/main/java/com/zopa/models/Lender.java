package com.zopa.models;

import java.math.BigDecimal;

public class Lender implements Comparable<Lender> {
    private String name;
    private BigDecimal rate;
    private Integer availableAmount;

    public Lender(String name, BigDecimal rate, Integer availableAmount) {
        this.name = name;
        this.rate = rate.setScale(3, BigDecimal.ROUND_HALF_UP);
        this.availableAmount = availableAmount;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public Integer getAvailableAmount() {
        return availableAmount;
    }

    @Override
    public int compareTo(Lender lender) {
        return getRate().compareTo(lender.getRate());
    }
}