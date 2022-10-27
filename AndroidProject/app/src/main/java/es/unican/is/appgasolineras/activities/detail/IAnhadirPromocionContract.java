package es.unican.is.appgasolineras.activities.detail;

import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

public interface IAnhadirPromocionContract {
    public interface Presenter {

        void init();
    }

    public interface View {

        IGasolinerasRepository getGasolineraRepository();
    }
}
