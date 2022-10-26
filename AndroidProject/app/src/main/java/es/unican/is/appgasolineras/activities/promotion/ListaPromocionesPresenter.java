package es.unican.is.appgasolineras.activities.promotion;

import java.util.List;

import es.unican.is.appgasolineras.activities.main.IMainContract;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

public class ListaPromocionesPresenter implements IListaPromocionesContract.Presenter {

    // Constante para indicar si las promociones se cargan de forma online u offline
    private static final int LOAD_ONLINE = 0;

    // Vista principal
    private final IListaPromocionesContract.View view;

    // Repositorio de gasolineras
    private IPromocionesRepository repository;

    // Lista de promociones a mostrar
    private List<Promocion> shownPromociones;

    // Metodo utilizado para la carga de promociones
    private int loadMethod;

    /**
     * Constructor del presenter de la vista principal de promociones.
     *
     * @param view Vista principal
     */
    public ListaPromocionesPresenter(IListaPromocionesContract.View view) {
        this.view = view;
        loadMethod = LOAD_ONLINE; // Por defecto, se cargan de repositorio online
    }

    @Override
    public void init() {
        if (repository == null) { // Si no consta repositorio asignado
            //repository = view.getPromocionRepository();
        }

        if (repository != null) { // Si ya consta un repositorio
            doSyncInit();
        }
    }

    /**
     * Muestra contenido despues de intentar haber recibido el actualizado de internet.
     */
    private void doSyncInit() {
        List<Promocion> data = repository.getPromociones();

        if (!data.isEmpty()) { // Si se obtiene una lista con promociones
            // Obtiene si se ha cargado de BD o repositorio online.
            loadMethod = repository.getLoadingMethod();

            // Muestra promociones
            view.showPromociones(data);
            shownPromociones = data;

            if (loadMethod == LOAD_ONLINE) { // Si se obtienen de repositorio online
                // Muestra que estan actualizadas y el numero
                view.showLoadCorrectOnline(data.size());
            } else { // Si se obtienen de BD
                // Muestra la fecha de ultima actualizacion y el numero
                view.showLoadCorrectOffline(data.size());
            }
        } else { // Si no se obtienen gasolineras
            shownPromociones = null;
            view.showLoadError();
        }
    }

    @Override
    public List<Promocion> getShownPromociones() {
        return this.shownPromociones;
    }
}
