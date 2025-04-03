package cp2024.solution;

import cp2024.circuit.*;
import cp2024.demo.SequentialSolver;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SmallTest3 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("TATATATTA");
        // Create the circuit structure
        CircuitNode leafNode1 = CircuitNode.mk(true, Duration.ofSeconds(300));
        CircuitNode leafNode2 = CircuitNode.mk(true, Duration.ofSeconds(3000));
        CircuitNode leafNode5 = CircuitNode.mk(true, Duration.ofSeconds(3000));
        
        CircuitNode andNode2 = CircuitNode.mk(NodeType.AND, leafNode1, leafNode2, leafNode5);
        CircuitNode leafNode3 = CircuitNode.mk(true, Duration.ofSeconds(300));
        CircuitNode leafNode4 = CircuitNode.mk(false, Duration.ofSeconds(5)); // One node is false

        CircuitNode andNode = CircuitNode.mk(NodeType.AND, andNode2, leafNode3, leafNode4);

        // Create the circuit
        Circuit circuit = new Circuit(andNode);

        // Create solvers
        CircuitSolver sequentialSolver = new SequentialSolver();
        CircuitSolver parallelSolver = new ParallelCircuitSolver();

        // Solve the circuit using ParallelCircuitSolver
        // Solve the circuit using SequentialSolver
        //CircuitValue sequentialResult = sequentialSolver.solve(circuit);

        // Print time before solving with SequentialSolver
        LocalDateTime startTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Start time for ParallelSolver: " + startTime.format(formatter));


        CircuitValue parallelResult = parallelSolver.solve(circuit);
        System.out.println("ParallelCircuitSolver result: " + parallelResult.getValue());
        // Print time after solving with SequentialSolver
        LocalDateTime endTime = LocalDateTime.now();
        System.out.println("End time for ParallelSolver: " + endTime.format(formatter));

        // Calculate and print the duration
        Duration duration = Duration.between(startTime, endTime);
        System.out.println("Duration for SequentialSolver: " + duration.toMillis() + " milliseconds");


        // Compare the results
/*        if (false == parallelResult.getValue()) {
            System.out.println("Results are consistent.");
        } else {
            System.out.println("Results are inconsistent.");
        }
*/      //parallelSolver.stop();
        //System.out.println("SequentialSolver stopped.");

    }
}
