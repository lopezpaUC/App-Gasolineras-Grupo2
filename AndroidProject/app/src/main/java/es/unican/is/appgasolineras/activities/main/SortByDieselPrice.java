package es.unican.is.appgasolineras.activities.main;

import java.util.Comparator;
import java.util.Locale;

import es.unican.is.appgasolineras.model.Gasolinera;

public class SortByDieselPrice implements Comparator<Gasolinera> {

    @Override
    public int compare(Gasolinera gasolinera, Gasolinera other) {
        double gasolineraDiesel = Double.parseDouble(gasolinera.getDieselA().replace(",", "."));
        double otherDiesel = Double.parseDouble(other.getDieselA().replace(",", "."));

        return Double.compare(gasolineraDiesel, otherDiesel);
    }
}
