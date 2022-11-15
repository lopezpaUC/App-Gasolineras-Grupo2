package es.unican.is.appgasolineras.activities.detail;

import android.content.Context;

import java.util.Map;

import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

/**
 * La actividad Detalle esta compuesta por un presentador y una vista, que deben presentar
 * los metodos definidos en las siguientes interfaces.
 */

public interface IGasolineraDetailContract {

    /**
     * Un presentador para la actividad Detalle debe implementar esta funcionalidad.
     */
    interface Presenter {
        /**
         * Metodo de inicializacion.
         */
        void init();

        /**
         * Metodo de respuesta ante aceptar la alerta que indica que no se han podido cargar
         * los detalles de una gasolinera.
         * Debe retornar a la vista principal.
         */
        void onAcceptClicked();

        /**
         * Retorna el precio sumario de la gasolinera.
         */
        String getPrecioSumario();

        /**
         * Returns the discounted summary price as text
         * @return the discounted summary price as text
         */
        String getDiscountedSummaryPriceStr();

        /**
         * Returns the discounted diesel price as text
         * @return the discounted diesel price as text
         */
        String getDiscountedDieselPriceStr();

        /**
         * Returns the discounted 95-octanes price as text
         * @return the discounted 95-octanes price as text
         */
        String getDiscounted95OctanesPriceStr();
    }

    /**
     * Una vista para la actividad Detalle debe implementar esta funcionalidad.
     */
    interface View {
        /**
         * Metodo de inicializacion.
         */
        void init();

        /**
         * Muestra informacion detallada de una gasolinera especifica.
         */
        void showInfo(Map<String, String> info);

        /**
         * Muestra una alerta informando de que ha habido un error intentando cargar la informacion
         * especifica de una gasolinera.
         */
        void showLoadError();

        /**
         * Abre la vista principal.
         */
        void openMainView();

        /**
         * Returns the context of the activity
         * @return the context of the activity
         */
        Context getContext();

        IPromocionesRepository getPromocionesRepository();

        IGasolinerasRepository getGasolinerasRepository();
    }
}
