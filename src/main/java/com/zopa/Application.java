package com.zopa;

import com.zopa.constant.Constant;
import com.zopa.exceptions.LoanUnavailableException;

import java.io.IOException;

public class Application {

    private static final int MINIMUM_LOAN = 1000, MAXIMUM_LOAN = 15000, VALID = 100;





    public static void main(String[] args) throws IOException {
        try {
            String csvFile = args[0];
            Integer loanAmount = Integer.valueOf(args[1]);

            if (checkLoanAmount(loanAmount)) {
                com.zopa.models.Quote quote = new com.zopa.models.Quote(loanAmount, csvFile);
                System.out.println(quote);
            }
        } catch (NumberFormatException e) {
            System.out.println(Constant.AMOUNT_TOO_HIGH_OR_LOW);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(Constant.INVALID_INPUT);
        } catch (LoanUnavailableException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean checkLoanAmount(double loanAmount) {
        if (loanAmount < MINIMUM_LOAN || loanAmount > MAXIMUM_LOAN || loanAmount % VALID != 0) {
            System.out.println(Constant.AMOUNT_TOO_HIGH_OR_LOW);
            return false;
        }
        return true;
    }
}
