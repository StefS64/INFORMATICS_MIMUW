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
        LinkedBlockingQueue<Integer> results = new LinkedBlockingQueue<>();
        CircuitTask task = new CircuitTask(c.getRoot(), results);
        pool.execute(task);
        return new ParallelCircuitValue(task);
    }

    @Override
    public void stop() {
        acceptComputations = false;
        pool.shutdownNow();
    }

    private class CircuitTask extends RecursiveTask<Integer> {
        private final CircuitNode circuit;
        private final LinkedBlockingQueue<Integer> parentResults;
        private final List<RecursiveTask<Integer>> tasks = new ArrayList<>();
        CircuitTask(CircuitNode circuit, LinkedBlockingQueue<Integer> parentResults) {
            this.circuit = circuit;
            this.parentResults = parentResults;
        }

        @Override
        protected Integer compute() {
            try {
                return recursiveSolve(circuit);
            } catch (InterruptedException e) {
                throw new RuntimeException("Computation was interrupted", e);
            }
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            for (RecursiveTask<Integer> task : tasks) {
                task.cancel(mayInterruptIfRunning);
            }
            return super.cancel(mayInterruptIfRunning);
        }

        private Integer recursiveSolve(CircuitNode n) throws InterruptedException {
            System.out.println("Solving " + n.getType());
            if (n.getType() == NodeType.LEAF) {
                System.out.println("Returning " + ((LeafNode) n).getValue());
                int value = ((LeafNode) n).getValue() ? 1 : 0;
                parentResults.put(value);
                return value;
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
            return logical_value;            
        }

        private Integer solveIF(CircuitNode[] args) throws InterruptedException {
            if (args.length != 3) {
                throw new IllegalArgumentException("IF node must have exactly 3 arguments");
            }

            ForkJoinTask<Integer> conditionTask = new CircuitTask(args[0], parentResults);
            ForkJoinTask<Integer> trueBranchTask = new CircuitTask(args[1], parentResults);
            ForkJoinTask<Integer> falseBranchTask = new CircuitTask(args[2], parentResults);

            conditionTask.fork();
            trueBranchTask.fork();
            falseBranchTask.fork();

            Integer condition = conditionTask.join();

            if (condition == 1) {
                falseBranchTask.cancel(true);
                return trueBranchTask.join();
            } else if (condition == 0) {
                trueBranchTask.cancel(true);
                return falseBranchTask.join();
            } else {
                return -1;
            }
        } 

        private Integer solveAND(CircuitNode[] args) throws InterruptedException {
           return solveGT(args, args.length-1, 1);
        }

        private Integer solveOR(CircuitNode[] args) throws InterruptedException {
            return solveGT(args,0,1);
        }

        private Integer solveGT(CircuitNode[] args, int threshold, int calculateOnes) throws InterruptedException {
            LinkedBlockingQueue<Integer> results = new LinkedBlockingQueue<>();

            for (CircuitNode arg : args) {
                RecursiveTask<Integer> task = new CircuitTask(arg, results);
                tasks.add(task);
                task.fork();
            }

            int trueNodes = 0;
            for (int i = 0; i < args.length; i++) {
                System.out.println("Waiting for " + i);
                int result = results.take();
                System.out.println("Got result " + result);
                if (result == calculateOnes) {
                    trueNodes++;
                }

                if (trueNodes > threshold) {
                    for (RecursiveTask<Integer> task : tasks) {
                        task.cancel(true);
                    }
                    return 1; // Short-circuit if any argument is false
                }

                if(Thread.currentThread().isInterrupted() || result == -1) {
                    putInQueue(results, result);
                    this.cancel(true);
                    throw new InterruptedException();
                }
            }
            return 0; // All arguments are true
        }

        private Integer solveLT(CircuitNode[] args, int threshold) throws InterruptedException {
            return solveGT(args, threshold, 0);
        }

        private Integer solveNOT(CircuitNode[] args) throws InterruptedException {
            return recursiveSolve(args[0]);
        }

        private void putInQueue(LinkedBlockingQueue<Integer> results, Integer value) {
            try {
                results.put(value);
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to put value in queue", e);
            }
        }
    }
    public class InterruptedExceptionTask extends RecursiveTask<Integer> { // Dummy task. Used to throw InterruptedException
        @Override
        protected Integer compute() {
            return -1;
        }
    }
}
