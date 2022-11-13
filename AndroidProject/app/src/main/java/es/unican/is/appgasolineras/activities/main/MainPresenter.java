package es.unican.is.appgasolineras.activities.main;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

public class MainPresenter implements IMainContract.Presenter {

    // Constante para indicar si las gasolineras se cargan de forma online u offline
    private static final int LOAD_ONLINE = 0;

    // Vista principal
    private final IMainContract.View view;

    // Repositorio de gasolineras
    private IGasolinerasRepository repositoryGasolineras;
    private IPromocionesRepository repositoryPromotions;

    // Lista de gasolineras a mostrar
    private List<Gasolinera> shownGasolineras;

    // Metodo utilizado para la carga de gasolineras
    private int loadMethod;

    private NumberFormat format;

    /**
     * Constructor del presenter de la vista principal.
     *
     * @param view Vista principal
     */
    public MainPresenter(IMainContract.View view) {
        this.view = view;
        loadMethod = LOAD_ONLINE; // Por defecto, se cargan de repositorio online
    }

    @Override
    public void init() {
        if (repositoryGasolineras == null) { // Si no consta repositorio asignado
            repositoryGasolineras = view.getGasolineraRepository();
        }

        if (repositoryPromotions == null) { // Si no consta repositorio asignado
            repositoryPromotions = view.getPromotionsRepository();
        }


        if (repositoryGasolineras != null) { // Si ya consta un repositorio
            doSyncInit();
        }

        format = NumberFormat.getInstance(Locale.FRANCE);
    }

    /** NO USADO POR EL MOMENTO
     * Muestra contenido antes de haber intentado recibir el actualizado de internet.
     */
    /**private void doAsyncInit() {
     repositoryGasolineras.requestGasolineras(new Callback<List<Gasolinera>>() {
    @Override public void onSuccess(List<Gasolinera> data) {
    loadMethod = repositoryGasolineras.getLoadingMethod();
    view.showGasolineras(data);
    shownGasolineras = data;
    if (loadMethod == LOAD_ONLINE) {
    view.showLoadCorrectOnline(data.size());
    } else {
    view.showLoadCorrectOffline(data.size());
    }
    }

    @Override public void onFailure() {
    shownGasolineras = null;
    view.showLoadError();
    }
    });
     }*/

    /**
     * Muestra contenido despues de intentar haber recibido el actualizado de internet.
     */
    private void doSyncInit() {
        List<Gasolinera> data = repositoryGasolineras.getGasolineras();

        if (!data.isEmpty()) { // Si se obtiene una lista con gasolineras
            // Obtiene si se ha cargado de BD o repositorio online.
            loadMethod = repositoryGasolineras.getLoadingMethod();

            // Muestra gasolineras
            view.showGasolineras(data);
            shownGasolineras = data;

            if (loadMethod == LOAD_ONLINE) { // Si se obtienen de repositorio online
                // Muestra que estan actualizadas y el numero
                view.showLoadCorrectOnline(data.size());
            } else { // Si se obtienen de BD
                // Muestra la fecha de ultima actualizacion y el numero
                view.showLoadCorrectOffline(data.size());
            }
        } else { // Si no se obtienen gasolineras
            shownGasolineras = null;
            view.showLoadError();
        }

    }

    @Override
    public List<Gasolinera> getShownGasolineras() {
        return this.shownGasolineras;
    }

    @Override
    public void onGasolineraClicked(int index) {
        if (shownGasolineras != null && index < shownGasolineras.size()) {
            Gasolinera gasolinera = shownGasolineras.get(index);
            view.openGasolineraDetails(gasolinera);
        }
    }

    @Override
    public void onInfoClicked() {
        view.openInfoView();
    }

    @Override
    public void onFilterClicked() {
        view.openFilterDialog();
    }

    @Override
    public void onAddPromotionClicked() {
        view.openAnhadirPromocionView();
    }

    @Override
    public void onListPromotionsClicked() {
        view.openListaPromocionesView();
    }

    @Override
    public void onRefreshClicked() {
        init();
    }

    @Override
    public void onOrderByPriceClicked() {
        view.openOrderByPrice();
    }

    @Override
    public void filter(CombustibleType combustibleType, List<String> brands) {
        shownGasolineras = repositoryGasolineras.getGasolineras(); // Lista Completa
        filterByCombustible(combustibleType);
        filterByBrand(brands);

        if (!shownGasolineras.isEmpty()) { // Si hay gasolineras a mostrar despues de filtrado
            view.showGasolineras(shownGasolineras);

            // Muestra la informacion de la obtencion de las gasolineras
            if (loadMethod == LOAD_ONLINE) {
                view.showLoadCorrectOnline(shownGasolineras.size());
            } else {
                view.showLoadCorrectOffline(shownGasolineras.size());
            }

        } else { // Si no hay gasolineras a mostrar despues de filtrado
            view.showLoadEmpty();
            shownGasolineras = null;
        }
    }

