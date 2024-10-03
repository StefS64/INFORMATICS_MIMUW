package pl.edu.mimuw;

import pl.edu.mimuw.investor.Investor;
import pl.edu.mimuw.investor.RandomInvestor;
import pl.edu.mimuw.investor.Sma;
import pl.edu.mimuw.stock.CompanyStock;
import pl.edu.mimuw.stock.StockSystem;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Simulation {
    private boolean printMode = false;
    private final List<Investor> investors;
    private int numberOfInvestors;
    private final StockSystem stockSystem;
    private final int numberOfTurns;
    private String[] investorType;
    Map<String, CompanyStock> stocks = new HashMap<>();

    public Simulation(String filepath, int numberOfTurns) {
        investors = new ArrayList<>();
        parseFile(filepath);
        this.numberOfTurns = numberOfTurns;
        stockSystem  = new StockSystem(stocks, investors);
        for (Investor investor : investors) {
            investor.setStockSystem(stockSystem);
        }
    }

    public void startSim() {
        System.out.println("Starting SiMulation");
        for (int i = 0; i < numberOfTurns; i++) {
            stockSystem.runTurn();
            if(printMode) {
                printState();
            }
        }
        printInvestorWallets();
    }


    private void parseFile(String filepath){
        int countOfInvestors = 0;
        int countOfLines = 0;
        try (Scanner scan = new Scanner(new FileReader(filepath))) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                line = line.trim();
                if (!(line.startsWith("#") || line.isEmpty())) {
                    if (line.matches("^[RS ]+$")) {      // linia zawiera tylko R, S i spacje
                        if(countOfLines != 0){
                            throw new IllegalArgumentException("Wrong line order");
                        }
                        parseInvestors(line);
                    } else if (line.matches("^\\w+:\\d+( \\w+:\\d+)*$")) { // linia zawiera ceny akcji
                        if(countOfLines != 1){
                            throw new IllegalArgumentException("Wrong line order");
                        }
                        parseStocks(line);
                    } else if (line.matches("^\\d+.( \\w+:\\d+)*$")) {        // linia zawiera portfele
                        if(countOfLines != 2 + countOfInvestors){
                            throw new IllegalArgumentException("Wrong line order");
                        }
                        if(countOfInvestors >= numberOfInvestors){
                            throw new IllegalArgumentException("Number of investors doesn't match number of types of investors");
                        }
                        parseInitialWallet(line, countOfInvestors);
                        countOfInvestors++;
                    }else {
                        throw new IllegalArgumentException("Unknown file format, error in line: " + line);
                    }
                    countOfLines++;
                }
            }
            if(countOfInvestors != numberOfInvestors){
                throw new IllegalArgumentException("Number of investors doesn't match number of types");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private void parseInvestors(String line) {
         investorType = line.split(" ");
         numberOfInvestors = investorType.length;
    }

    private void parseStocks(String line) {
        String[] items = line.split(" ");

        for (String item : items) {
            String[] stockInfo = item.split(":");
            String name = stockInfo[0];
            if(name.length() >= 6){
                throw new IllegalArgumentException("Name too long!");
            }
            int price = Integer.parseInt(stockInfo[1]);
            if(stocks.containsKey(name)){
                throw new IllegalArgumentException("Duplicate Stock!  Name: " + name);
            }
            stocks.put(name, new CompanyStock(name, price));
        }
    }

    private void parseInitialWallet(String line, int num) {
        String[] items = line.split(" ");
        int cash = Integer.parseInt(items[0]);


        Map<String, Integer> walletStocks = new HashMap<>();
        for (int i = 1; i < items.length; i++) {
            String[] stockInfo = items[i].split(":");
            String name = stockInfo[0];
            if(name.length() >= 6){
                throw new IllegalArgumentException("Name too long!");
            }
            int quantity = Integer.parseInt(stockInfo[1]);
            if(walletStocks.containsKey(name)){
                throw new IllegalArgumentException("Duplicate Stock! in wallet num:" + num + " Name: " + name);
            }
            walletStocks.put(name, quantity);
        }
        if(Objects.equals(investorType[num], "R")) {
            investors.add(new RandomInvestor(cash, walletStocks, String.valueOf (num)));
        } else if( Objects.equals(investorType[num], "S")) {
            investors.add(new Sma(cash, walletStocks, String.valueOf (num)));
        } else {
            throw new IllegalArgumentException("Invalid investor type");
        }
    }

    void printState(){
        System.out.println(stockSystem.toString());
    }

    void printInvestorWallets(){
        System.out.println("WALLETS:\n");
        for(Investor investor : investors) {
                System.out.println(investor.toString());
        }
    }
    public Map<String, Integer> numberOfStockOfCompanys() {
        Map<String, Integer> stocksSum = new HashMap<>();

        for (Investor investor : investors) {
            Map<String, Integer> investorShares = investor.getShares();
            for (Map.Entry<String, Integer> entry : investorShares.entrySet()) {
                String company = entry.getKey();
                int shares = entry.getValue();
                stocksSum.put(company, stocksSum.getOrDefault(company, 0) + shares);
            }
        }
        return stocksSum;
    }

    public void setPrintMode(boolean printMode) {
        this.printMode = printMode;
    }
}