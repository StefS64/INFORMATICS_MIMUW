package cp2024.solution;

import cp2024.circuit.CircuitNode;
import cp2024.circuit.LeafNode;
import cp2024.circuit.NodeType;
import cp2024.circuit.ThresholdNode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/*  This class implements tasks for cachedThreadPool. 
    It is used to solve the circuit in parallel. 
    It is used by ParallelCircuitSolver class.
    It is ran in a vertex of the circuit and then it will recursively solve the circuit.
*/
public class CircuitAction implements Runnable {
    // Queue that allows for communication between parent and child nodes -1 signifies an error.
    private final BlockingQueue<Integer> parentResults;
    // List of tasks that are running in parallel beneath this node.
    private final List<Future<?>> tasks = new ArrayList<>();
    private final ExecutorService executor;
    private final CircuitNode circuit;

    public CircuitAction(CircuitNode circuit, BlockingQueue<Integer> parentResults, ExecutorService executor) {
        this.circuit = circuit;
        this.parentResults = parentResults;
        this.executor = executor;
    }

    @Override
    public void run() {
        try {
            parentResults.add(recursiveSolve(circuit));
            cancelKids();
        } catch (InterruptedException e) {
            // Something went wrong, notify the parent.
            System.out.println("Error in CircuitAction" +circuit.getType());
            parentResults.add(-1);
            cancelKids();
        }
    }
    /*
     * This method checks the node in which we are in and acts accordingly.
     */
    private Integer recursiveSolve(CircuitNode n) throws InterruptedException {

        if (n.getType() == NodeType.LEAF) {
            return ((LeafNode) n).getValue() ? 1 : 0;
        }

        CircuitNode[] args = n.getArgs();

        Integer logical_value = switch (n.getType()) {
            case IF -> solveIF(args);
            case AND -> solveAND(args);
            case OR -> solveOR(args);
            case GT -> solveGT(args, ((ThresholdNode) n).getThreshold(), 1);
            case LT -> solveLT(args, ((ThresholdNode) n).getThreshold());
            case NOT -> solveNOT(args);
            default -> throw new RuntimeException("Illegal type " + n.getType());
        };
        
        return logical_value;
    }

    private Integer solveIF(CircuitNode[] args) throws InterruptedException {
        if (args.length != 3) {
            throw new IllegalArgumentException("IF node must have exactly 3 arguments");
        }

        BlockingQueue<Integer> conditionQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Integer> trueBranchQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Integer> falseBranchQueue = new LinkedBlockingQueue<>();

        CircuitAction conditionAction = new CircuitAction(args[0], conditionQueue, executor);
        CircuitAction trueBranchAction = new CircuitAction(args[1], trueBranchQueue, executor);
        CircuitAction falseBranchAction = new CircuitAction(args[2], falseBranchQueue, executor);

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
        } else if (condition == 0) {
            trueBranchFuture.cancel(true);

            return falseBranchQueue.take();
        } else {
            throw new InterruptedException();
        }
    }

    private Integer solveNOT(CircuitNode[] args) throws InterruptedException {
        BlockingQueue<Integer> results = new LinkedBlockingQueue<>();
        CircuitAction task = new CircuitAction(args[0], results, executor);

        tasks.add(executor.submit(task));

        return negate(results.take());
    }
    
    private Integer solveGT(CircuitNode[] args, int threshold, int calculateOnes) throws InterruptedException {
        BlockingQueue<Integer> results = new LinkedBlockingQueue<>();

        for (CircuitNode arg : args) {
            CircuitAction task = new CircuitAction(arg, results, executor);
            tasks.add(executor.submit(task));
        }

        int trueNodes = 0;
        for (int i = 0; i < args.length; i++) {
            int result = results.take();
            
            if (result == -1) {
                cancelKids();
                throw new InterruptedException();
            }

            if (result == calculateOnes) {
                trueNodes++;
            }

            if (trueNodes > threshold) {
                cancelKids();
                return 1;
            }
        }
        return 0;
    }
    
    private Integer solveAND(CircuitNode[] args) throws InterruptedException {
        return negate(solveGT(args, 0, 0));
    }

    private Integer solveOR(CircuitNode[] args) throws InterruptedException {
        return solveGT(args, 0, 1);
    }

    private Integer solveLT(CircuitNode[] args, int threshold) throws InterruptedException {
        return solveGT(args, args.length - threshold, 0);
    }

    // Cancels all tasks that are running in parallel beneath this node.
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
}