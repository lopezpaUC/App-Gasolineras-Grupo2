package es.unican.is.appgasolineras.activities.detail;

import java.util.ArrayList;

import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

public class AnhadirPromocionPresenter implements IAnhadirPromocionContract.Presenter{

    // Vista
    private final IAnhadirPromocionContract.View view;

    private IGasolinerasRepository repository;

    /**
     * Constructor
     * @param view vista
     */
    public AnhadirPromocionPresenter(IAnhadirPromocionContract.View view) {
        this.view = view;

    }

    public ArrayList<String> getListadoNombresGasolineras() {
        repository =
    }










}
