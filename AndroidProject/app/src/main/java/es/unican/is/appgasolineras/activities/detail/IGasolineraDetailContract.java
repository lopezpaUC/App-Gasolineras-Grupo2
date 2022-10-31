package es.unican.is.appgasolineras.activities.detail;

import android.content.Context;

import java.util.Map;

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

        String getDiscountedSummaryPriceStr();

        String getDiscountedDieselPriceStr();

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

        Context getContext();
    }
}
