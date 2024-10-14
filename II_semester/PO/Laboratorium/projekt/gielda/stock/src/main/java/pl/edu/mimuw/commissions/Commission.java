package pl.edu.mimuw.commissions;

import pl.edu.mimuw.investor.Investor;
import pl.edu.mimuw.investor.InvestorRequest;
import static java.lang.Math.min;

public abstract class Commission implements Comparable<Commission> {
    private final int turn;
    private final CommissionType type;
    private final String companyName;           // id of action
    protected int numberOfStock;
    private final int priceLimit;
    private final int applicationNumber;
    private final Investor investor;

    public Commission(InvestorRequest request, int applicationNumber, int turn) {
        this.turn = turn;
        this.type = request.type();
        this.companyName = request.companyName();
        this.priceLimit = request.priceLimit();
        this.applicationNumber = applicationNumber;
        if(request.investor() == null){
            throw new IllegalArgumentException("Investor cannot be null");
        }
        this.investor = request.investor();
        this.numberOfStock = request.quantity();
    }
    public CommissionType getType(){
        return type;
    }

    @Override
    public int compareTo(Commission other) {
        if(other.type != this.type){
            if(other.turn != this.turn){
                return this.turn - other.turn;
            }
            if(this.applicationNumber != other.applicationNumber){
                return this.applicationNumber - other.applicationNumber;
            } else {
                return this.numberOfStock - other.numberOfStock;
            }
        } else {
            if(other.priceLimit != this.priceLimit){
                if(type == CommissionType.SELL){
                    return this.priceLimit - other.priceLimit;
                } else if(type == CommissionType.BUY){
                    return other.priceLimit - this.priceLimit;
                } else {
                    throw new IllegalArgumentException("Commission type " + type + " not supported");
                }
            } else {
                if(other.turn != this.turn){
                    return this.turn - other.turn;
                }
                return this.applicationNumber - other.applicationNumber;
            }
        }
    }


    public int getTurn(){
        return turn;
    }

    @Override
    public String toString() {
        return "Commission{" +
                "turn=" + turn +
                ", type=" + type +
                ", companyName=" + companyName +
                ", priceLimit=" + priceLimit +
                ", applicationNumber=" + applicationNumber +
                ", numberOfStocks: " + numberOfStock +
                ", investor=" + investor.getName() +
                "}\n";
    }
    public abstract Commission updateCommission(int quantity, int turn);
    public boolean checkIfBarterPossible(int quantity){
        return true;
    };
    public int getNumberOfStock() {
        return numberOfStock;
    }
    public Investor getInvestor() {
        return investor;
    }
    public int getPriceLimit(){
        return priceLimit;
    }
    public static int smallerNumberOfQuanity(Commission commission1, Commission commission2) {
        return min(commission2.getNumberOfStock(),commission1.getNumberOfStock());
    }
}
