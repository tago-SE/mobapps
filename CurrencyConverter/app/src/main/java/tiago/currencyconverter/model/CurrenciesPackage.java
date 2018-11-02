package tiago.currencyconverter.model;

import java.util.ArrayList;
import java.util.List;

public class CurrenciesPackage {
    public String timestamp;
    public List<Currency> currencies;


    public CurrenciesPackage(String timestamp, List<Currency> currencies) {
        this.currencies = currencies;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Currencies{" +
                "timestamp='" + timestamp + '\'' +
                ", currencies=" + currencies +
                '}';
    }
}
