package es.unican.is.appgasolineras.activities.promotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

public class ListaPromocionesPresenter implements IListaPromocionesContract.Presenter {

    // Vista principal
    private final IListaPromocionesContract.View view;

    // Repositorio de promociones
    private IPromocionesRepository repositoryPromociones;

    // Repositorio de gasolineras
    private IGasolinerasRepository repositoryGasolineras;

    // Lista de promociones a mostrar
    private List<Promocion> shownPromociones;

    // Lista con los strings de las gasolineras
    private List<String> listaNombreGasolineras = new ArrayList<>();

    // Lista con los rotulos que debe mostrar la promocion
    private List<String> listaImagenPromocion = new ArrayList<>();

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
     * Promocion a enseñar, de la cuál obtener las gasolineras y rótulo que tiene asociados
     * @param promocion Promocion
     */
    public void promocionAEnsenhar(Promocion promocion){
        if(repositoryGasolineras.getGasolinerasRelacionadasConPromocion(promocion.getId()).size()>1){
            listaNombreGasolineras.add("Varias");
            listaImagenPromocion.add("composicion");
        } else {
            listaNombreGasolineras.add(repositoryGasolineras.getGasolinerasRelacionadasConPromocion(promocion.getId()).get(0).getRotulo());
            listaImagenPromocion.add(repositoryGasolineras.getGasolinerasRelacionadasConPromocion(promocion.getId()).get(0).getRotulo().toLowerCase());
        }
    }

    /**
     * Muestra contenido despues de intentar haber recibido el actualizado de internet.
     */
    private void doSyncInit() {
        List<Promocion> data = repositoryPromociones.getPromociones();

        for(Promocion promocion: data){
            promocionAEnsenhar(promocion);
        }

        if (!data.isEmpty()) { // Si se obtiene una lista con promociones
            // Muestra promociones
            view.showPromociones(data, listaNombreGasolineras, listaImagenPromocion);
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
