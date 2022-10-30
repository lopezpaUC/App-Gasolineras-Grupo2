package es.unican.is.appgasolineras.activities.promotion;

import java.util.List;

import es.unican.is.appgasolineras.activities.main.CombustibleType;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

public interface IAnhadirPromocionContract {
    public interface Presenter {
        void init();

        boolean anhadePromocion(String n, String c, String m, String m2, double d, String e, List<String> gasolineras);
    }

    public interface View {

        IGasolinerasRepository getGasolineraRepository();
    }
}
