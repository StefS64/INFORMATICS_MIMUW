package pl.edu.mimuw;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SimulationTest {

    @Test
    public void testValidInputFile() {
        String filename = "valid_input.txt";
        String filePath = getPathOfFile(filename);

        // Run simulation with valid input file
        Simulation simulation = new Simulation(filePath, 6);
        assertDoesNotThrow(() -> simulation.startSim());
    }

    @Test
    public void testInvalidFileType() {
        String filename = "invalid_file_type.txt";
        String filePath = getPathOfFile(filename);

        // Expecting IllegalArgumentException due to invalid investor type
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Simulation(filePath, 6));
        assertEquals("Unknown file format, error in line: X R R R R R", exception.getMessage());
    }

    @Test
    public void testDuplicateStockInStocksSection() {
        String filename = "duplicate_stock_in_stocks.txt";
        String filePath = getPathOfFile(filename);

        // Expecting IllegalArgumentException due to duplicate stock in stocks section
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Simulation(filePath, 6));
        assertEquals("Duplicate Stock!  Name: APL", exception.getMessage());
    }

    @Test
    public void testLongStockNameInStocksSection() {
        String filename = "long_stock_name_in_stocks.txt";
        String filePath = getPathOfFile(filename);

        // Expecting IllegalArgumentException due to long stock name in stocks section
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Simulation(filePath, 6));
        assertEquals("Name too long!", exception.getMessage());
    }

    @Test
    public void testDuplicateStockInWallet() {
        String filename = "duplicate_stock_in_wallet.txt";
        String filePath = getPathOfFile(filename);

        // Expecting IllegalArgumentException due to duplicate stock in wallet section
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Simulation(filePath, 6));
        assertEquals("Duplicate Stock! in wallet num:1 Name: APL", exception.getMessage());
    }

    @Test
    public void testLongStockNameInWallet() {
        String filename = "long_stock_name_in_wallet.txt";
        String filePath = getPathOfFile(filename);

        // Expecting IllegalArgumentException due to long stock name in wallet section
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Simulation(filePath, 6));
        assertEquals("Name too long!", exception.getMessage());
    }

    @Test
    public void testNumberOfInvestorsMismatch() {
        String filename = "number_of_investors_mismatch.txt";
        String filePath = getPathOfFile(filename);

        // Expecting IllegalArgumentException due to mismatch between number of investors and investor types
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Simulation(filePath, 6));
        assertEquals("Number of investors doesn't match number of types", exception.getMessage());
    }

    @Test
    public void testUnknownFileFormat() {
        String filename = "unknown_file_format.txt";
        String filePath = getPathOfFile(filename);

        // Expecting IllegalArgumentException due to unknown file format
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Simulation(filePath, 6));
        assertEquals("Unknown file format, error in line: Invalid Line", exception.getMessage());
    }

    // Helper method to get the absolute path of the test resources
    private String getPathOfFile(String filename) {
        Path resourceDirectory = Paths.get("src", "test","input_assertions_tests", filename);
        return resourceDirectory.toFile().getAbsolutePath();
    }
}
