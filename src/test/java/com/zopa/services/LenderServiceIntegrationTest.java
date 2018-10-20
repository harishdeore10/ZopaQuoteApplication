package com.zopa.services;

import com.zopa.exceptions.LoanUnavailableException;
import com.zopa.models.Lender;
import com.zopa.services.LenderService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class LenderServiceIntegrationTest {
    private LenderService service;
    private List<Lender> lenders;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        service = new LenderService("src/test/resources/test_data.csv");
        lenders = service.getLenders();
    }

    @Test
    public void shouldStoreLendersFromCSVInLendersList() throws Exception {
        Lender bob = lenders.get(0);
        assertEquals("Bob", bob.getName());
        assertEquals(BigDecimal.valueOf(0.075), bob.getRate());
        assertEquals(Integer.valueOf("640"), bob.getAvailableAmount());

        Lender jane = lenders.get(1);
        assertEquals("Jane", jane.getName());
        assertEquals(BigDecimal.valueOf(0.069), jane.getRate());
        assertEquals(Integer.valueOf("480"), jane.getAvailableAmount());
    }

    @Test
    public void shouldThrowErrorIfRequestedAmountExceedsLenderTotal() throws Exception {
        Integer requestedAmount = 80000;
        exception.expect(LoanUnavailableException.class);
        service.getListOfLendersForQuote(requestedAmount);
    }

    @Test
    public void shouldReturnListOfMemberWithLowestRateIfTheyHaveEnoughToCoverQuote() throws Exception {
        Lender jane = lenders.get(1);
        Integer requestedAmount = 100;
        List<Lender> expectedLender = Collections.singletonList(jane);
        List<Lender> requiredLender = service.getListOfLendersForQuote(requestedAmount);
        assertEquals(expectedLender, requiredLender);
    }

    @Test
    public void shouldGetAListOfLendersWithLowestRatesToCoverAQuote() throws Exception {
        Lender jane = lenders.get(1);
        Lender fred = lenders.get(2);
        Integer requestedAmount = 1000;
        List<Lender> expectedLenders = Arrays.asList(jane, fred);
        List<Lender> requiredLenders = service.getListOfLendersForQuote(requestedAmount);
        assertEquals(expectedLenders, requiredLenders);
    }
}