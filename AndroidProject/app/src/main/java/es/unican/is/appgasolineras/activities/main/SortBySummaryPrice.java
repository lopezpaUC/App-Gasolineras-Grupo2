package es.unican.is.appgasolineras.activities.main;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import es.unican.is.appgasolineras.common.utils.PriceUtilities;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

public class SortBySummaryPrice implements Comparator<Gasolinera> {

    private IGasolinerasRepository gRep;
    private IPromocionesRepository pRep;
    private PriceUtilities utilities;

    public SortBySummaryPrice(IGasolinerasRepository gRep, IPromocionesRepository pRep) {
        this.gRep = gRep;
        this.pRep = pRep;

        utilities = new PriceUtilities();
    }

    @Override
    public int compare(Gasolinera gasolinera, Gasolinera other) {
        // Obtiene datos Gasolinera 1
        double gas1 = utilities.precioToDouble(gasolinera.getNormal95(), NumberFormat.getInstance(Locale.FRANCE));
        double gasDescontado1;
        double diesel1 = utilities.precioToDouble(gasolinera.getDieselA(), NumberFormat.getInstance(Locale.FRANCE));
        double dieselDescontado1;
        List<Promocion> promociones1 = pRep.getPromocionesRelacionadasConGasolinera(gasolinera.getId());

        if (promociones1.isEmpty()) {
            gasDescontado1 = gas1;
            dieselDescontado1 = diesel1;
        } else {
            Promocion best1 = gRep.bestPromotion(gas1, promociones1, "Gasolina");
            gasDescontado1 = utilities.calculateDiscountedPrice(gas1, best1);
            best1 = gRep.bestPromotion(gas1, promociones1, "Diésel");
            dieselDescontado1 = utilities.calculateDiscountedPrice(diesel1, best1);
        }

        // Obtiene datos Gasolinera 2
        double gas2 = utilities.precioToDouble(other.getNormal95(), NumberFormat.getInstance(Locale.FRANCE));
        double gasDescontado2;
        double diesel2 = utilities.precioToDouble(other.getDieselA(), NumberFormat.getInstance(Locale.FRANCE));
        double dieselDescontado2;
        List<Promocion> promociones2 = pRep.getPromocionesRelacionadasConGasolinera(other.getId());

        if (promociones2.isEmpty()) {
            gasDescontado2 = gas2;
            dieselDescontado2 = diesel2;
        } else {
            Promocion best2 = gRep.bestPromotion(gas2, promociones2, "Gasolina");
            gasDescontado2 = utilities.calculateDiscountedPrice(gas2, best2);
            best2 = gRep.bestPromotion(gas2, promociones2, "Diésel");
            dieselDescontado2 = utilities.calculateDiscountedPrice(diesel2, best2);
        }

        double sum1 = utilities.calculateSummary(dieselDescontado1, gasDescontado1);
        double sum2 = utilities.calculateSummary(dieselDescontado2, gasDescontado2);

        return Double.compare(sum1, sum2);
    }
}
