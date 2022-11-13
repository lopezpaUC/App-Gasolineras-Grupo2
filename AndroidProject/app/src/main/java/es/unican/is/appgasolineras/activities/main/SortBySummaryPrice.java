package es.unican.is.appgasolineras.activities.main;

import java.util.Comparator;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

public class SortBySummaryPrice implements Comparator<Gasolinera> {

    private IGasolinerasRepository gRep;
    private IPromocionesRepository pRep;

    public SortBySummaryPrice(IGasolinerasRepository gRep, IPromocionesRepository pRep) {
        this.gRep = gRep;
        this.pRep = pRep;
    }

    @Override
    public int compare(Gasolinera gasolinera, Gasolinera other) {
        /*double gasolineraSummaryPrice = gasolinera.calculateDiscountedSummaryPrice();
        double otherSummaryPrice = other.calculateDiscountedSummaryPrice();

        return Double.compare(gasolineraSummaryPrice, otherSummaryPrice);*/

        return 0;
    }
}
