package es.unican.is.appgasolineras.activities.main;

import java.util.Comparator;

import es.unican.is.appgasolineras.model.Gasolinera;

public class SortBySummaryPrice implements Comparator<Gasolinera> {

    @Override
    public int compare(Gasolinera gasolinera, Gasolinera other) {
        double gasolineraSummaryPrice = gasolinera.calculateSummaryPrice();
        double otherSummaryPrice = other.calculateSummaryPrice();

        return Double.compare(gasolineraSummaryPrice, otherSummaryPrice);
    }
}
