package pl.edu.mimuw.lines;

public abstract class Line {
    protected final int number;
    protected final int lineDuration;
    protected final Stop[] stops;
    protected final int[] timeBetweenStops;
    public Line(int number, int lineDuration, Stop[] stops, int[] timeBetweenStops) {
        this.number = number;
        this.lineDuration = lineDuration;
        this.stops = stops;
        this.timeBetweenStops = timeBetweenStops;
    }
    public int getNumber() {
        return number;
    }
    public int getNumberOfStops(){
        return stops.length;
    }
    public Stop[] getStops() {
        return stops;
    }
}
