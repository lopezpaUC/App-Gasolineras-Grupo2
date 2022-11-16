package es.unican.is.appgasolineras.activities.promotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.MarcaPromocionCrossRef;
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
        List<Gasolinera> listaGasolineras = repositoryGasolineras.getGasolinerasRelacionadasConPromocion(promocion.getId());
        String marca = listaGasolineras.get(0).getRotulo();
        Boolean variasMarcas = false;
        if(listaGasolineras.size()>1){
            // Comprobamos si solo es una marca o varias
            if(repositoryPromociones.getMarcasRelacionadasConPromocion(promocion.getId()).size()>1)
                listaImagenPromocion.add("composicion");
            else
                listaImagenPromocion.add(marca.toLowerCase());
            listaNombreGasolineras.add("Varias");
        } else {
            listaNombreGasolineras.add(acortaString(listaGasolineras.get(0).getRotulo()));
            listaImagenPromocion.add(listaGasolineras.get(0).getRotulo().toLowerCase());
        }
    }

    /**
     * Muestra contenido.
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


    @Override

    public Promocion deletePromotion(String nombre) {
        return repositoryPromociones.deletePromocion(repositoryPromociones.getPromocionById(nombre));

    }

    @Override
    public boolean listaPromocionesVacia(){
        return repositoryPromociones.getPromociones().isEmpty();
    }

    @Override
    public String acortaString(String nombreGasolinera){
        String nombreAcortado = nombreGasolinera;
        if (nombreAcortado.contains("E.S. "))
            nombreAcortado = nombreAcortado.substring(5, nombreAcortado.length());
        if (nombreAcortado.contains("E.S."))
            nombreAcortado = nombreAcortado.substring(4, nombreAcortado.length());
        if (nombreAcortado.contains("S.L."))
            nombreAcortado = nombreAcortado.substring(0, nombreAcortado.length()-5);
        if (nombreAcortado.contains("S.L"))
            nombreAcortado = nombreAcortado.substring(0, nombreAcortado.length()-4);
        System.out.println(nombreAcortado);
        if (nombreAcortado.contains("AREA"))
            nombreAcortado = nombreAcortado.substring(17, nombreAcortado.length());
        if (nombreAcortado.contains("ESTACION"))
            nombreAcortado = nombreAcortado.substring(21, nombreAcortado.length());
        if (nombreAcortado.length()>10)
            nombreAcortado = nombreAcortado.substring(0, 9) + "...";
        return nombreAcortado;
    }
}
