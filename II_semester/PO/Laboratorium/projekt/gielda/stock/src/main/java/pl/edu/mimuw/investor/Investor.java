package pl.edu.mimuw.investor;

import pl.edu.mimuw.commissions.CommissionType;
import pl.edu.mimuw.stock.StockSystem;
import java.util.Map;

import static java.lang.Math.min;

public abstract class Investor {
    private final String name;
    private int funds;
    protected Map<String, Integer> shares;
    protected StockSystem stockSystem;
    public Investor(int funds, Map<String, Integer> shares, String name) {
        this.funds = funds;
        this.shares = shares;
        this.name = name;
    }
    public int getFunds() {
        return funds;
    }
    public String getName() {
        return name;
    }

    public void setStockSystem(StockSystem stockSystem) {
        this.stockSystem = stockSystem;
    }

    public abstract void yourTurn(int turn);

    @Override
    public String toString() {
        StringBuilder mapAsString = new StringBuilder(name + "\nfunds: "+ funds + "\nshares:\n"+"{");
        for (String key : shares.keySet()) {
            mapAsString.append(key + "=" + shares.get(key) + ", ");
        }
        mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");
        return mapAsString.toString();
    }


    public int checkFunds(String stockName, int price, int quantity, CommissionType type) {
        if(type == CommissionType.SELL){
            if(shares.containsKey(stockName) && shares.get(stockName) > 0 ){
                return min(shares.get(stockName), quantity);
            } else {
                return -1;
            }
        }
        else if(type == CommissionType.BUY){
            if( funds >= price * quantity ){
                if(!shares.containsKey(stockName)){
                    shares.put(stockName, 0);
                }
                return quantity;
            }else {
                int possibleQuantity = funds/price;
                if(possibleQuantity > 0){
                    return possibleQuantity;
                }
                return -1;
            }
        }else{
            throw new IllegalArgumentException("Type not buy or sell");
        }
    }


    public int checkFunds(InvestorRequest request) {
        return checkFunds(request.companyName(), request.priceLimit(),request.quantity(), request.type());
    }

    public void makeBarter(String stockName, int price, int quantity, CommissionType type){
        if(type == CommissionType.SELL) {
            funds += price * quantity;
            int newValue = shares.get(stockName) - quantity;
            shares.put(stockName, newValue);
        } else if (type == CommissionType.BUY) {
            funds -= price * quantity;
            int newValue = shares.get(stockName) + quantity;
            shares.put(stockName, newValue);
        }
    }

    public Map<String, Integer> getShares() {
        return shares;
    }
}
