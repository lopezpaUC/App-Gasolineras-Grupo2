package es.unican.is.appgasolineras.activities.main;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import es.unican.is.appgasolineras.common.Callback;
import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

public class MainPresenter implements IMainContract.Presenter {
    // Constantes para indicar si las gasolineras se cargan de forma online u offline
    private static final int LOAD_OFFLINE = 1;
    private static final int LOAD_ONLINE = 0;

    private final IMainContract.View view;
    private IGasolinerasRepository repository;

    private List<Gasolinera> shownGasolineras;

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

    private void doAsyncInit() {
        repository.requestGasolineras(new Callback<List<Gasolinera>>() {
            @Override
            public void onSuccess(List<Gasolinera> data) {
                view.showGasolineras(data);
                shownGasolineras = data;
                view.showLoadCorrectOnline(data.size());
            }

            @Override
            public void onFailure() {
                shownGasolineras = null;
                view.showLoadError();
            }
        });
    }

    private void doSyncInit() {
        List<Gasolinera> data = repository.getGasolineras();

        if (data != null) {
            showGasInfo(data, LOAD_ONLINE);

        } else {
            data = repository.getGasolinerasOffline();
            if (data.isEmpty()) {
                shownGasolineras = null;
                view.showLoadError();
            } else {
                showGasInfo(data, LOAD_OFFLINE);
            }

        }
    }

    /**
     * Ordena a la vista mostrar la información de las gasolineras.
     * @param data Lista de gasolineras a mostrar.
     * @param loadMethod Método usado para cargar gasolineras, lo que determina si los datos
     *                   son o no recientes.
     */
    private void showGasInfo(List<Gasolinera> data, int loadMethod) {
        view.showGasolineras(data);
        shownGasolineras = data;
        if (loadMethod == LOAD_OFFLINE) {
            view.showLoadCorrectOffline(data.size());
        }
        view.showLoadCorrectOnline(data.size());
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
}
