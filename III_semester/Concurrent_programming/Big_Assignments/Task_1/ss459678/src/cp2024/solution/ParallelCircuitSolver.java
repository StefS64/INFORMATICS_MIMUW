package cp2024.solution;

import cp2024.circuit.*;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.List;
import java.util.ArrayList;

public class ParallelCircuitSolver implements CircuitSolver {
    private volatile boolean acceptComputations = true;
    private final ForkJoinPool pool = new ForkJoinPool();

    @Override
    public CircuitValue solve(Circuit c) {
        if (!acceptComputations) {
            return new ParallelCircuitValue(new InterruptedExceptionTask());
        }
        LinkedBlockingQueue<Boolean> results = new LinkedBlockingQueue<>();
        CircuitTask task = new CircuitTask(c.getRoot(), results);
        pool.execute(task);
        return new ParallelCircuitValue(task);
    }

    @Override
    public void stop() {
        acceptComputations = false;
        pool.shutdownNow();
    }

    private class CircuitTask extends RecursiveTask<Boolean> {
        private final CircuitNode circuit;
        private final LinkedBlockingQueue<Boolean> parentResults;
        CircuitTask(CircuitNode circuit, LinkedBlockingQueue<Boolean> parentResults) {
            this.circuit = circuit;
            this.parentResults = parentResults;
        }

        @Override
        protected Boolean compute() {
            try {
                return recursiveSolve(circuit);
            } catch (InterruptedException e) {
                throw new RuntimeException("Computation was interrupted", e);
            }
        }

        private boolean recursiveSolve(CircuitNode n) throws InterruptedException {
            System.out.println("Solving " + n.getType());
            if (n.getType() == NodeType.LEAF) {
                System.out.println("Returning " + ((LeafNode) n).getValue());
                parentResults.put(((LeafNode) n).getValue());
                return ((LeafNode) n).getValue();
            }

            CircuitNode[] args = n.getArgs();

            boolean logical_value = switch (n.getType()) {
                case IF -> solveIF(args);
                case AND -> solveAND(args);
                case OR -> solveOR(args);
                case GT -> solveGT(args, ((ThresholdNode) n).getThreshold(), false);
                case LT -> solveLT(args, ((ThresholdNode) n).getThreshold());
                case NOT -> solveNOT(args);
                default -> throw new RuntimeException("Illegal type " + n.getType());
            };

            putInQueue(parentResults, logical_value);
            return logical_value;            
        }

        private boolean solveIF(CircuitNode[] args) throws InterruptedException {
            if (args.length != 3) {
                throw new IllegalArgumentException("IF node must have exactly 3 arguments");
            }

            ForkJoinTask<Boolean> conditionTask = ForkJoinTask.adapt(() -> recursiveSolve(args[0]));
            ForkJoinTask<Boolean> trueBranchTask = ForkJoinTask.adapt(() -> recursiveSolve(args[1]));
            ForkJoinTask<Boolean> falseBranchTask = ForkJoinTask.adapt(() -> recursiveSolve(args[2]));

            conditionTask.fork();
            trueBranchTask.fork();
            falseBranchTask.fork();

            boolean condition = conditionTask.join();

            if (condition) {
                falseBranchTask.cancel(true);
                return trueBranchTask.join();
            } else {
                trueBranchTask.cancel(true);
                return falseBranchTask.join();
            }
        } 

        private boolean solveAND(CircuitNode[] args) throws InterruptedException {
           return solveGT(args, args.length-1, true);
        }

        private boolean solveOR(CircuitNode[] args) throws InterruptedException {
            return solveGT(args,0,true);
        }

        private boolean solveGT(CircuitNode[] args, int threshold, boolean calculateOnes) throws InterruptedException {
            LinkedBlockingQueue<Boolean> results = new LinkedBlockingQueue<>();
            List<RecursiveTask<Boolean>> tasks = new ArrayList<>();

            for (CircuitNode arg : args) {
                RecursiveTask<Boolean> task = new CircuitTask(arg, results);
                tasks.add(task);
                task.fork();
            }

            int trueNodes = 0;
            for (int i = 0; i < args.length; i++) {
                System.out.println("Waiting for " + i);
                boolean result = results.take();
                System.out.println("Got result " + result);
                if (result == calculateOnes) {
                    trueNodes++;
                }

                if (trueNodes > threshold) {
                    for (RecursiveTask<Boolean> task : tasks) {
                        task.cancel(true);
                    }
                    return true; // Short-circuit if any argument is false
                }
            }
            return false; // All arguments are true
        }

        private boolean solveLT(CircuitNode[] args, int threshold) throws InterruptedException {
            return solveGT(args, threshold, false);
        }

        private boolean solveNOT(CircuitNode[] args) throws InterruptedException {
            return !recursiveSolve(args[0]);
        }

        private void putInQueue(LinkedBlockingQueue<Boolean> results, boolean value) {
            try {
                results.put(value);
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to put value in queue", e);
            }
        }
    }
    public class InterruptedExceptionTask extends RecursiveTask<Boolean> {
        @Override
        protected Boolean compute() {
            return false;
        }
    }
}
