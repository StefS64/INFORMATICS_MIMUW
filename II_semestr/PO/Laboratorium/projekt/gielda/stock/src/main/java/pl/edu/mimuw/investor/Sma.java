package pl.edu.mimuw.investor;

import pl.edu.mimuw.MyRandom;
import pl.edu.mimuw.commissions.CommissionType;
import pl.edu.mimuw.commissions.ValidityTime;
import pl.edu.mimuw.stock.StockSystem;
import java.util.*;
import java.util.List;

public class Sma extends Investor {
    private final Map<String, Vector<Integer>> lastPrices = new HashMap<>();

    public Sma(int funds, Map<String, Integer> shares, String Name) {
        super(funds, shares, Name);
    }

    @Override
    public void yourTurn(int turn) {
        List<String> names = new ArrayList<>(Arrays.asList(stockSystem.availableCompanyNames()));
        Collections.shuffle(names);
        boolean checkIfOrderPlaced = false;

        for (String companyName : names) {
            if(!checkIfOrderPlaced) {
                int lastPrice = stockSystem.getPriceOfLastCommission(companyName);
                lastPrices.get(companyName).add(lastPrice);

                if (lastPrices.get(companyName).size() >= 10) {
                    double sma5 = calculateSMA(companyName, 5);
                    double sma10 = calculateSMA(companyName, 10);

                    if (lastPrices.get(companyName).size() > 10) {
                        double prevSma5 = calculateSMA(companyName, 5, lastPrices.get(companyName).size() - 2);
                        double prevSma10 = calculateSMA(companyName, 10, lastPrices.get(companyName).size() - 2);

                        if (prevSma5 <= prevSma10 && sma5 > sma10) {
                            placeOrder(companyName, CommissionType.BUY);
                        } else if (prevSma5 >= prevSma10 && sma5 < sma10) {
                            placeOrder(companyName, CommissionType.SELL);
                        }
                        checkIfOrderPlaced = true;
                    }
                }
            }
        }
    }

    private double calculateSMA(String companyName, int period) {
        return calculateSMA(companyName, period, lastPrices.get(companyName).size() - 1);
    }

    private double calculateSMA(String companyName, int period, int endIndex) {
        Vector<Integer> prices = lastPrices.get(companyName);
        if (endIndex + 1 < period) {
            throw new IllegalArgumentException("Not enough data to calculate SMA" + period);
        }
        double sum = 0;
        for (int i = endIndex; i > endIndex - period; i--) {
            sum += prices.get(i);
        }
        return sum / period;
    }

    private void placeOrder(String companyName, CommissionType commissionType) {
        int lastPrice = stockSystem.getPriceOfLastCommission(companyName);
        int priceLimit = MyRandom.rand(lastPrice - 10, lastPrice + 11);
        ValidityTime validityTime = ValidityTime.values()[MyRandom.rand(0, ValidityTime.values().length)];
        int quantity = MyRandom.rand(1, 100);
        int numberOfTurn = -1;

        if (validityTime == ValidityTime.TIMEDCOMMISION) {
            numberOfTurn = MyRandom.rand(0, 1000 - stockSystem.getNumberOfTurn());
        }

        InvestorRequest newRequest = new InvestorRequest(companyName, commissionType, quantity, priceLimit, validityTime, numberOfTurn, stockSystem, this);
        if (super.checkFunds(newRequest) != -1) {
            stockSystem.addCommission(newRequest);
        }
    }

    @Override
    public void setStockSystem(StockSystem stockSystem) {
        super.setStockSystem(stockSystem);
        String[] names = super.stockSystem.availableCompanyNames();
        for (String companyName : names) {
            lastPrices.put(companyName, new Vector<>());
        }
    }
}
