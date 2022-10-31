package es.unican.is.appgasolineras.activities.promotion;

import java.util.List;

import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionRepository;

public class ListaPromocionesPresenter implements IListaPromocionesContract.Presenter {

    // Vista principal
    private final IListaPromocionesContract.View view;

    // Repositorio de promociones
    private IPromocionRepository repositoryPromociones;

    // Repositorio de gasolineras
    private IGasolinerasRepository repositoryGasolineras;

    // Lista de promociones a mostrar
    private List<Promocion> shownPromociones;

    /**
     * Constructor del presenter de la vista principal de promociones.
     *
     * @param view Vista principal
     */
    public ListaPromocionesPresenter(IListaPromocionesContract.View view) {
        this.view = view;
    }

    @Override
    public void init() {
        if (repositoryPromociones == null) { // Si no consta repositorio asignado
            repositoryPromociones = view.getPromocionRepository();
            repositoryGasolineras = view.getGasolineraRepository();
        }
        if (repositoryPromociones != null) { // Si ya consta un repositorio
            doSyncInit();
        }
    }

    /**
     * Muestra contenido despues de intentar haber recibido el actualizado de internet.
     */
    private void doSyncInit() {
        List<Promocion> data = repositoryPromociones.getPromociones();
        List<String> lista = null;
        for(Promocion promocion: data){
            if(repositoryGasolineras.getGasolinerasRelacionadasConPromocion(promocion.getId()).size()>1){
                lista.add("Varias");
            } else {
                lista.add(repositoryGasolineras.getGasolinerasRelacionadasConPromocion(promocion.getId()).get(0).getRotulo());
            }
        }

        if (!data.isEmpty()) { // Si se obtiene una lista con promociones
            // Muestra promociones
            view.showPromociones(data, lista);
            shownPromociones = data;

            // Avisamos que se han caragdo correctamente
            view.showLoadCorrect(data.size());
        } else { // Si no se obtienen gasolineras
            shownPromociones = null;
            // Avisamos que ha habido un error cargando las promociones
            view.showLoadError();
        }
    }

    @Override
    public List<Promocion> getShownPromociones() {
        return this.shownPromociones;
    }
}
