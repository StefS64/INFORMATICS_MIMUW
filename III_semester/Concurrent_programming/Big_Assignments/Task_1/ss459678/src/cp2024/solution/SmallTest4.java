package cp2024.solution;

import cp2024.circuit.*;
import cp2024.demo.SequentialSolver;
import cp2024.solution.ParallelCircuitSolver;

public class SmallTest4 {
    public static void main(String[] args) throws InterruptedException {
        // Create the circuit structure
        CircuitNode leafNode1 = CircuitNode.mk(true);
        CircuitNode leafNode2 = CircuitNode.mk(false);
        CircuitNode leafNode3 = CircuitNode.mk(true);
        CircuitNode ltNode = CircuitNode.mk(NodeType.LT, 3, leafNode1, leafNode2, leafNode3);
        CircuitNode notNode = CircuitNode.mk(NodeType.NOT, ltNode);

        // Create the circuit
        Circuit circuit = new Circuit(notNode);

        // Create solvers
        CircuitSolver sequentialSolver = new SequentialSolver();
        CircuitSolver parallelSolver = new ParallelCircuitSolver();

        // Solve the circuit using SequentialSolver
        CircuitValue sequentialResult = sequentialSolver.solve(circuit);
        System.out.println("SequentialSolver result: " + sequentialResult.getValue());

        // Solve the circuit using ParallelCircuitSolver
        CircuitValue parallelResult = parallelSolver.solve(circuit);
        System.out.println("ParallelCircuitSolver result: " + parallelResult.getValue());
        // Compare the results
        if (sequentialResult.getValue() == parallelResult.getValue()) {
            System.out.println("Results are consistent.");
        } else {
            System.out.println("Results are inconsistent.");
        }
        parallelSolver.stop();
    }
}
