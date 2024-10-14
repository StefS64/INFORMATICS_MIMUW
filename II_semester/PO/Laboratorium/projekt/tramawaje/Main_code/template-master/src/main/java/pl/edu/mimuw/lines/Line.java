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
    public Stop[] getStops() {
        return stops;
    }

    public String toString(){
        StringBuilder output = new StringBuilder("Num: " + number + "\n" + "Duration: " + lineDuration + "\n" + "number of stops: " + stops.length + "\n");
        for(int i = 0; i < stops.length; i++){
            output.append(" ").append(stops[i].toString()).append(" ").append(timeBetweenStops[i]);
        }
        return output.toString();
    }

    public void clearData(){
        for(int i = 0; i < stops.length; i++){
            stops[i].clearData();
        }
    }
}
