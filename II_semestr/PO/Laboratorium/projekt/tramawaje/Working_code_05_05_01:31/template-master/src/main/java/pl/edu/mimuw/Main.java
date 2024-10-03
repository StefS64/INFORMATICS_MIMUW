package pl.edu.mimuw;
//presentation mode menu- view - appearance - presentation mode
//import java.util.Arrays;

import pl.edu.mimuw.eventqueue.EventQueueVector;
import pl.edu.mimuw.eventqueue.VehicleEvent;
import pl.edu.mimuw.eventqueue.VehicleEventType;
import pl.edu.mimuw.lines.Stop;

public class    Main {

    public static void main(String[] args) {
       Simulation sym = new Simulation();
        sym.simulate();
//      EventQueueVector events = new EventQueueVector();
//      Line line = new TramLine(1,2,null);
//      Tram tram = new Tram(12, line, null);
//       events.add(new VehicleEvent(3,null,tram, VehicleEventType.ARRIVAL));
//       events.printEvents();
//        events.add(new VehicleEvent(1,null,tram, VehicleEventType.ARRIVAL));
//        events.printEvents();
//        events.add(new VehicleEvent(5,null,tram, VehicleEventType.ARRIVAL));
//        events.printEvents();
//        events.add(new VehicleEvent(0,null,tram, VehicleEventType.ARRIVAL));
//        events.printEvents();
    }
}
