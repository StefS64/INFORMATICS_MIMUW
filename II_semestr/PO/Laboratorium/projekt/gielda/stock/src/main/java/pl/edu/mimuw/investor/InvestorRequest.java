package pl.edu.mimuw.investor;

import pl.edu.mimuw.commissions.CommissionType;
import pl.edu.mimuw.commissions.ValidityTime;
import pl.edu.mimuw.stock.StockSystem;

public record InvestorRequest(
        String companyName,
        CommissionType type,
        int quantity,
        int priceLimit,
        ValidityTime time,
        int numberOfTurns,
        StockSystem stockSystem,
        Investor investor
){
}

// record which allows Investor to safely request the commission without needing to set the turn and application number.