    /**
     * Filtra por tipo de combustible.
     *
     * @param combustibleType Tipo de combustible a utilizar para filtrar
     */
    public void filterByCombustible(CombustibleType combustibleType) {
        List<Gasolinera> resultadoFiltrado;

        // Determina que gasolineras mostrar
        switch (combustibleType) {
            case DIESEL: // Mostrar gasolineras con diesel
                resultadoFiltrado = filterByDiesel();
                break;
            case GASOLINA: // Mostrar gasolineras con gasolina
                resultadoFiltrado = filterByGasolina();
                break;
            default: // Mostrar todas
                resultadoFiltrado = repositoryGasolineras.getGasolineras();
                break;
        }
        if (resultadoFiltrado.isEmpty()) { // Si no hay gasolineras compatibles -> Lista vacia
            shownGasolineras = new ArrayList<>();
        } else {
            shownGasolineras = resultadoFiltrado;
        }
    }

    /**
     * Filtra por la marca o las marcas seleccionadas.
     *
     * @param marcas Lista de marca o marcas seleccionadas.
     */
    private void filterByBrand(List<String> marcas) {
        List<Gasolinera> resultadoFiltrado = new ArrayList<>();
        if (!marcas.isEmpty()) { // Solo se filtra si se han indicado marcas
            for (String m : marcas) {
                resultadoFiltrado.addAll(clearStationsWithoutBrand(m));
            }
            shownGasolineras = resultadoFiltrado;
        }
    }

    @Override
    public void orderByPrice(PriceOrderType order, PriceFilterType orderedValue) {
        // List with results
        List<Gasolinera> ordered;

        // Orders by defined criterion
        if (orderedValue == PriceFilterType.DIESEL) { // Diesel
            ordered = filterByDiesel();
            Collections.sort(ordered, new SortByDieselPrice(repositoryGasolineras, repositoryPromotions));
        } else if (orderedValue == PriceFilterType.GASOLINA) { // 95-octanes
            ordered = filterByGasolina();
            Collections.sort(ordered, new SortBy95OctanesPrice(repositoryGasolineras, repositoryPromotions));
        } else { // Summary price
            ordered = filterBySummaryPrice();
            Collections.sort(ordered, new SortBySummaryPrice(repositoryGasolineras, repositoryPromotions));
        }

        // Descending order (highest first, lowest last) --> reversed ascending list
        if (order == PriceOrderType.DESC) {
            Collections.reverse(ordered);
        }

        // Anhade las restantes
        for (Gasolinera g:shownGasolineras) {
            if (!ordered.contains(g)) {
                ordered.add(ordered.size(), g);
            }
        }

        if (ordered.isEmpty()) { // If empty list (no compatible gas stations)
            shownGasolineras = new ArrayList<>();
        } else {
            shownGasolineras = ordered;
        }
    }

    /**
     * Orders the list of gas stations based on the order (ascending or descending).
     * @return the ordered list
     */
    private List<Gasolinera> filterBySummaryPrice() {
        List<Gasolinera> filtered = new ArrayList<>();

        // Applies promotions
        for (Gasolinera gasolinera : getShownGasolineras()) {
            if (!gasolinera.getDieselA().equals("") && !gasolinera.getNormal95().equals("")) {
                filtered.add(gasolinera);
            }
        } // for
        return filtered;
    }


    /**
     * Genera una lista que contenga aquellas gasolineras con la marca que se introduce como parametro.
     *
     * @param marca Marca por la que se desea filtrar.
     * @return lista de gasolineras de las marcas seleccionadas.
     */
    private List<Gasolinera> clearStationsWithoutBrand(String marca) {
        List<Gasolinera> compatibles = new ArrayList<>();

        for (Gasolinera g : shownGasolineras) {
            if (g.getRotulo().equals(marca.toUpperCase())) {
                compatibles.add(g);
            }
        }
        return compatibles;
    }

    /**
     * Genera una lista que contenga aquellas gasolineras de la lista que actualmente se muestran
     * y que ofrecen diesel.
     *
     * @return lista de gasolineras que ofrecen diesel.
     */
    private List<Gasolinera> filterByDiesel() {
        List<Gasolinera> compatibles = new ArrayList<>();
        for (Gasolinera g : shownGasolineras) {
            if ((g.getDieselA() != null) && (!g.getDieselA().equals(""))) {
                compatibles.add(g);
            }
        }
        return compatibles;
    }

    /**
     * Genera una lista que contenga aquellas gasolineras de la lista que actualmente se muestran
     * y que ofrecen gasolina.
     *
     * @return lista de gasolineras que ofrecen gasolina.
     */
    private List<Gasolinera> filterByGasolina() {
        List<Gasolinera> compatibles = new ArrayList<>();
        for (Gasolinera g : shownGasolineras) {
            if ((g.getNormal95() != null) && (!g.getNormal95().equals(""))) {
                compatibles.add(g);
            }
        }
        return compatibles;
    }
}