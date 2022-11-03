package es.unican.is.appgasolineras.activities.promotion;

import android.view.View;

import java.util.List;
import java.util.Map;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

public interface IListaPromocionesContract {

    /**
     * Un presentador para la actividad Promocion debe implementar esta funcionalidad.
     */
    interface Presenter {
        /**
         * Metodo de inicializacion.
         */
        void init();

        /**
         * Retorna la lista de promociones mostrada.
         */
        List<Promocion> getShownPromociones();

        /**
         * Borra la promocion.
         * @param nombre de la promocion.
         */
        Promocion deletePromocion(String nombre);

        boolean tamanhoListaPromociones();
    }

    /**
     * Una vista para la actividad Promocion debe implementar esta funcionalidad.
     */
    interface View {

        /**
         * Metodo de inicializacion.
         */
        void init();

        /**
         * Returns the Promociones Repository object.
         * This object can be used to access promotions from
         * different sources (REST API, persisted DB, etc.)
         * This method is in the View because it generally requires access to the
         * Android Context, and this is available in the View
         * @return the Repository object to access promotions
         */
        IPromocionesRepository getPromocionRepository();

        /**
         * Returns the Gasolinera Repository object.
         * This object can be used to access promotions from
         * different sources (REST API, persisted DB, etc.)
         * This method is in the View because it generally requires access to the
         * Android Context, and this is available in the View
         * @return the Repository object to access gasolineras
         */
        IGasolinerasRepository getGasolineraRepository();

        /**
         * The View is requested to show a list of promotions
         * @param promociones the list of promotions
         * @param lista list of strings with the name of fuel stations
         * @param rotulos list of strings with the rotulos to show
         */
        void showPromociones(List<Promocion> promociones, List<String> lista, List<String> rotulos);

        /**
         * The View is requested to show an alert informing that the promotions were loaded
         * correctly from DB.
         * @param promocionesCount the number of promotions that were loaded
         */
        void showLoadCorrect(int promocionesCount);

        /**
         * The View is requested to show an alert informing that there was an error while
         * loading the promotions
         */
        void showLoadError();


        void deletePromocionSeleccionada(android.view.View v);
    }
}
