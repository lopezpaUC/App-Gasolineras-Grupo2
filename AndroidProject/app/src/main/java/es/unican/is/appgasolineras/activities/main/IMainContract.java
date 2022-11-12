package es.unican.is.appgasolineras.activities.main;

import java.util.List;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

/**
 * The Main Activity is composed of a Presenter and View, which must expose the methods
 * defined in the following interfaces.
 */
public interface IMainContract {

    /**
     * A Presenter for the Main Activity must implement this functionality
     * These methods (excluding init), are meant to be used by the View.
     */
    interface Presenter {
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

        /**
         * This method should be used by the View to notify the Presenter that the
         * Filter button has been clicked
         */
        void onFilterClicked();

        /**
         * This method should be used by the View to notifiy the Presenter that the
         * Promocion button has been clicked
         */
        void onAddPromotionClicked();

        /**
         * This method should be used by the View to notifiy the Presenter that the
         * Promocion button has been clicked
         */
        void onListPromotionsClicked();

        /**
         * This method should be used by the View to notify the Presenter that the
         * Order by Price button has been clicked.
         */
        void onOrderByPriceClicked();

        /**
         * Obtiene la lista de gasolineras que debe mostrar la vista.
         * @return lista de gasolineras que debe mostrar la vista.
         */
        List<Gasolinera> getShownGasolineras();

        /**
         * Filtra gasolineras a mostrar en funcion de los parametros especificados.
         * @param combustibleType Tipo de combustible.
         * @param brands Mara o listado de marcas.
         */
        void filter(CombustibleType combustibleType, List<String> brands);

        /**
         * Obtains a list of gas stations ordered by a determined price (diesel / 95-octanes /
         * summary price), either in an ascending or a descending order.
         * @param order the order (ascending / descending)
         * @param orderedValue the price to be ordered
         * @return
         */
        void orderByPrice(PriceOrderType order, PriceFilterType orderedValue);
    }

    /**
     * A View for the Main Activity must implement this functionality
     * These methods (excluding init), are meant to be used by the Presenter.
     */
    interface View {

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


        IPromocionesRepository getPromotionsRepository();

        /**
         * The View is requested to show a list of gas stations
         * @param gasolineras the list of gas stations
         */
        void showGasolineras(List<Gasolinera> gasolineras);

        /**
         * The View is requested to show an alert informing that the gas stations were loaded
         * correctly from online repository.
         * @param gasolinerasCount the number of gas stations that were loaded
         */
        void showLoadCorrectOnline(int gasolinerasCount);

        /**
         * The View is requested to show an alert informing that the gas stations were loaded
         * correctly from DB.
         * @param gasolinerasCount the number of gas stations that were loaded
         */
        void showLoadCorrectOffline(int gasolinerasCount);

        /**
         * The View is requested to show an alert informing that there was an error while
         * loading the gas stations
         */
        void showLoadError();

        /**
         * The View is requested to show an alert informing that there are not compatible gas
         * stations after filtering.
         */
        void showLoadEmpty();

        /**
         * The View is requested to open a Details view on the given gas station
         * @param gasolinera the gas station
         */
        void openGasolineraDetails(Gasolinera gasolinera);

        /**
         * The View is requested to open the Info view
         */
        void openInfoView();

        /**
         * Se requiere abrir la vista Anhadir Promocion
         */
        void openAnhadirPromocionView();

        /**
         * Se requiere abrir la vista Lista Promociones
         */
        void openListaPromocionesView();

        /**
         * The View is requested to open the Filter dialog
         */
        void openFilterDialog();

        /**
         * The View is requested to oen the Order by Price dialog
         */
        void openOrderByPrice();
    }

}
