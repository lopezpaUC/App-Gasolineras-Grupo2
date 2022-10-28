package es.unican.is.appgasolineras.activities.promotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.unican.is.appgasolineras.activities.main.CombustibleType;
import es.unican.is.appgasolineras.model.Gasolinera;

import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

public class AnhadirPromocionPresenter implements IAnhadirPromocionContract.Presenter {
    // Vista
    private final IAnhadirPromocionContract.View view;



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

    }

    @Override
    public void init() {
        dataNombreDireccion = new ArrayList<String>();
        //repository = view.getGasolineraRepository();
        rellenaGasolineras();
    }

    /**
     * Obtiene la lista con las gasolineras
     */
    private void rellenaGasolineras() {
        /* TODO */
    }


    /**
     * Anhade una promocion.
     * @return true Si se ha podido anhadir. False en caso contrario.
     */

    @Override
    public boolean anhadePromocion(String n, String c, String m, String m2, double d, String e, List<String> gasolineras) {

        /*Promocion p;

        // Convertir combustible
        CombustibleType combustibleType = null;
        c = c.toUpperCase(Locale.ROOT);
        for(int i = 0; i < NUM_COMBUSTIBLES; i++ ) {
            combustibleType.getCombTypeFromInt(i);
            if(combustibleType.toString() == c) {
                break;
            }
        }

        // Convertir porMarca
        boolean porMarca;
        if(m == "Por marca") {
            porMarca = true;
        } else {
            porMarca = false;
        }

        // Convertir esPorcentaje
        boolean esPorcentaje;
        if(m == "%") {
            esPorcentaje = true;
        } else {
            esPorcentaje = false;
        }

        *//* TODO convertir gasolineras y quitar el null *//*
        List<Gasolinera> listaGasolineras = null;
        // Crear la promocion
        p = new Promocion(n, combustibleType, porMarca, m2, d, esPorcentaje, listaGasolineras);
        aplicarPromocion(p);

        *//* TODO llamar a la dao para anhadir promocion a la base de datos *//*
        *//* TODO actualizar base de datos con los nuevos cambios en las gasolineras */

        return true;

    }

    /**
     * Aplica la promocion en todas las gasolineras afectadas
     * @param promocion
     */
    /* CELSO
    private void aplicarPromocion(Promocion promocion) {
        for(Gasolinera gasolinera: promocion.getGasolineras()) {
            gasolinera.aplicarPromocion(promocion);
        }
        }
    }*/
}





















