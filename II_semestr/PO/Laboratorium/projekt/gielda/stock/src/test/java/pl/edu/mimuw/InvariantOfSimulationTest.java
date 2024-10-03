package pl.edu.mimuw;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class InvariantOfSimulationTest {
    @Test
    public void onlyRandInvariantTest() {
        String filename = "random_investors_invariant.txt";

        Simulation simulation = new Simulation(getPathOfFile(filename),100);
        Map<String, Integer> before = simulation.numberOfStockOfCompanys();
        simulation.startSim();
        assertEquals(before, simulation.numberOfStockOfCompanys());
    }
    @Test
    public void mixedMoreRandInvariantTest() {
        String filename = "mixed_more_company's_invariant.txt";

        Simulation simulation = new Simulation(getPathOfFile(filename),100);
        simulation.setPrintMode(true);
        Map<String, Integer> before = simulation.numberOfStockOfCompanys();
        simulation.startSim();
        assertEquals(before, simulation.numberOfStockOfCompanys());
    }
    @Test
    public void onlySmaInvariantTest() {
        String filename = "sma_investors_invariant.txt";

        Simulation simulation = new Simulation(getPathOfFile(filename),100);
        Map<String, Integer> before = simulation.numberOfStockOfCompanys();
        simulation.startSim();
        assertEquals(before, simulation.numberOfStockOfCompanys());
    }
    @Test
    public void mixedInvariantTest() {
        String filename = "mixed_investors_invariant.txt";

        Simulation simulation = new Simulation(getPathOfFile(filename),100);
        Map<String, Integer> before = simulation.numberOfStockOfCompanys();
        simulation.startSim();
        assertEquals(before, simulation.numberOfStockOfCompanys());
    }

    // Helper method to get the absolute path of the test resources
    private String getPathOfFile(String filename) {
        Path resourceDirectory = Paths.get("src", "test","invariant_tests", filename);
        return resourceDirectory.toFile().getAbsolutePath();
    }
}
