package cp2024.solution;

import cp2024.circuit.CircuitSolver;
import cp2024.circuit.CircuitValue;
import cp2024.demo.BrokenCircuitValue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import cp2024.circuit.Circuit;

public class ParallelCircuitSolver implements CircuitSolver {
    private volatile boolean acceptComputations = true;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public CircuitValue solve(Circuit c) {
        if(!acceptComputations) {
            return new BrokenCircuitValue();
        }
        BlockingQueue<Integer> results = new LinkedBlockingQueue<>();
        CircuitAction task = new CircuitAction(c.getRoot(), results, executor);
        executor.submit(task);

        return new ParallelCircuitValue(results);
    }

    @Override
    public void stop() {
        acceptComputations = false;
        executor.shutdown();
    }
}
