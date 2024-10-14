package pl.edu.mimuw.stock;

import pl.edu.mimuw.commissions.*;
import pl.edu.mimuw.investor.Investor;
import pl.edu.mimuw.investor.InvestorRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StockSystem {
    int NumberOfTurn = 0;
    int commissionNumber = 0;
    Map<String,CompanyStock> companyStocks;
    List<Investor> investors;
    public StockSystem( Map<String,CompanyStock> companyStocks, List<Investor> investors ) {
        this.companyStocks = companyStocks;
        this.investors = investors;
    }
    public void runTurn(){
        Collections.shuffle(investors);
        for (Investor investor : investors) {
            investor.yourTurn(NumberOfTurn);
        }
        for (Map.Entry<String, CompanyStock> entry : companyStocks.entrySet()) {
            CompanyStock companyStock = entry.getValue();
            companyStock.calculate(NumberOfTurn);
        }
        NumberOfTurn++;
    }

    public String toString(){
        return "Turn Number: " + NumberOfTurn + "\n" +
                "Company stocks:\n " + companyStocks + "\n";
    }

    public int getNumberOfTurn() {
        return NumberOfTurn;
    }
    public int getPriceOfLastCommission(String companyName) {
        return companyStocks.get(companyName).getLastCommissionPrice();
    }

    public String[] availableCompanyNames() {
        return companyStocks.keySet().toArray(new String[0]);
    }

    public void addCommission(InvestorRequest request){
        if(request.time() == ValidityTime.TIMEDCOMMISION){
            companyStocks.get(request.companyName()).addCommission(new TimedCommission(request, commissionNumber, NumberOfTurn, -1));
        } else if(request.time() == ValidityTime.FILLORKILL){
            companyStocks.get(request.companyName()).addCommission(new FillOrKill(request, commissionNumber, NumberOfTurn));
        } else if (request.time() == ValidityTime.NEVERENDING){
            companyStocks.get(request.companyName()).addCommission(new NeverEnding(request, commissionNumber, NumberOfTurn));
        } else if(request.time() == ValidityTime.IMMIDIATE){
            companyStocks.get(request.companyName()).addCommission(new Immidiate(request, commissionNumber, NumberOfTurn));
        } else {
            throw new IllegalStateException("Unsupported request validity time type");
        }
        commissionNumber++;
    }
}
