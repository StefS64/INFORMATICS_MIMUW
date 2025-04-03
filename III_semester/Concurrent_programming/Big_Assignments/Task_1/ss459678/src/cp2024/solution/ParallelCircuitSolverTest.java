package cp2024.solution;

import cp2024.circuit.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ParallelCircuitSolverTest {

    @Test
    public void testBasicSimpleCircuit() {
        // Test for a simple circuit with a single leaf node
        CircuitNode leafNode = CircuitNode.mk(false, Duration.ofSeconds(3));
        Circuit circuit = new Circuit(leafNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertFalse(value.getValue(), "The circuit value should be true.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void testMultipleNodes() {
        // Test for multiple nodes with AND operation
        CircuitNode leafNode1 = new SleepyLeafNode(true);
        CircuitNode leafNode2 = new SleepyLeafNode(false);
        CircuitNode andNode = CircuitNode.mk(NodeType.AND, leafNode1, leafNode2);
        Circuit circuit = new Circuit(andNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertFalse(value.getValue(), "The AND operation should result in false.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void testLeafNodeWithDelay() {
        // Test for leaf nodes with a delay (simulating a "sleepy" node)
        CircuitNode leafNode1 = new SleepyLeafNode(false, Duration.ofMillis(200));
        CircuitNode leafNode2 = new SleepyLeafNode(true, Duration.ofMillis(100));
        CircuitNode orNode = CircuitNode.mk(NodeType.OR, leafNode1, leafNode2);
        Circuit circuit = new Circuit(orNode);
        CircuitSolver solver = new ParallelCircuitSolver();

        long startTime = System.currentTimeMillis();
        CircuitValue value = solver.solve(circuit);
        try {
            assertTrue(value.getValue(), "The OR operation should result in true.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
        long endTime = System.currentTimeMillis();

        // Since it's a parallel solver, we check if it's completed within a reasonable time
        long duration = endTime - startTime;
        assertTrue(duration < 500, "The operation should finish within a reasonable time (parallelism should be handled).");
    }

    @Test
    public void testThresholdNode() {
        // Test for a threshold node (e.g., GT)
        CircuitNode leafNode1 = new SleepyLeafNode(true);
        CircuitNode thresholdNode = CircuitNode.mk(NodeType.GT, 10, leafNode1);
        Circuit circuit = new Circuit(thresholdNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertFalse(value.getValue(), "The threshold node should evaluate to false (threshold 10 and value 1).");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void testLargeCircuit() {
        for (int j = 0; j < 1000; j ++) {        // Stress test for large circuits with many nodes
            List<CircuitNode> nodes = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                nodes.add(new SleepyLeafNode(i % 2 == 0)); // alternating true/false
            }

            CircuitNode andNode = nodes.getFirst(); // Start with the first node
            for (int i = 1; i < nodes.size(); i++) {
                andNode = CircuitNode.mk(NodeType.AND, andNode, nodes.get(i));
            }

            Circuit circuit = new Circuit(andNode);
            ParallelCircuitSolver solver = new ParallelCircuitSolver();

            long startTime = System.currentTimeMillis();
            CircuitValue value = solver.solve(circuit);
            try {
                assertFalse(value.getValue(), "The AND operation with alternating true/false nodes should result in false.");
            } catch (InterruptedException e) {
                fail("The operation was interrupted.");
            }
            long endTime = System.currentTimeMillis();

            long duration = endTime - startTime;
            System.out.println(duration);
            assertTrue(duration < 1000, "The operation should complete within a reasonable time for a large circuit.");
        }
    }

    @Test
    public void testSingleNodeCircuit() {
        // Edge case: Circuit with a single node set to false
        CircuitNode singleFalseNode = new SleepyLeafNode(false);
        Circuit circuit = new Circuit(singleFalseNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertFalse(value.getValue(), "A single node set to false should return false.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void testComplexNestedCircuit() {
        // Complex circuit with nested operations (e.g., (A AND (B OR C)) AND (NOT D))
        CircuitNode leafA = new SleepyLeafNode(true);
        CircuitNode leafB = new SleepyLeafNode(false);
        CircuitNode leafC = new SleepyLeafNode(true);
        CircuitNode leafD = new SleepyLeafNode(false);

        CircuitNode orNode = CircuitNode.mk(NodeType.OR, leafB, leafC);  // (B OR C)
        CircuitNode andNode1 = CircuitNode.mk(NodeType.AND, leafA, orNode);  // A AND (B OR C)
        CircuitNode notNode = CircuitNode.mk(NodeType.NOT, leafD);  // NOT D
        CircuitNode andNode2 = CircuitNode.mk(NodeType.AND, andNode1, notNode);  // (A AND (B OR C)) AND (NOT D)

        Circuit circuit = new Circuit(andNode2);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertTrue(value.getValue(), "The complex nested circuit should evaluate to true.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void testIFNodeCircuit() {
        // Test for an IF node: IF(A, B, C) where A is true, B is false, and C is true
        CircuitNode condition = new SleepyLeafNode(true);  // A
        CircuitNode trueBranch = new SleepyLeafNode(false);  // B
        CircuitNode falseBranch = new SleepyLeafNode(true);  // C

        CircuitNode ifNode = CircuitNode.mk(NodeType.IF, condition, trueBranch, falseBranch);
        Circuit circuit = new Circuit(ifNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertFalse(value.getValue(), "The IF node should return false because condition is true and trueBranch is false.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void testThresholdNode_GT() {
        // Test for a GT (greater than) threshold node with multiple arguments
        CircuitNode[] nodes = {
                new SleepyLeafNode(true),  // Value 1
                new SleepyLeafNode(true),  // Value 2
                new SleepyLeafNode(false),  // Value 3
                new SleepyLeafNode(true),  // Value 4
                new SleepyLeafNode(false)   // Value 5
        };
        // Create a GT node with a threshold of 2 (true if more than 2 nodes are true)
        CircuitNode thresholdNode = CircuitNode.mk(NodeType.GT, 2, nodes);
        Circuit circuit = new Circuit(thresholdNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertTrue(value.getValue(), "The GT node should evaluate to true as more than 2 nodes are true.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void testThresholdNode_LT() {
        // Test for an LT (less than) threshold node with multiple arguments
        CircuitNode[] nodes = {
                new SleepyLeafNode(false),
                new SleepyLeafNode(false),
                new SleepyLeafNode(false),
                new SleepyLeafNode(true)
        };
        // Create an LT node with a threshold of 2 (true if less than 2 nodes are true)
        CircuitNode thresholdNode = CircuitNode.mk(NodeType.LT, 2, nodes);
        Circuit circuit = new Circuit(thresholdNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertTrue(value.getValue(), "The LT node should evaluate to true as less than 2 nodes are true.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void testDelayedNodesWithComplexCircuit() {
        // Test for complex circuit with delays on each node
        CircuitNode leafA = new SleepyLeafNode(true, Duration.ofMillis(300));
        CircuitNode leafB = new SleepyLeafNode(true, Duration.ofMillis(200));
        CircuitNode leafC = new SleepyLeafNode(false, Duration.ofMillis(100));

        CircuitNode andNode = CircuitNode.mk(NodeType.AND, leafA, leafB);  // A AND B
        CircuitNode orNode = CircuitNode.mk(NodeType.OR, andNode, leafC);  // (A AND B) OR C

        Circuit circuit = new Circuit(orNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        long startTime = System.currentTimeMillis();
        CircuitValue value = solver.solve(circuit);
        try {
            assertTrue(value.getValue(), "The delayed complex circuit should evaluate to true.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        assertTrue(duration < 800, "The operation should handle delays properly (within reasonable time using parallel execution).");
    }

    @Test
    public void testInterruptHandling() {
        // Test to verify interrupt handling in the parallel solver
        CircuitNode leafNode1 = new SleepyLeafNode(true, Duration.ofMillis(500));
        CircuitNode leafNode2 = new SleepyLeafNode(false, Duration.ofMillis(500));
        CircuitNode andNode = CircuitNode.mk(NodeType.AND, leafNode1, leafNode2);
        Circuit circuit = new Circuit(andNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        Thread solverThread = new Thread(() -> {
            CircuitValue value = solver.solve(circuit);
            try {
                assertFalse(value.getValue(), "The AND operation should return false.");
            } catch (InterruptedException e) {
                fail("The operation was interrupted.");
            }
        });

        solverThread.start();
        try {
            Thread.sleep(600);  // Give it some time to finish
            solver.stop();  // Interrupt the solver
            solverThread.join();
        } catch (InterruptedException e) {
            fail("The test thread was interrupted.");
        }
    }

    @Test
    public void testDeeplyNestedCircuit() {
        // Deeply nested circuit: ((((A AND B) OR C) AND (D OR E)) OR NOT(F))
        CircuitNode leafA = new SleepyLeafNode(true);
        CircuitNode leafB = new SleepyLeafNode(false);
        CircuitNode leafC = new SleepyLeafNode(true);
        CircuitNode leafD = new SleepyLeafNode(false);
        CircuitNode leafE = new SleepyLeafNode(true);
        CircuitNode leafF = new SleepyLeafNode(false);

        CircuitNode andNode1 = CircuitNode.mk(NodeType.AND, leafA, leafB);  // A AND B
        CircuitNode orNode1 = CircuitNode.mk(NodeType.OR, andNode1, leafC);  // (A AND B) OR C
        CircuitNode orNode2 = CircuitNode.mk(NodeType.OR, leafD, leafE);  // D OR E
        CircuitNode andNode2 = CircuitNode.mk(NodeType.AND, orNode1, orNode2);  // ((A AND B) OR C) AND (D OR E)
        CircuitNode notNode = CircuitNode.mk(NodeType.NOT, leafF);  // NOT F
        CircuitNode finalNode = CircuitNode.mk(NodeType.OR, andNode2, notNode);  // (((A AND B) OR C) AND (D OR E)) OR NOT(F)

        Circuit circuit = new Circuit(finalNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertTrue(value.getValue(), "The deeply nested circuit should evaluate to true.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void testHighlyConnectedComplexCircuit() {
        // Complex circuit with multiple levels and combinations: (A OR (B AND (C OR D))) AND (E OR (NOT(F AND G)))
        CircuitNode leafA = new SleepyLeafNode(false);
        CircuitNode leafB = new SleepyLeafNode(true);
        CircuitNode leafC = new SleepyLeafNode(true);
        CircuitNode leafD = new SleepyLeafNode(false);
        CircuitNode leafE = new SleepyLeafNode(true);
        CircuitNode leafF = new SleepyLeafNode(true);
        CircuitNode leafG = new SleepyLeafNode(false);

        CircuitNode orNode1 = CircuitNode.mk(NodeType.OR, leafC, leafD);  // C OR D
        CircuitNode andNode1 = CircuitNode.mk(NodeType.AND, leafB, orNode1);  // B AND (C OR D)
        CircuitNode orNode2 = CircuitNode.mk(NodeType.OR, leafA, andNode1);  // A OR (B AND (C OR D))
        CircuitNode andNode2 = CircuitNode.mk(NodeType.AND, leafF, leafG);  // F AND G
        CircuitNode notNode = CircuitNode.mk(NodeType.NOT, andNode2);  // NOT (F AND G)
        CircuitNode orNode3 = CircuitNode.mk(NodeType.OR, leafE, notNode);  // E OR NOT(F AND G)
        CircuitNode finalNode = CircuitNode.mk(NodeType.AND, orNode2, orNode3);  // (A OR (B AND (C OR D))) AND (E OR NOT(F AND G))

        Circuit circuit = new Circuit(finalNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertTrue(value.getValue(), "The highly connected complex circuit should evaluate to true.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void testMultiLayerCircuitWithThresholds() {
        // Multi-layer circuit involving threshold nodes: (GT(A, B, C, D, threshold=2) AND LT(E, F, G, H, threshold=3)) OR NOT(I)
        CircuitNode leafA = new SleepyLeafNode(true);
        CircuitNode leafB = new SleepyLeafNode(false);
        CircuitNode leafC = new SleepyLeafNode(true);
        CircuitNode leafD = new SleepyLeafNode(true);
        CircuitNode leafE = new SleepyLeafNode(true);
        CircuitNode leafF = new SleepyLeafNode(false);
        CircuitNode leafG = new SleepyLeafNode(false);
        CircuitNode leafH = new SleepyLeafNode(true);
        CircuitNode leafI = new SleepyLeafNode(false);

        CircuitNode gtNode = CircuitNode.mk(NodeType.GT, 2, leafA, leafB, leafC, leafD);  // GT threshold: >2 of A, B, C, D
        CircuitNode ltNode = CircuitNode.mk(NodeType.LT, 3, leafE, leafF, leafG, leafH);  // LT threshold: <3 of E, F, G, H
        CircuitNode andNode = CircuitNode.mk(NodeType.AND, gtNode, ltNode);  // GT(...) AND LT(...)
        CircuitNode notNode = CircuitNode.mk(NodeType.NOT, leafI);  // NOT(I)
        CircuitNode finalNode = CircuitNode.mk(NodeType.OR, andNode, notNode);  // (GT(...) AND LT(...)) OR NOT(I)

        Circuit circuit = new Circuit(finalNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertTrue(value.getValue(), "The multi-layer circuit with thresholds should evaluate to true.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void testCircuitWithMultipleIFConditions() {
        // Circuit with nested IF conditions: IF(A, IF(B, C, D), IF(E, F, G))
        CircuitNode leafA = new SleepyLeafNode(true);
        CircuitNode leafB = new SleepyLeafNode(false);
        CircuitNode leafC = new SleepyLeafNode(true);
        CircuitNode leafD = new SleepyLeafNode(false);
        CircuitNode leafE = new SleepyLeafNode(true);
        CircuitNode leafF = new SleepyLeafNode(true);
        CircuitNode leafG = new SleepyLeafNode(false);

        CircuitNode ifNode1 = CircuitNode.mk(NodeType.IF, leafB, leafC, leafD);  // IF(B, C, D)
        CircuitNode ifNode2 = CircuitNode.mk(NodeType.IF, leafE, leafF, leafG);  // IF(E, F, G)
        CircuitNode finalNode = CircuitNode.mk(NodeType.IF, leafA, ifNode1, ifNode2);  // IF(A, IF(B, C, D), IF(E, F, G))

        Circuit circuit = new Circuit(finalNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertFalse(value.getValue(), "The circuit with multiple nested IF conditions should evaluate to false.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void testLargeScaleComplexCircuit() {
        // A large and complex circuit involving various operations
        CircuitNode[] trueNodes = new CircuitNode[50];
        CircuitNode[] falseNodes = new CircuitNode[50];

        // Initialize a mix of true and false nodes with delays
        for (int i = 0; i < 50; i++) {
            trueNodes[i] = new SleepyLeafNode(true, Duration.ofMillis(10 * i));
            falseNodes[i] = new SleepyLeafNode(false, Duration.ofMillis(10 * (50 - i)));
        }

        CircuitNode andNode = CircuitNode.mk(NodeType.AND, trueNodes);  // AND all true nodes
        CircuitNode orNode = CircuitNode.mk(NodeType.OR, falseNodes);  // OR all false nodes
        CircuitNode finalNode = CircuitNode.mk(NodeType.AND, andNode, CircuitNode.mk(NodeType.NOT, orNode));  // AND(AND(trueNodes), NOT(OR(falseNodes)))

        Circuit circuit = new Circuit(finalNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertTrue(value.getValue(), "The large-scale complex circuit should evaluate to true.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void testInterruptionDuringSimpleEvaluation() {
        CircuitNode leafA = new SleepyLeafNode(true, Duration.ofSeconds(5));
        CircuitNode leafB = new SleepyLeafNode(true, Duration.ofSeconds(5));
        CircuitNode andNode = CircuitNode.mk(NodeType.AND, leafA, leafB);

        Circuit circuit = new Circuit(andNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        Thread solverThread = new Thread(() -> {
            try {
                CircuitValue result = solver.solve(circuit);
                result.getValue();  // This should be interrupted
                fail("Expected an InterruptedException due to interruption.");
            } catch (InterruptedException e) {
                // Expected interruption, test passes
            }
        });

        solverThread.start();

        // Interrupt after 1 second
        try {
            Thread.sleep(1000);
            solver.stop();  // Use stop() to interrupt the operation
            solverThread.join();
        } catch (InterruptedException e) {
            fail("Test was interrupted unexpectedly.");
        }
    }

    @Test
    public void testInterruptionDuringComplexEvaluation() {
        CircuitNode leafA = new SleepyLeafNode(true, Duration.ofSeconds(2));
        CircuitNode leafB = new SleepyLeafNode(false, Duration.ofSeconds(3));
        CircuitNode leafC = new SleepyLeafNode(true, Duration.ofSeconds(2));
        CircuitNode leafD = new SleepyLeafNode(false, Duration.ofSeconds(4));

        CircuitNode andNode1 = CircuitNode.mk(NodeType.AND, leafA, leafB);
        CircuitNode andNode2 = CircuitNode.mk(NodeType.AND, leafC, leafD);
        CircuitNode finalNode = CircuitNode.mk(NodeType.OR, andNode1, andNode2);

        Circuit circuit = new Circuit(finalNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        Thread solverThread = new Thread(() -> {
            try {
                CircuitValue result = solver.solve(circuit);
                result.getValue();  // This should be interrupted
                fail("Expected an InterruptedException due to interruption.");
            } catch (InterruptedException e) {
                // Expected interruption, test passes
            }
        });

        solverThread.start();

        // Interrupt after 3 seconds
        try {
            Thread.sleep(3000);
            solver.stop();
            solverThread.join();
        } catch (InterruptedException e) {
            fail("Test was interrupted unexpectedly.");
        }
    }

    @Test
    public void testInterruptionMidwayThroughLargeCircuit() {
        CircuitNode[] trueNodes = new CircuitNode[20];
        CircuitNode[] falseNodes = new CircuitNode[20];

        for (int i = 0; i < 20; i++) {
            trueNodes[i] = new SleepyLeafNode(true, Duration.ofMillis(200 * (i + 1)));
            falseNodes[i] = new SleepyLeafNode(false, Duration.ofMillis(200 * (20 - i)));
        }

        CircuitNode orNode1 = CircuitNode.mk(NodeType.AND, trueNodes);
        CircuitNode andNode1 = CircuitNode.mk(NodeType.OR, falseNodes);
        CircuitNode finalNode = CircuitNode.mk(NodeType.OR, orNode1, andNode1);

        Circuit circuit = new Circuit(finalNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        Thread solverThread = new Thread(() -> {
            try {
                CircuitValue result = solver.solve(circuit);
                result.getValue();  // This should be interrupted
                fail("Expected an InterruptedException due to interruption.");
            } catch (InterruptedException e) {
                // Expected interruption, test passes
            }
        });

        solverThread.start();

        // Interrupt after 2 seconds
        try {
            Thread.sleep(2000);
            solver.stop();
            solverThread.join();
        } catch (InterruptedException e) {
            fail("Test was interrupted unexpectedly.");
        }
    }

    @Test
    public void testInterruptionHandlingInNestedIFConditions() {
        CircuitNode leafA = new SleepyLeafNode(false, Duration.ofSeconds(2));
        CircuitNode leafB = new SleepyLeafNode(true, Duration.ofSeconds(3));
        CircuitNode leafC = new SleepyLeafNode(true, Duration.ofSeconds(4));
        CircuitNode leafD = new SleepyLeafNode(false, Duration.ofSeconds(1));

        CircuitNode ifNode1 = CircuitNode.mk(NodeType.IF, leafA, leafB, leafC);
        CircuitNode ifNode2 = CircuitNode.mk(NodeType.IF, leafD, leafB, leafC);
        CircuitNode finalNode = CircuitNode.mk(NodeType.OR, ifNode1, ifNode2);

        Circuit circuit = new Circuit(finalNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        Thread solverThread = new Thread(() -> {
            try {
                CircuitValue result = solver.solve(circuit);
                result.getValue();  // This should be interrupted
                fail("Expected an InterruptedException due to interruption.");
            } catch (InterruptedException e) {
                // Expected interruption, test passes
            }
        });

        solverThread.start();

        // Interrupt after 2.5 seconds
        try {
            Thread.sleep(2500);
            solver.stop();
            solverThread.join();
        } catch (InterruptedException e) {
            fail("Test was interrupted unexpectedly.");
        }
    }

    @Test
    public void testInterruptionOnThresholdEvaluation() {
        CircuitNode leafA = new SleepyLeafNode(true, Duration.ofSeconds(2));
        CircuitNode leafB = new SleepyLeafNode(true, Duration.ofSeconds(3));
        CircuitNode leafC = new SleepyLeafNode(false, Duration.ofSeconds(2));
        CircuitNode leafD = new SleepyLeafNode(false, Duration.ofSeconds(4));
        CircuitNode leafE = new SleepyLeafNode(true, Duration.ofSeconds(1));

        CircuitNode gtNode = CircuitNode.mk(NodeType.GT, 3, leafA, leafB, leafC, leafD, leafE);
        CircuitNode finalNode = CircuitNode.mk(NodeType.NOT, gtNode);

        Circuit circuit = new Circuit(finalNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        Thread solverThread = new Thread(() -> {
            try {
                CircuitValue result = solver.solve(circuit);
                result.getValue();  // This should be interrupted
                fail("Expected an InterruptedException due to interruption.");
            } catch (InterruptedException e) {
                // Expected interruption, test passes
            }
        });

        solverThread.start();

        // Interrupt after 3 seconds
        try {
            Thread.sleep(3000);
            solver.stop();
            solverThread.join();
        } catch (InterruptedException e) {
            fail("Test was interrupted unexpectedly.");
        }
    }

    @Test
    public void complexThresholdCircuitEvaluation() {
        CircuitNode leaf1 = new SleepyLeafNode(false);
        CircuitNode leaf2 = new SleepyLeafNode(true);
        CircuitNode leaf3 = new SleepyLeafNode(true);
        CircuitNode ltNode1 = CircuitNode.mk(NodeType.LT, 2, leaf1, leaf2, leaf3);

        CircuitNode leaf4 = new SleepyLeafNode(false);
        CircuitNode leaf5 = new SleepyLeafNode(false);
        CircuitNode leaf6 = new SleepyLeafNode(true);
        CircuitNode orNode1 = CircuitNode.mk(NodeType.OR, leaf4, leaf5, leaf6);

        CircuitNode leaf7 = new SleepyLeafNode(true);
        CircuitNode leaf8 = new SleepyLeafNode(true);
        CircuitNode orNode2 = CircuitNode.mk(NodeType.OR, leaf7, leaf8);

        CircuitNode leaf9 = new SleepyLeafNode(false);
        CircuitNode leaf10 = new SleepyLeafNode(false);
        CircuitNode leaf11 = new SleepyLeafNode(false);
        CircuitNode leaf12 = new SleepyLeafNode(false);
        CircuitNode leaf13 = new SleepyLeafNode(true);
        CircuitNode ltNode2 = CircuitNode.mk(NodeType.LT, 2, leaf9, leaf10, leaf11, leaf12, leaf13);

        CircuitNode gtNode = CircuitNode.mk(NodeType.GT, 1, ltNode1, orNode1, orNode2, ltNode2);

        CircuitNode leaf14 = new SleepyLeafNode(false);
        CircuitNode leaf15 = new SleepyLeafNode(true);
        CircuitNode leaf16 = new SleepyLeafNode(true);
        CircuitNode leaf17 = new SleepyLeafNode(false);
        CircuitNode leaf18 = new SleepyLeafNode(false);
        CircuitNode ltNode3 = CircuitNode.mk(NodeType.LT, 1, leaf15, leaf16, leaf17, leaf18);

        CircuitNode leaf19 = new SleepyLeafNode(false);
        CircuitNode leaf20 = new SleepyLeafNode(false);
        CircuitNode leaf21 = new SleepyLeafNode(true);
        CircuitNode ltNode4 = CircuitNode.mk(NodeType.LT, 2, leaf19, leaf20, leaf21);

        CircuitNode leaf22 = new SleepyLeafNode(true);
        CircuitNode leaf23 = new SleepyLeafNode(true);
        CircuitNode leaf24 = new SleepyLeafNode(true);
        CircuitNode ifNode1 = CircuitNode.mk(NodeType.IF, leaf22, leaf23, leaf24);

        CircuitNode leaf25 = new SleepyLeafNode(true);
        CircuitNode leaf26 = new SleepyLeafNode(true);
        CircuitNode leaf27 = new SleepyLeafNode(false);
        CircuitNode ifNode2 = CircuitNode.mk(NodeType.IF, leaf25, leaf26, leaf27);

        CircuitNode ltNode5 = CircuitNode.mk(NodeType.LT, 5, leaf14, ltNode3, ltNode4, ifNode1, ifNode2);

        CircuitNode leaf28 = new SleepyLeafNode(false);

        CircuitNode finalNode = CircuitNode.mk(NodeType.LT, 3, gtNode, ltNode5, leaf28);

        Circuit circuit = new Circuit(finalNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertTrue(value.getValue(), "The complex threshold circuit should evaluate to true.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void randomFailedTest() {
        CircuitNode leaf1 = new SleepyLeafNode(true);
        CircuitNode leaf2 = new SleepyLeafNode(false);
        CircuitNode leaf3 = new SleepyLeafNode(false);
        CircuitNode leaf4 = new SleepyLeafNode(false);
        CircuitNode leaf5 = new SleepyLeafNode(true);
        CircuitNode ltNode1 = CircuitNode.mk(NodeType.LT, 1, leaf1, leaf2, leaf3, leaf4, leaf5);

        CircuitNode leaf6 = new SleepyLeafNode(true);
        CircuitNode notNode1 = CircuitNode.mk(NodeType.NOT, leaf6);

        CircuitNode orNode1 = CircuitNode.mk(NodeType.OR, ltNode1, notNode1);

        CircuitNode leaf7 = new SleepyLeafNode(true);
        CircuitNode leaf8 = new SleepyLeafNode(true);
        CircuitNode ltNode2 = CircuitNode.mk(NodeType.LT, 1, leaf7, leaf8);

        CircuitNode leaf9 = new SleepyLeafNode(true);
        CircuitNode leaf10 = new SleepyLeafNode(true);
        CircuitNode gtNode1 = CircuitNode.mk(NodeType.GT, 2, leaf9, leaf10);

        CircuitNode leaf11 = new SleepyLeafNode(true);
        CircuitNode leaf12 = new SleepyLeafNode(true);
        CircuitNode andNode1 = CircuitNode.mk(NodeType.AND, leaf11, leaf12);

        CircuitNode orNode2 = CircuitNode.mk(NodeType.OR, ltNode2, gtNode1, andNode1);

        CircuitNode leaf13 = new SleepyLeafNode(true);
        CircuitNode leaf14 = new SleepyLeafNode(false);
        CircuitNode leaf15 = new SleepyLeafNode(true);
        CircuitNode leaf16 = new SleepyLeafNode(true);
        CircuitNode ifNode1 = CircuitNode.mk(NodeType.IF, leaf14, leaf15, leaf16);

        CircuitNode leaf17 = new SleepyLeafNode(true);
        CircuitNode leaf18 = new SleepyLeafNode(true);
        CircuitNode leaf19 = new SleepyLeafNode(true);
        CircuitNode leaf20 = new SleepyLeafNode(true);
        CircuitNode andNode2 = CircuitNode.mk(NodeType.AND, leaf17, leaf18, leaf19, leaf20);

        CircuitNode orNode3 = CircuitNode.mk(NodeType.OR, leaf13, ifNode1, andNode2);

        CircuitNode finalNode = CircuitNode.mk(NodeType.LT, 2, orNode1, orNode2, orNode3);

        Circuit circuit = new Circuit(finalNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertFalse(value.getValue(), "The complex threshold circuit should evaluate to false.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
    }

    @Test
    public void puttingToQueueTests() {
        CircuitNode leaf1 = new SleepyLeafNode(false);
        CircuitNode leaf2 = new SleepyLeafNode(true);
        CircuitNode leaf3 = new SleepyLeafNode(true);
        CircuitNode orNode = CircuitNode.mk(NodeType.OR, leaf1, leaf2, leaf3);

        CircuitNode leaf4 = new SleepyLeafNode(false);
        CircuitNode notNode = CircuitNode.mk(NodeType.NOT, leaf4);

        CircuitNode finalNode = CircuitNode.mk(NodeType.LT, 2, orNode, notNode);

        Circuit circuit = new Circuit(finalNode);
        ParallelCircuitSolver solver = new ParallelCircuitSolver();

        CircuitValue value = solver.solve(circuit);
        try {
            assertFalse(value.getValue(), "The simple threshold circuit should evaluate to true.");
        } catch (InterruptedException e) {
            fail("The operation was interrupted.");
        }
        solver.stop();
    }
}
