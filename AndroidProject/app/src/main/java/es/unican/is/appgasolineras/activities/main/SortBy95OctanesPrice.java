package es.unican.is.appgasolineras.activities.main;

import java.util.Comparator;

import es.unican.is.appgasolineras.model.Gasolinera;

public class SortBy95OctanesPrice implements Comparator<Gasolinera> {

    @Override
    public int compare(Gasolinera gasolinera, Gasolinera other) {
        double gasolinera95 = Double.parseDouble(gasolinera.getNormal95().replace(",", "."));
        double other95 = Double.parseDouble(other.getNormal95().replace(",", "."));

        return Double.compare(gasolinera95, other95);
    }
}
