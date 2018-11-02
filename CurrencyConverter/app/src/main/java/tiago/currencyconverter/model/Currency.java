package tiago.currencyconverter.model;

public class Currency {

    public String label;
    public float rate;

    public Currency(String label, float rate) {
        this.label = label;
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "label='" + label + '\'' +
                ", rate=" + rate +
                '}';
    }
}
