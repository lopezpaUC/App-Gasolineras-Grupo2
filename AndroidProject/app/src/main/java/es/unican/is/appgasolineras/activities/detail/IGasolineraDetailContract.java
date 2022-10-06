package es.unican.is.appgasolineras.activities.detail;

import android.content.Context;

import es.unican.is.appgasolineras.model.Gasolinera;

/**
 * The Detail Activity is composed of a Presenter and View, which must expose the methods
 * defined in the following interfaces.
 */
public interface IGasolineraDetailContract {
    /**
     * A Presenter for the Detail Activity must implement this functionality
     * These methods (excluding init), are meant to be used by the View.
     */
    interface Presenter {
        /**
         * Initialization method
         */
        void init();
    }


    /**
    * A View for the Detail Activity must implement this functionality
    * These methods (excluding init), are meant to be used by the Presenter.
    */
    interface View {
        /**
         * Initialization method
         */
        void init();

        /**
         * Return Gas Station from the intent that triggered the activity
         */
        Gasolinera getSelectedGasolinera();

        /**
         * Return context from the activity.
         */
        Context getContext();

        /**
         * Show logo image of the Gas Station.
         */
        void showLogo(int imageID);

        /**
         * Show name of the Gas Station.
         */
        void showName(String rotulo);

        /**
         * Show direction of the Gas Station.
         */
        void showDirection(String direccion);

        /**
         * Show name of the Gas Station's municipality.
         */
        void showMunicipality(String municipio);

        /**
         * Show CP of the Gas Station's municipality.
         */
        void showCP(String cp);

        /**
         * Show Gasolina95's price.
         */
        void showPrice95(String precio);

        /**
         * Show DieselA's price.
         */
        void showPriceDieselA(String precio);

        /**
         * Show schedule of the Gas Station.
         */
        void showSchedule(String horario);

        /**
         * Show summary of the Gas Station.
         */
        void showSummary(String sumario);
    }
}
