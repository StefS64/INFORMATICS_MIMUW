package cp2024.solution;

import cp2024.circuit.CircuitValue;
import cp2024.solution.ParallelCircuitSolver.InterruptedExceptionTask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

public class ParallelCircuitValue implements CircuitValue {
    private final RecursiveTask<Integer> task;

    public ParallelCircuitValue(RecursiveTask<Integer> task) {
        this.task = task;
    }



    @Override
    public boolean getValue() throws InterruptedException {
        try {
            if (task instanceof InterruptedExceptionTask) {
                throw new InterruptedException();
            }
            int value = task.get();
            if (value == 0) {
                return false;
            } else if (value == 1) {
                return true;
            } else {
                throw new InterruptedException();
            }
        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to get circuit value", e);
        }
    }
}
