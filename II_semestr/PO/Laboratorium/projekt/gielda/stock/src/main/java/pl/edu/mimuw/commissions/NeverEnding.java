package pl.edu.mimuw.commissions;

import pl.edu.mimuw.investor.InvestorRequest;

public class NeverEnding extends TimedCommission {
    public NeverEnding(InvestorRequest request, int applicationNumber, int turn){
        super(request, applicationNumber, turn, Integer.MAX_VALUE);
    }

    @Override
    public NeverEnding updateCommission(int quantity, int turn){
        numberOfStock -= quantity;
        return this;
    }
}
