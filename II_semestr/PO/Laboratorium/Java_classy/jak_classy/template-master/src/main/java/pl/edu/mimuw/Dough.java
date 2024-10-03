package pl.edu.mimuw;
// gdy mamy z góry określone elementy
public enum Dough {
    PAN(100),
    THIN(20),
    CLASSIC(40);
    private final int fatness;
    Dough(int newFatness) {
        fatness = newFatness;
    }
    public int getFatness() {
        return fatness;
    }

}
