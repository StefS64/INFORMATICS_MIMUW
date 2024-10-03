package pl.edu.mimuw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.mimuw.commissions.CommissionType;
import pl.edu.mimuw.investor.Investor;
import pl.edu.mimuw.investor.RandomInvestor;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InvestorTest {
    private Investor investor;
    private Map<String, Integer> shares;

    @BeforeEach
    void setUp() {
        shares = new HashMap<>();
        shares.put("TestCompany", 100);
        investor = new RandomInvestor(1000, shares, "TestInvestor") {
            @Override
            public void yourTurn(int turn) {
                //does nothing when testing
            }
        };
    }

    @Test
    void testGetFunds() {
        assertEquals(1000, investor.getFunds());
    }

    @Test
    void testGetName() {
        assertEquals("TestInvestor", investor.getName());
    }

    @Test
    void testCheckFundsSell() {
        int result = investor.checkFunds("TestCompany", 10, 50, CommissionType.SELL);
        assertEquals(50, result);
    }

    @Test
    void testCheckFundsSellInsufficientShares() {
        int result = investor.checkFunds("TestCompany", 10, 150, CommissionType.SELL);
        assertEquals(100, result);
    }

    @Test
    void testCheckFundsSellNoShares() {
        int result = investor.checkFunds("UnknownCompany", 10, 50, CommissionType.SELL);
        assertEquals(-1, result);
    }

    @Test
    void testCheckFundsBuy() {
        int result = investor.checkFunds("TestCompany", 10, 50, CommissionType.BUY);
        assertEquals(50, result);
    }

    @Test
    void testCheckFundsBuyInsufficientFunds() {
        int result = investor.checkFunds("TestCompany", 10, 200, CommissionType.BUY);
        assertEquals(100, result);
    }

    @Test
    void testMakeBarterSell() {
        investor.makeBarter("TestCompany", 10, 50, CommissionType.SELL);
        assertEquals(1500, investor.getFunds());
        assertEquals(50, (int) investor.getShares().get("TestCompany"));
    }

    @Test
    void testMakeBarterBuy() {
        investor.makeBarter("TestCompany", 10, 50, CommissionType.BUY);
        assertEquals(500, investor.getFunds());
        assertEquals(150, (int) investor.getShares().get("TestCompany"));
    }

    @Test
    void testToString() {
        String expectedString = "TestInvestor\nfunds: 1000\nshares:\n{TestCompany=100}";
        assertEquals(expectedString, investor.toString());
    }
}
