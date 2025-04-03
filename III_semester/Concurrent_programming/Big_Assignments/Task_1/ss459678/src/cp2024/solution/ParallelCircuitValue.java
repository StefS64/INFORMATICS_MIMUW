package cp2024.solution;

import java.util.concurrent.BlockingQueue;
import cp2024.circuit.CircuitValue;

public class ParallelCircuitValue implements CircuitValue {
    private final BlockingQueue<Integer> results;
    private boolean got_value = false;
    private Integer value;

    public ParallelCircuitValue(BlockingQueue<Integer> results) {
        this.results = results;
    }
    // Waits for the result to be available and returns it. 
    @Override
    public boolean getValue() throws InterruptedException {
        if(!got_value) {
            got_value = true;
            value = results.take();
        }
        if(value == -1) {
            throw new InterruptedException();
        } else {
            return value == 1;
        }
    }
}
