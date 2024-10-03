package pl.edu.mimuw.stock;

import pl.edu.mimuw.commissions.Commission;
import pl.edu.mimuw.commissions.CommissionType;
import pl.edu.mimuw.investor.Investor;

import java.util.PriorityQueue;

import static java.lang.Math.min;

public class CompanyStock {                 // every Company has its own stock
    private final String companyName;
    private final PriorityQueue<Commission> buyQueue;
    private final PriorityQueue<Commission> sellQueue;
    private int lastCommissionPrice;
    private int lastCommissionTurn = 0;

    public CompanyStock(String companyName, int lastCommissionPrice) {
        this.companyName = companyName;
        this.lastCommissionPrice = lastCommissionPrice;
        this.buyQueue = new PriorityQueue<>();
        this.sellQueue = new PriorityQueue<>();
    }

    public void addCommission(Commission commission){
        if (commission != null) {
            if (commission.getType() == CommissionType.BUY) {
                buyQueue.add(commission);
            }
            if (commission.getType() == CommissionType.SELL) {
                sellQueue.add(commission);
            }
        }
    }

    public int getLastCommissionPrice(){
        return lastCommissionPrice;
    }
    public int getLastCommissionTurn(){
        return lastCommissionTurn;
    }

    @Override
    public String toString() {
        return companyName +
                "\nBUY QUEUE:\n" +
                buyQueue.toString() +
                "\nSELL QUEUE:\n" +
                sellQueue.toString() + "\n";
    }

    public void calculate(int turn){
        PriorityQueue<Commission> endBuyQueue;
        PriorityQueue<Commission> endSellQueue;
        //here transactions after the turn are implemented
        Commission buy = buyQueue.peek();
        Commission sell = sellQueue.peek();
        while(!buyQueue.isEmpty() && !sellQueue.isEmpty() && (sell.getPriceLimit() <= buy.getPriceLimit())){
            System.out.println(buy.toString());
            System.out.println(sell.toString());
            int price = 0;
            buy = buyQueue.poll();
            sell = sellQueue.poll();
            if(buy.compareTo(sell) >= 0){
                price = buy.getPriceLimit();
            } else{
                price = sell.getPriceLimit();
            }
            Investor buyInvestor = buy.getInvestor();
            Investor sellInvestor = sell.getInvestor();
            int quantityOfBarter = Commission.smallerNumberOfQuanity(buy, sell);
            quantityOfBarter =
                    min(
                            buyInvestor.checkFunds(companyName ,price, quantityOfBarter, CommissionType.BUY),
                            sellInvestor.checkFunds(companyName, price, quantityOfBarter, CommissionType.SELL)
                    );
            if(buy.checkIfBarterPossible(quantityOfBarter) && sell.checkIfBarterPossible(quantityOfBarter)){
                if(quantityOfBarter != -1){
                    Commission tempBuy = buy.updateCommission(quantityOfBarter, turn);
                    Commission tempSell = sell.updateCommission(quantityOfBarter, turn);
                    buyInvestor.makeBarter(companyName ,price, quantityOfBarter, CommissionType.BUY);
                    sellInvestor.makeBarter(companyName, price, quantityOfBarter, CommissionType.SELL);
                    lastCommissionTurn = turn;
                    lastCommissionPrice = price;
                    if(tempSell != null){
                        if(tempSell.getNumberOfStock() != 0){
                            sellQueue.add(tempSell);
                        }
                    }
                    if(tempBuy != null){
                        if(tempBuy.getNumberOfStock() != 0) {
                            buyQueue.add(tempBuy);
                        }
                    }
                }
            }
            buy = buyQueue.peek();
            sell = sellQueue.peek();
        }
    }
}
