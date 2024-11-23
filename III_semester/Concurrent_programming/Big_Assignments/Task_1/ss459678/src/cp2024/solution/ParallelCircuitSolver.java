package cp2024.solution;

import cp2024.circuit.CircuitSolver;
import cp2024.circuit.CircuitValue;
import cp2024.circuit.LeafNode;
import cp2024.circuit.NodeType;
import cp2024.circuit.ThresholdNode;
import cp2024.demo.BrokenCircuitValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import cp2024.circuit.Circuit;
import cp2024.circuit.CircuitNode;

public class ParallelCircuitSolver implements CircuitSolver {
    private volatile boolean acceptComputations = true;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public CircuitValue solve(Circuit c) {
        if(!acceptComputations) {
            return new BrokenCircuitValue();
        }
        BlockingQueue<Integer> results = new LinkedBlockingQueue<>();
        CircuitAction task = new CircuitAction(c.getRoot(), results);
        executor.submit(task);

        return new ParallelCircuitValue(results);
    }

    @Override
    public void stop() {
        acceptComputations = false;
        executor.shutdown();
    }

    private class CircuitAction implements Runnable {
        BlockingQueue<Integer> parentResults;
        List<Future<?>> tasks = new ArrayList<>();
        CircuitNode circuit;

        CircuitAction(CircuitNode circuit, BlockingQueue<Integer> parentResults) {
            this.circuit = circuit;
            this.parentResults = parentResults;
        }

        @Override
        public void run() {
            try {
                recursiveSolve(circuit);
            } catch (InterruptedException e) {
                parentResults.add(-1);
                System.out.println("Interupted");
            }
        }

        private void recursiveSolve(CircuitNode n) throws InterruptedException {

            if (n.getType() == NodeType.LEAF) {
                int value = ((LeafNode) n).getValue() ? 1 : 0;
                parentResults.put(value);
                return;
            }

            CircuitNode[] args = n.getArgs();
            Integer logical_value = switch (n.getType()) {
                case IF -> solveIF(args);
                case AND -> solveAND(args);
                case OR -> solveOR(args);
                case GT -> solveGT(args, ((ThresholdNode) n).getThreshold(), 0);
                case LT -> solveLT(args, ((ThresholdNode) n).getThreshold());
                case NOT -> solveNOT(args);
                default -> throw new RuntimeException("Illegal type " + n.getType());
            };

            putInQueue(parentResults, logical_value);
        }

        private Integer solveIF(CircuitNode[] args) throws InterruptedException {
            if (args.length != 3) {
                throw new IllegalArgumentException("IF node must have exactly 3 arguments");
            }

            BlockingQueue<Integer> conditionQueue = new LinkedBlockingQueue<>();
            BlockingQueue<Integer> trueBranchQueue = new LinkedBlockingQueue<>();
            BlockingQueue<Integer> falseBranchQueue = new LinkedBlockingQueue<>();

            CircuitAction conditionAction = new CircuitAction(args[0], conditionQueue);
            CircuitAction trueBranchAction = new CircuitAction(args[1], trueBranchQueue);
            CircuitAction falseBranchAction = new CircuitAction(args[2], falseBranchQueue);

            Future<?> conditionFuture = executor.submit(conditionAction);
            Future<?> trueBranchFuture = executor.submit(trueBranchAction);
            Future<?> falseBranchFuture = executor.submit(falseBranchAction);

            tasks.add(conditionFuture);
            tasks.add(trueBranchFuture);
            tasks.add(falseBranchFuture);

            Integer condition = conditionQueue.take();

            if (condition == 1) {
                falseBranchFuture.cancel(true);
                return trueBranchQueue.take();
            } else {
                trueBranchFuture.cancel(true);
                return falseBranchQueue.take();
            }
        }

        private Integer solveAND(CircuitNode[] args) throws InterruptedException {
            return negate(solveGT(args, 0, 0));
        }

        private Integer solveOR(CircuitNode[] args) throws InterruptedException {
            return solveGT(args, 0, 1);
        }

        private Integer solveGT(CircuitNode[] args, int threshold, int calculateOnes) throws InterruptedException {
            BlockingQueue<Integer> results = new LinkedBlockingQueue<>();
            List<Future<?>> tasks = new ArrayList<>();

            for (CircuitNode arg : args) {
                CircuitAction task = new CircuitAction(arg, results);
                tasks.add(executor.submit(task));
            }

            int trueNodes = 0;
            for (int i = 0; i < args.length; i++) {
                //System.out.println("Taking result: " + i + " " + results.size());
                int result = results.take();
                if (result == calculateOnes) {
                    trueNodes++;
                }

                if (trueNodes > threshold) {
                    //System.out.println("Threshold reached");
                    cancelKids();
                    return 1;
                }
            }
            return 0;
        }

        private Integer solveLT(CircuitNode[] args, int threshold) throws InterruptedException {
            return solveGT(args, args.length - threshold, 0);
        }

        private Integer solveNOT(CircuitNode[] args) throws InterruptedException {
            BlockingQueue<Integer> results = new LinkedBlockingQueue<>();
            CircuitAction task = new CircuitAction(args[0], results);
            tasks.add(executor.submit(task));
            return negate(results.take());
        }

        private void cancelKids() {
            for (Future<?> task : tasks) {
                task.cancel(true);
            }
        }

        private Integer negate(int value) throws InterruptedException {
            if (value == 0) {
                return 1;
            } else if (value == 1) {
                return 0;
            } else {
                throw new InterruptedException();
            }
        }
        private void putInQueue(BlockingQueue<Integer> results, Integer value) {
            try {
                results.put(value);
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to put value in queue", e);
            }
        }
    }
}
