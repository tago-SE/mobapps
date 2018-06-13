package tiago.lab1.model;

import java.util.ArrayList;
import java.util.List;

public class Currencies {
    private static final Currencies ourInstance = new Currencies();

    private String timeLastPull;
    private String time;
    private List<Currency> currencies;


    public static Currencies getInstance() {
        return ourInstance;
    }

    private Currencies() {
        currencies = new ArrayList<>();
    }

    public void pull() {

    }

    public boolean isAutoPullEnabled() {
        return false;
    }

    public void enableAutoPull() {

    }

    public void disableAutoPull() {

    }
}
