package es.unican.is.appgasolineras.activities.main;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

public class SortByDieselPrice implements Comparator<Gasolinera> {

    private IGasolinerasRepository gRep;
    private IPromocionesRepository pRep;

    public SortByDieselPrice(IGasolinerasRepository gRep, IPromocionesRepository pRep) {
        this.gRep = gRep;
        this.pRep = pRep;
    }

    @Override
    public int compare(Gasolinera gasolinera, Gasolinera other) {
        // Obtiene datos Gasolinera 1
        double diesel1 = gRep.precioToDouble(gasolinera.getDieselA(), NumberFormat.getInstance(Locale.FRANCE));
        double dieselDescontado1;
        List<Promocion> promociones1 = pRep.getPromocionesRelacionadasConGasolinera(gasolinera.getId());

        if (promociones1.isEmpty()) {
            dieselDescontado1 = diesel1;
        } else {
            Promocion best1 = gRep.bestPromotion(diesel1, promociones1, "Diésel");
            dieselDescontado1 = gRep.calculateDiscountedPrice(diesel1, best1);
         }

        // Obtiene datos Gasolinera 2
        double diesel2 = gRep.precioToDouble(other.getDieselA(), NumberFormat.getInstance(Locale.FRANCE));
        double dieselDescontado2;
        List<Promocion> promociones2 = pRep.getPromocionesRelacionadasConGasolinera(other.getId());

        if (promociones2.isEmpty()) {
            dieselDescontado2 = diesel2;
        } else {
            Promocion best2 = gRep.bestPromotion(diesel2, promociones2, "Diésel");
            dieselDescontado2 = gRep.calculateDiscountedPrice(diesel2, best2);
        }

        return Double.compare(dieselDescontado1, dieselDescontado2);
    }
}
