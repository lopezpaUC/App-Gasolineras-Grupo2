package es.unican.is.appgasolineras.activities.main;

import java.util.List;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

/**
 * The Main Activity is composed of a Presenter and View, which must expose the methods
 * defined in the following interfaces.
 */
public interface IMainContract {

    /**
     * A Presenter for the Main Activity must implement this functionality
     * These methods (excluding init), are meant to be used by the View.
     */
    public interface Presenter {
        /**
         * Initialization method
         */
        void init();

        /**
         * This method should be used by the View to notify the Presenter that a Gas Station
         * has been clicked
         * @param index the index of the gas station (position in the list)
         */
        void onGasolineraClicked(int index);

        /**
         * This method should be used by the View to notifiy the Presenter that the
         * Info button has been clicked
         */
        void onInfoClicked();

        /**
         * This method should be used by the View to notify the Presenter that the
         * Refresh button has been clicked
         */
        void onRefreshClicked();
    }

    /**
     * A View for the Main Activity must implement this functionality
     * These methods (excluding init), are meant to be used by the Presenter.
     */
    public interface View {

        /**
         * Initialization method
         */
        void init();

        /**
         * Returns the Gasolineras Repository object.
         * This object can be used to access gas stations from
         * different sources (REST API, persisted DB, etc.)
         * This method is in the View because it generally requires access to the
         * Android Context, and this is available in the View
         * @return the Repository object to access gas stations
         */
        IGasolinerasRepository getGasolineraRepository();

        /**
         * The View is requested to show a list of gas stations
         * @param gasolineras the list of gas stations
         */
        void showGasolineras(List<Gasolinera> gasolineras);

        /**
         * The View is requested to show an alert informing that the gas stations were loaded
         * correctly
         * @param gasolinerasCount the number of gas stations that were loaded
         */
        void showLoadCorrect(int gasolinerasCount);

        /**
         * The View is requested to show an alert informing that there was an error while
         * loading the gas stations
         */
        void showLoadError();

        /**
         * The View is requested to open a Details view on the given gas station
         * @param gasolinera the gas station
         */
        void openGasolineraDetails(Gasolinera gasolinera);

        /**
         * The View is requested to open the Info view
         */
        void openInfoView();
    }

}
