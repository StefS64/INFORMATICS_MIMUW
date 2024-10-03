package pl.edu.mimuw.commissions;

import pl.edu.mimuw.investor.InvestorRequest;

public class Immidiate extends TimedCommission {
    public Immidiate(InvestorRequest request, int applicationNumber, int turn){
        super(request, applicationNumber, turn, turn);
    }
}
