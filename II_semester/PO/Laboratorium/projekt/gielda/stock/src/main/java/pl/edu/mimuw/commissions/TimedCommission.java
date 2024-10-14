package pl.edu.mimuw.commissions;

import pl.edu.mimuw.investor.InvestorRequest;

public class TimedCommission extends Commission {
    int TimeOfTermination;
    public TimedCommission(InvestorRequest request, int applicationNumber, int turn, int numberOfTurn) {
        super(request, applicationNumber, turn);
        if (numberOfTurn != -1) {
            this.TimeOfTermination = numberOfTurn;
        } else {
            this.TimeOfTermination = turn + request.numberOfTurns();
        }
    }
    @Override
    public Commission updateCommission(int quantity, int turn){
        if(turn > TimeOfTermination){
            return null;
        }
        numberOfStock -= quantity;
        if(numberOfStock == 0){
           return null;
        } else {
            return this;
        }
    }
}
