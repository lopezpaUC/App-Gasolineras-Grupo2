package es.unican.is.appgasolineras.activities.promotion;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

public class AnhadirPromocionPresenter implements IAnhadirPromocionContract.Presenter {

    // Vista
    private final IAnhadirPromocionContract.View view;

    // Repositorio de gasolineras
    private IGasolinerasRepository repository;

    // Lista con todas las gasolineras
    List<Gasolinera> data;

    // Lista con el nombre y la dirección de todas las gasolineras
    ArrayList<String> dataNombreDireccion;

    /**
     * Constructor
     * @param view vista
     */
    public AnhadirPromocionPresenter(IAnhadirPromocionContract.View view) {
        this.view = view;
        repository = repository;

    }

    @Override
    public void init() {
        dataNombreDireccion = new ArrayList<String>();
        repository = view.getGasolineraRepository();

        rellenaGasolineras();
    }

    private void rellenaGasolineras() {
        int i= 0;

        /*
        for (Gasolinera gasolinera : data = dao.getAll()) {
            //dataNombreDireccion.set(i, gasolinera.getRotulo() + " - " + gasolinera.getDireccion());
            i++;
        }

         */
    }
}


















