package cp2024.solution;

import cp2024.circuit.*;
import cp2024.demo.SequentialSolver;
import java.time.Duration;

public class SmallTest3 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("TATATATTA");
        // Create the circuit structure
        CircuitNode leafNode1 = CircuitNode.mk(true, Duration.ofSeconds(5));
        CircuitNode leafNode2 = CircuitNode.mk(true, Duration.ofSeconds(5));
        CircuitNode leafNode3 = CircuitNode.mk(true, Duration.ofSeconds(1));
        CircuitNode leafNode4 = CircuitNode.mk(false); // One node is false

        CircuitNode andNode = CircuitNode.mk(NodeType.AND, leafNode1, leafNode2, leafNode3, leafNode4);

        // Create the circuit
        Circuit circuit = new Circuit(andNode);

        // Create solvers
        CircuitSolver sequentialSolver = new SequentialSolver();
        CircuitSolver parallelSolver = new ParallelCircuitSolver();

        // Solve the circuit using ParallelCircuitSolver
        CircuitValue parallelResult = parallelSolver.solve(circuit);
        System.out.println("ParallelCircuitSolver result: " + parallelResult.getValue());
        
        // Solve the circuit using SequentialSolver
        CircuitValue sequentialResult = sequentialSolver.solve(circuit);
        Thread.sleep(1000);

        System.out.println("SequentialSolver result: " + sequentialResult.getValue());

        // Compare the results
        if (sequentialResult.getValue() == parallelResult.getValue()) {
            System.out.println("Results are consistent.");
        } else {
            System.out.println("Results are inconsistent.");
        }
        parallelSolver.stop();
        //System.out.println("SequentialSolver stopped.");

    }
}
