package es.unican.is.appgasolineras.activities.detail;

import android.content.Context;

import java.util.List;
import java.util.Map;

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

        void onAcceptClicked();
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
         * Show info of tha Gas Station.
         */
        void showInfo(Map<String, String> info);

        /**
         * Show an alert informing that there was an error while loading the gas station's info.
         */
        void showLoadError();

        void openMenuView();
    }
}
