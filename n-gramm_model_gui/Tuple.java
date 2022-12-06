import java.util.HashMap;

public class Tuple {//extends JsonSerializer<Tuple> {

    public String word;
    public double probability;

    public Tuple(String word, double probability) {
        this.word = word;
        this.probability = probability;
    }

    @Override
    public String toString() {
        return String.format("(%s:%s)", word, probability);
    }
}
