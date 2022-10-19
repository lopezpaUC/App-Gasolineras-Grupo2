package es.unican.is.appgasolineras.activities.main;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.common.Callback;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

public class MainPresenter implements IMainContract.Presenter {
    // Constante para indicar si las gasolineras se cargan de forma online u offline
    private static final int LOAD_ONLINE = 0;

    // Vista principal
    private final IMainContract.View view;

    // Repositorio de gasolineras
    private IGasolinerasRepository repository;

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
        if (repository == null) { // Si no consta repositorio asignado
            repository = view.getGasolineraRepository();
        }

        if (repository != null) { // Si ya consta un repositorio
            doSyncInit();
        }
    }

    /**
     * Muestra contenido antes de haber intentado recibir el actualizado de internet.
     */
    private void doAsyncInit() {
        repository.requestGasolineras(new Callback<List<Gasolinera>>() {
            @Override
            public void onSuccess(List<Gasolinera> data) {
                loadMethod = repository.getLoadingMethod();
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
    }

    /**
     * Muestra contenido despues de intentar haber recibido el actualizado de internet.
     */
    private void doSyncInit() {
        List<Gasolinera> data = repository.getGasolineras();

        if (!data.isEmpty()) { // Si se obtiene una lista con gasolineras
            // Obtiene si se ha cargado de BD o repositorio online.
            loadMethod = repository.getLoadingMethod();

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
    public void onRefreshClicked() {
        init();
    }

    /**
     * Filtra la lista de gasolineras en funcion de unas marcas seleccionadas y/o un tipo
     * determinado de combustible.
     *
     * @param combustibleType Tipo de combustible.
     * @param brands Mara o listado de marcas.
     */
    @Override
    public void filter(CombustibleType combustibleType, List<String> brands) {
        shownGasolineras = repository.getGasolineras();
        filterByCombustible(combustibleType);
        filterByBrand(brands);

        if (!shownGasolineras.isEmpty()) { // Si hay gasolineras a mostrar despues de filtrado
            view.showGasolineras(shownGasolineras);

            // Muestra la informacion de las gasolineras obtenidas
            if (loadMethod == LOAD_ONLINE) {
                view.showLoadCorrectOnline(shownGasolineras.size());
            } else {
                view.showLoadCorrectOffline(shownGasolineras.size());
            }

        } else { // Si no hay gasolineras a mostrar despues de filtrado
            view.showLoadError();
            shownGasolineras = null;
        }
    }

    /**
     * Filtra por tipo de combustible.
     * @param combustibleType Tipo de combustible a utilizar para filtrar
     */
    private void filterByCombustible(CombustibleType combustibleType) {
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
                resultadoFiltrado = repository.getGasolineras();
                break;
        }
        if (resultadoFiltrado.isEmpty()) {
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
        if (!marcas.isEmpty()) {
            for (String m:marcas) {
                 resultadoFiltrado.addAll(clearStationsWithoutBrand(m));
            }
            shownGasolineras = resultadoFiltrado;
        }
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
        for (Gasolinera g:shownGasolineras) {
            if ((g.getNormal95() != null) && (!g.getNormal95().equals(""))) {
                compatibles.add(g);
            }
        }
        return compatibles;
    }
}
