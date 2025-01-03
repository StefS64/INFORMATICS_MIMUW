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
    
    @Override
    public boolean getValue() throws InterruptedException {
        System.out.println("Getting value");
        if(!got_value) {
            got_value = true;
            System.out.println("help");
            value = results.take();
            System.out.println("Value: " + value);
        }
        if(value == -1) {
            throw new InterruptedException();
        } else {
            return value == 1;
        }
    }
}
