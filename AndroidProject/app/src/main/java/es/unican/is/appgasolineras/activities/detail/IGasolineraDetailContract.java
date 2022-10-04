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
         * Show name of the Gas Station's municipality.
         */
        void showMunicipality(String municipio);
    }
}
