package com.zopa.services;

import com.zopa.exceptions.LoanUnavailableException;
import com.zopa.models.Lender;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LenderService {
    private List<Lender> lenders;

    public LenderService(String csvFile) throws IOException {
        lenders = loadDataFromCSVFile(csvFile);
    }

    public List<Lender> getLenders() {
        return lenders;
    }

    public List<Lender> getListOfLendersForQuote(Integer requestedAmount) throws LoanUnavailableException {
        Integer remainingLoan = requestedAmount;
        List<Lender> requiredLenders = new ArrayList<>();
        Integer availableAmountToLend = CalculatorService.getMaxLoanValue(lenders);

        if (requestedAmount > availableAmountToLend) {
            throw new LoanUnavailableException("Sorry, it is not possible to provide a loan at this time.");
        }

        sortLendersByLowestRate(lenders);

        for (Lender lender : lenders) {
            if (remainingLoan <= 0) {
                break;
            }
            if (lender.getAvailableAmount() > remainingLoan) {
                requiredLenders.add(lender);
                remainingLoan = 0;

            } else {
                remainingLoan -= lender.getAvailableAmount();
                requiredLenders.add(lender);
            }
        }
        return requiredLenders;
    }

    private void sortLendersByLowestRate(List<Lender> lenders) {
        Collections.sort(lenders);
    }

    private List<Lender> loadDataFromCSVFile(String csvFilePath) throws IOException {
        List<Lender> lenders;
        File file = new File(csvFilePath);
        InputStream inputStream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        lenders = reader.lines().skip(1).map(mapToLender).collect(Collectors.toList());
        reader.close();

        return lenders;
    }

    private Function<String, Lender> mapToLender = (String line) -> {
        String[] details = line.split(",");

        String name = details[0];
        BigDecimal rate = new BigDecimal(details[1]);
        Integer availableAmount = Integer.valueOf(details[2]);

        return new Lender(name, rate, availableAmount);
    };
}
