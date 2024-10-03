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

    public String toString(){
        String output = "Num: " + number + "\n" + "Duration: " + lineDuration + "\n" +"number of stops: " + stops.length + "\n";
        for(int i = 0; i < stops.length; i++){
            output += " " + stops[i].getName() + " " + timeBetweenStops[i];
        }
        return output;
    }
}
