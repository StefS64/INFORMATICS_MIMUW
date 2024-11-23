package cp2024.solution;

import cp2024.circuit.*;
import cp2024.demo.SequentialSolver;

public class SmallTest2 {
    public static void main(String[] args) throws InterruptedException {
        // Create the circuit structure
        CircuitNode andNode = CircuitNode.mk(NodeType.AND, CircuitNode.mk(true), CircuitNode.mk(false));
        CircuitNode orNode = CircuitNode.mk(NodeType.OR, CircuitNode.mk(false), CircuitNode.mk(false), CircuitNode.mk(false), CircuitNode.mk(false));
        CircuitNode thresholdNode = CircuitNode.mk(NodeType.GT, 2, andNode, orNode, CircuitNode.mk(false));

        // Create the circuit
        Circuit circuit = new Circuit(thresholdNode);
        Circuit circuit2 = new Circuit(orNode);
        Circuit circuit3 = new Circuit(andNode);

        // Create solvers
        CircuitSolver sequentialSolver = new SequentialSolver();
        CircuitSolver parallelSolver = new ParallelCircuitSolver();
        // Solve the circuit using SequentialSolver
        CircuitValue sequentialResult = sequentialSolver.solve(circuit);
        System.out.println("SequentialSolver result: " + sequentialResult.getValue());

        // Solve the circuit using ParallelCircuitSolver
        CircuitValue parallelResult = parallelSolver.solve(circuit);
        CircuitValue parallelResult2 = parallelSolver.solve(circuit2);
        CircuitValue parallelResult3 = parallelSolver.solve(circuit3);
        System.out.println("ParallelCircuitSolver result GT: " + parallelResult.getValue());
        System.out.println("ParallelCircuitSolver result OR: " + parallelResult2.getValue());
        System.out.println("ParallelCircuitSolver result AND: " + parallelResult3.getValue());

        // Compare the results
        if (sequentialResult.getValue() == parallelResult.getValue()) {
            System.out.println("Results are consistent.");
        } else {
            System.out.println("Results are inconsistent.");
        }
        parallelSolver.stop();
    }
}
