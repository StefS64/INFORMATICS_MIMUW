package pl.edu.mimuw.commissions;

import pl.edu.mimuw.investor.InvestorRequest;

public class FillOrKill extends Commission{
    public FillOrKill(InvestorRequest request, int applicationNumber, int turn){
        super(request, applicationNumber, turn);
    }
    @Override
    public FillOrKill updateCommission(int quantity, int turn){
        return null;
    }

    @Override
    public boolean checkIfBarterPossible(int quantity) {
        if(numberOfStock == quantity){ // this checks if fill or kill is a barter that connects only with one other commission
            return true;
        }else{
            return false;
        }
    }
}