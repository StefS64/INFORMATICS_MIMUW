package pl.edu.mimuw.investor;

import pl.edu.mimuw.MyRandom;
import pl.edu.mimuw.commissions.Commission;
import pl.edu.mimuw.commissions.CommissionType;
import pl.edu.mimuw.commissions.ValidityTime;
import pl.edu.mimuw.stock.StockSystem;

import java.util.Map;
import java.util.Random;

public class RandomInvestor extends Investor{
    public RandomInvestor(int funds, Map<String, Integer> shares, String Name) {
        super(funds, shares, Name);
    }


    @Override
    public void yourTurn(int turn){
        String companyName = MyRandom.keyRand(super.stockSystem.availableCompanyNames());

        CommissionType[] buyOrSellTable = CommissionType.values();
        CommissionType  buyOrSell = buyOrSellTable[MyRandom.rand(1,3) - 1];

        int lastPrice = stockSystem.getPriceOfLastCommission(companyName);
        int priceLimit = MyRandom.rand(lastPrice-10,11+lastPrice);

        ValidityTime[] validityTimeTable = ValidityTime.values();
        ValidityTime validityTime = validityTimeTable[MyRandom.rand(0,validityTimeTable.length)];
        int quantity = MyRandom.rand(1,100);
        int numberOfTurn = -1;          // is important only for TIMEDCOMMISION

        if(validityTime == ValidityTime.TIMEDCOMMISION){
            numberOfTurn = MyRandom.rand(0,1000-stockSystem.getNumberOfTurn());
        }

        StockSystem stockSystem = super.stockSystem;
        Investor investor = this;
        InvestorRequest newRequest = new InvestorRequest(companyName, buyOrSell, quantity, priceLimit, validityTime, numberOfTurn, stockSystem, investor);
        if(super.checkFunds(newRequest) != -1) {
            stockSystem.addCommission(newRequest);
        }
    }
}
