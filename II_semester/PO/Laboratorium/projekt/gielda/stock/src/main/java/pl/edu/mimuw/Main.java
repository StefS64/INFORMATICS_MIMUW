package pl.edu.mimuw;

public class Main {

    public static void main(String[] args) {
        Simulation simulation = new Simulation(args[0], Integer.parseInt(args[1]));
        simulation.setPrintMode(true);
        simulation.startSim();
    }
}
