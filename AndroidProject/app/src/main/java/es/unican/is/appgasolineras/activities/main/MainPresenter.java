package es.unican.is.appgasolineras.activities.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

        if (repositoryGasolineras != null) { // Si ya consta un repositorio
            doSyncInit();
        }
    }

    /** NO USADO POR EL MOMENTO
     * Muestra contenido antes de haber intentado recibir el actualizado de internet.
     */
    /**private void doAsyncInit() {
        repositoryGasolineras.requestGasolineras(new Callback<List<Gasolinera>>() {
            @Override
            public void onSuccess(List<Gasolinera> data) {
                loadMethod = repositoryGasolineras.getLoadingMethod();
                view.showGasolineras(data);
                shownGasolineras = data;
                if (loadMethod == LOAD_ONLINE) {
                    view.showLoadCorrectOnline(data.size());
                } else {
                    view.showLoadCorrectOffline(data.size());
                }
            }

            @Override
            public void onFailure() {
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
    public void onFilterClicked(){
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

    //public void onFilterByPriceClicked() { view.openPriceFilterDialog(); }

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

    public void filterByPrice() {

    }
    /**
     * Filtra por tipo de combustible.
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
     * @param marcas Lista de marca o marcas seleccionadas.
     */
    private void filterByBrand(List<String> marcas) {
        List<Gasolinera> resultadoFiltrado = new ArrayList<>();
        if (!marcas.isEmpty()) { // Solo se filtra si se han indicado marcas
            for (String m:marcas) {
                 resultadoFiltrado.addAll(clearStationsWithoutBrand(m));
            }
            shownGasolineras = resultadoFiltrado;
        }
    }

    private List<Gasolinera> filterByPrice(PriceOrderType order, PriceFilterType orderedValue) {
        // List with results
        List<Gasolinera> filtered = new LinkedList<>();

        // Applies promotions
        if (orderedValue == PriceFilterType.DIESEL || orderedValue == PriceFilterType.GASOLINA) {
            for (Gasolinera gasolinera : filtered) {
                List<Promocion> promotions = repositoryPromotions.
                        getPromocionesRelacionadasConGasolinera(gasolinera.getId());

                double dieselPrice = Double.parseDouble(gasolinera.getDieselA());
                double unleaded95Price = Double.parseDouble(gasolinera.getNormal95());
                Promocion bestPromotionDiesel;
                Promocion bestPromotion95Octanes;

                // There is a promotion to be applied
                if (!promotions.isEmpty()) {
                    // Gets the best promotion for either diesel or unleaded 95 octanes
                    if (orderedValue == PriceFilterType.DIESEL) { // Diesel
                        // Gets the best promotion for diesel
                        bestPromotionDiesel = repositoryGasolineras.bestPromotion(dieselPrice, promotions, "Diésel");

                        // Valid promotion for diesel, updates price
                        if (bestPromotionDiesel != null) {
                            dieselPrice = repositoryGasolineras.calculateDiscountedPrice(dieselPrice, bestPromotionDiesel);
                        }
                    } else if (orderedValue == PriceFilterType.GASOLINA) { // 95 octanes
                        bestPromotion95Octanes = repositoryGasolineras.bestPromotion(unleaded95Price, promotions, "Gasolina");

                        // Valid promotion for 95 octanes, updates price
                        if (bestPromotion95Octanes != null) {
                            unleaded95Price = repositoryGasolineras.calculateDiscountedPrice(unleaded95Price, bestPromotion95Octanes);
                        }
                    } else { // Summary price
                        double summaryPrice = gasolinera.getSummaryPrice();
                    } // else
                } // if
                gasolinera.setDiscountedDiesel(String.valueOf(dieselPrice));
                gasolinera.setDiscounted95(String.valueOf(unleaded95Price));
            } // for
        } // if (orderedValue)

        // Filters by defined criterion
        if (orderedValue == PriceFilterType.DIESEL) { // Diesel
            filtered = filterByDiesel();
            Collections.sort(filtered, new SortByDieselPrice());
        } else if (orderedValue == PriceFilterType.GASOLINA) { // 95-octanes
            filtered = filterByGasolina();
            Collections.sort(filtered, new SortBy95OctanesPrice());
        } else { // Summary price
            Collections.sort(filtered, new SortBySummaryPrice());
        }

        Iterator<Gasolinera> iterator = filtered.iterator();

        // Descending order (highest first, lowest last) --> reversed ascending list
        if (order == PriceOrderType.DESC) {
            Collections.reverse(filtered);
        }

        return filtered;
    }


    /**
     * Genera una lista que contenga aquellas gasolineras con la marca que se introduce como parametro.
     * @param marca Marca por la que se desea filtrar.
     * @return lista de gasolineras de las marcas seleccionadas.
     */
    private List<Gasolinera> clearStationsWithoutBrand (String marca) {
        List<Gasolinera> compatibles = new ArrayList<>();

        for (Gasolinera g:shownGasolineras) {
            if (g.getRotulo().equals(marca.toUpperCase())) {
                compatibles.add(g);
            }
        }
        return compatibles;
    }

    /**
     * Genera una lista que contenga aquellas gasolineras de la lista que actualmente se muestran
     * y que ofrecen diesel.
     * @return lista de gasolineras que ofrecen diesel.
     */
    private List<Gasolinera> filterByDiesel() {
        List<Gasolinera> compatibles = new ArrayList<>();
        for (Gasolinera g:shownGasolineras) {
            if ((g.getDieselA() != null) && (!g.getDieselA().equals(""))) {
                compatibles.add(g);
            }
        }
        return compatibles;
    }

    /**
     * Genera una lista que contenga aquellas gasolineras de la lista que actualmente se muestran
     * y que ofrecen gasolina.
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
