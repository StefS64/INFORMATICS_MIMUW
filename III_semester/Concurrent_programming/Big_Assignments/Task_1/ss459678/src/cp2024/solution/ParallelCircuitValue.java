package cp2024.solution;

import cp2024.circuit.CircuitValue;
import cp2024.solution.ParallelCircuitSolver.InterruptedExceptionTask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

public class ParallelCircuitValue implements CircuitValue {
    private final RecursiveTask<Boolean> task;

    public ParallelCircuitValue(RecursiveTask<Boolean> task) {
        this.task = task;
    }

    @Override
    public boolean getValue() throws InterruptedException {
        try {
            if (task instanceof InterruptedExceptionTask) {
                throw new InterruptedException();
            }
            return task.get();
        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to get circuit value", e);
        }
    }
}
