package es.unican.is.appgasolineras.activities.main;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.unican.is.appgasolineras.common.Callback;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

public class MainPresenter implements IMainContract.Presenter {
    // Constantes para indicar si las gasolineras se cargan de forma online u offline
    private static final int LOAD_ONLINE = 0;
    private static final int DIESEL = 1;
    private static final int GASOLINA = 2;

    private final IMainContract.View view;
    private IGasolinerasRepository repository;

    private List<Gasolinera> shownGasolineras;
    private int loadMethod = LOAD_ONLINE;


    public MainPresenter(IMainContract.View view) {
        this.view = view;
    }

    @Override
    public void init() {
        if (repository == null) {
            repository = view.getGasolineraRepository();
        }

        if (repository != null) {
            doSyncInit();
        }
    }


    /**
     * Muestra contenido antes de haber intentado recibir el actualizado.
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
     * Muestra contenido despues de intentar haber recibido el actualizado.
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

    public void filter(int combustibleType, List<String> brands) {
        shownGasolineras = repository.getGasolineras();
        filterByCombustible(combustibleType);

        filterByMarca(brands);

        if (!shownGasolineras.isEmpty()) {
            view.showGasolineras(shownGasolineras);
            if (loadMethod == LOAD_ONLINE) {
                view.showLoadCorrectOnline(shownGasolineras.size());
            } else {
            view.showLoadCorrectOffline(shownGasolineras.size());
            }
        } else {
            view.showLoadError();
            shownGasolineras = null;
        }

    }


    public void filterByMarca(List<String> marcas) {
        Set<Gasolinera> resultadoFiltrado = new HashSet<>();
        Set<Gasolinera> shownOldGasolineras = new HashSet<>(repository.getGasolineras());

        if (marcas.size() == 0){
            resultadoFiltrado.addAll(repository.getGasolineras());
        }else{
                for (int i = 0; i < marcas.size(); i++) {
                    resultadoFiltrado.addAll(filterByMarcas(marcas.get(i).toString()));
                }

        }

        shownOldGasolineras.retainAll(resultadoFiltrado);
        Log.d("DEBUG", String.format("%s",resultadoFiltrado));
        if (shownOldGasolineras.isEmpty()) {
            shownGasolineras = new ArrayList<>();
        } else {
            shownGasolineras = new ArrayList<>(shownOldGasolineras);
        }
    }

    private Set<Gasolinera> filterByMarcas(String marca) {
        Set<Gasolinera> compatibles = new HashSet<>();
        for (Gasolinera g:shownGasolineras) {
            if (g.getRotulo().equals(marca.toUpperCase())) {
                compatibles.add(g);
            }
        }
        return compatibles;
    }


    private void filterByCombustible(int combustibleType) {
        CombustibleType combustibleFiltrar = CombustibleType.getCombTypeFromInt(combustibleType);
        Set<Gasolinera> resultadoFiltrado;
        Set<Gasolinera> shownOldGasolineras = new HashSet<>(repository.getGasolineras());
        switch (combustibleFiltrar) {

            case DIESEL:
                resultadoFiltrado = filterByDiesel();
                break;
            case GASOLINA:
                resultadoFiltrado = filterByGasolina();
                break;
            default:
                resultadoFiltrado = new HashSet<>(repository.getGasolineras());
                break;
        }
        shownOldGasolineras.retainAll(resultadoFiltrado);
        Log.d("DEBUG", String.format("%s",resultadoFiltrado));
        if (shownOldGasolineras.isEmpty()) {
            shownGasolineras = new ArrayList<>();
        } else {
            shownGasolineras = new ArrayList<>(shownOldGasolineras);
        }
    }

    private Set<Gasolinera> filterByDiesel() {
        Set<Gasolinera> compatibles = new HashSet<>();
        for (Gasolinera g:shownGasolineras) {
            if ((g.getDieselA() != null) && (!g.getDieselA().equals(""))) {
                compatibles.add(g);
            }
        }
        return compatibles;
    }

    private Set<Gasolinera> filterByGasolina() {
        Set<Gasolinera> compatibles = new HashSet<>();
        for (Gasolinera g:shownGasolineras) {
            if ((g.getNormal95() != null) && (!g.getNormal95().equals(""))) {
                compatibles.add(g);
            }
        }
        return compatibles;
    }

    public List<Gasolinera> getShownGasolineras() {
        return this.shownGasolineras;
    }
}
