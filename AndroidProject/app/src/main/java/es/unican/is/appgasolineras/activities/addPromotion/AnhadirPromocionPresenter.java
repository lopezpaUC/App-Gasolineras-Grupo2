package es.unican.is.appgasolineras.activities.addPromotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.unican.is.appgasolineras.model.Gasolinera;

import es.unican.is.appgasolineras.model.Marca;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

/**
 * Presenter para la actividad relacionada con registrar una nueva promocion.
 *
 * @author Grupo 02-CarbuRed
 * @version 1.0
 */
public class AnhadirPromocionPresenter implements IAnhadirPromocionContract.Presenter {

    private static final String DESCUENTO_PORCENTAJE = "%";
    private static final String DESCUENTO_EUROS_LITRO = "€/L";
    private static final double ERROR_DESCUENTO = -1.0;

    // Vista
    private final IAnhadirPromocionContract.View view;

    // Repositorios
    private IGasolinerasRepository repGasolineras;
    private IPromocionesRepository repPromociones;

    /**
     * Constructor del presenter de Anhadir Promociones.
     *
     * @param view vista
     */
    public AnhadirPromocionPresenter(IAnhadirPromocionContract.View view) {
        this.view = view;
    }

    @Override
    public void init() {
        this.repGasolineras = view.getGasolineraRepository();
        this.repPromociones = view.getPromocionRepository();
    }

    @Override
    public String[] getArrayGasolinerasParsed() {
        List<Gasolinera> gasolineras = repGasolineras.getGasolineras();
        Gasolinera g;

        String[] list = new String[gasolineras.size()];

        for (int i = 0; i < gasolineras.size(); i++) {
            g = gasolineras.get(i);
            list[i] = g.getRotulo() + " // " + g.getDireccion() + " // " + g.getMunicipio();
        }

        return list;
    }

    @Override
    public void onAnhadirClicked(Map<String, List<String>> infoList, Map<String, String> infoString) {

        // Obtener ID de la promocion
        String idPromocion = infoString.get("idPromocion");

        // Comprobar si existe promocion con el mismo ID
        if (repPromociones.getPromocionById(idPromocion) != null) {
            view.showStatus(EstadoOperacionAnhadirPromocion.REPETIDA);
            return;
        }

        // Obtener combustibles seleccionados como String
        @SuppressWarnings("ConstantConditions")
        String combustiblesParsed = parseSelectedCombustibles(infoList.get("selectedCombustibles"));

        // Comprobar que se seleccionaron combustibles
        if (combustiblesParsed == null) {
            view.showStatus(EstadoOperacionAnhadirPromocion.SIN_COMB);
            return;
        }

        // Obtener selecciones realizadas para una gasolinera o marcas.
        Gasolinera gasolineraParsed;
        List<Marca> marcasParsed;

        gasolineraParsed = parseSelectedGasolinera(infoString.get("selectedGasolinera"));
        //noinspection ConstantConditions
        marcasParsed = parseSelectedMarcas(infoList.get("selectedMarcas"));

        // Comprobar que se puede seleccionar alguna gasolinera
        //noinspection ConstantConditions
        if (gasolineraParsed == null && marcasParsed.isEmpty() &&
                !infoString.get("selectedCriterio").equals(infoString.get("stringValueAllGas"))) {
            view.showStatus(EstadoOperacionAnhadirPromocion.SIN_GASOLINERA);
            return;
        }

        // Comprobar que se ha introducido un descuento
        String descuento = infoString.get("descuento");
        if (descuento == null || descuento.equals("")) {
            view.showStatus(EstadoOperacionAnhadirPromocion.SIN_DESC);
            return;
        }

        // Obtiene el valor de los descuentos en formato numerico
        double descuentoPorcentual = parseDescuento(descuento);
        double descuentoEurosLitro = descuentoPorcentual;

        String tipoDescuento = infoString.get("selectedDescuentoTipo");
        //noinspection ConstantConditions
        switch (tipoDescuento) {
            case DESCUENTO_EUROS_LITRO:
                descuentoPorcentual = ERROR_DESCUENTO;
                break;
            case DESCUENTO_PORCENTAJE:
                descuentoEurosLitro = ERROR_DESCUENTO;
                break;
            default:
                break;
        }

        // Comprueba validez del descuento para el tipo escogido
        if (tipoDescuento.equals(DESCUENTO_PORCENTAJE) &&
                !checkDescuentoPorcentual(descuentoPorcentual)) {
            view.showStatus(EstadoOperacionAnhadirPromocion.PORC_NO_VALIDO);
            return;
        } else if (tipoDescuento.equals(DESCUENTO_EUROS_LITRO) &&
                !checkDescuentoEuroLitro(descuentoEurosLitro)) {
            view.showStatus(EstadoOperacionAnhadirPromocion.EURO_L_NO_VALIDO);
            return;
        }

        // Si los datos son correctos, persiste en base de datos
        Promocion p = new Promocion(idPromocion, descuentoPorcentual, descuentoEurosLitro,
                combustiblesParsed);
        try {
            repPromociones.insertPromocion(p); // Registra promocion

            if (gasolineraParsed != null) { // Si es para una gasolinera especifica
                repPromociones.insertRelacionGasolineraPromocion(gasolineraParsed, p);

            } else if (!marcasParsed.isEmpty()) { // Si es para un conjunto de marcas
                insertMarcasPromocion(marcasParsed, p);
                // Relacionar gasolineras correspondientes a las marcas
                for (Gasolinera g:repGasolineras.getGasolineras()) {
                    for (Marca m:marcasParsed) {
                        if (m.getNombre().toUpperCase(Locale.ROOT).equals(g.getRotulo())) {
                            repPromociones.insertRelacionGasolineraPromocion(g, p);
                        }
                    }
                }
            } else { // Si es para todas las gasolineras
                insertGasolinerasPromocion(p);
            }
        } catch (Exception e) {
            repPromociones.deletePromocion(p);
            view.showStatus(EstadoOperacionAnhadirPromocion.ERROR_BD);
            return;
        }

        view.showStatus(EstadoOperacionAnhadirPromocion.EXITO);
    }

    /**
     * Junta los combustibles seleccionados en un solo string en base a sus nombres.
     * @param combustibles Lista de combustibles seleccionados.
     * @return string con los combustibles seleccionados, separados por guiones.
     */
    private String parseSelectedCombustibles(List<String> combustibles) {
        // String nulo si no se seleccionaron combustibles
        if (combustibles.isEmpty()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = combustibles.iterator();
        builder.append(iterator.next());

        while (iterator.hasNext()) {
            builder.append("-");
            builder.append(iterator.next());
        }

        return builder.toString();
    }

    /**
     * Transforma una lista de nombres de marcas, en una lista de marcas representadas como
     * clases que reflejen la entidad en la base de datos.
     *
     * @param marcas Lista de nombres de marcas.
     * @return Lista de marcas como clases.
     */
    private List<Marca> parseSelectedMarcas(List<String> marcas) {
        List<Marca> mParsed = new ArrayList<>();

        for (String m:marcas) {
            mParsed.add(new Marca(m, false));
        }

        return mParsed;
    }

    /**
     * Obtiene de un string correspondiente al nombre, direccion y localidad de una gasolinera,
     * la gasolinera que representa.
     *
     * @param g String que representa gasolinera: Formato -> nombre // dir // localidad
     * @return Gasolinera a la que hace referencia el string.
     */
    private Gasolinera parseSelectedGasolinera(String g) {
        if (g!= null && !g.equals("-")) {
            String[] info = g.split(" // ");
            return repGasolineras.getGasolineraByNameDirLocalidad(info[0], info[1], info[2]);
        } else {
            return null;
        }
    }

    /**
     * Convierte un descuento representado como String, a un descuento como valor decimal.
     *
     * @param descuentoStr Descuento en formato de cadena de caracteres.
     * @return descuento como valor decimal, o -1.0 en caso de ocurrir un error.
     */
    private double parseDescuento(String descuentoStr) {
        double descuento;

        try {
            descuento = Double.parseDouble(descuentoStr);
        } catch (Exception e) {
            descuento = ERROR_DESCUENTO;
        }

        return descuento;
    }

    /**
     * Comprueba si el descuento porcentual es valido.
     *
     * @param descuento Descuento porcentual.
     * @return true si es valido.
     *         false en caso contrario.
     */
    private boolean checkDescuentoPorcentual(double descuento) {
        boolean validez;

        if (descuento == ERROR_DESCUENTO) { // Si no se convirtio correctamente
            validez = false;
        } else {
            validez = descuento <= 100.0 && descuento > 0.0;
        }

        return validez;
    }

    /**
     * Comprueba si el descuento en euros/litro es valido.
     *
     * @param descuento Descuento en euros/litro.
     * @return true si es valido.
     *         false en caso contrario.
     */
    private boolean checkDescuentoEuroLitro(double descuento) {
        boolean validez;

        if (descuento == ERROR_DESCUENTO) { // Si no se convirtio correctamente
            validez = false;
        } else {
            validez = descuento > 0.0;
        }

        return validez;
    }

    /**
     * Inserta marcas y su relacion con la promocion
     * @param marcasParsed Marcas relacionadas con la promocion
     * @param p Promocion
     */
    private void insertMarcasPromocion(List<Marca> marcasParsed, Promocion p) {
        for (Marca m : marcasParsed) {
            repPromociones.insertMarca(m);
            repPromociones.insertRelacionMarcaPromocion(m, p);
        }
    }

    /**
     * Inserta todas las relaciones de las gasolineras con la promocion.
     * @param p Promocion
     */
    private void insertGasolinerasPromocion(Promocion p) {
        for (Gasolinera g : repGasolineras.getGasolineras()) {
            repPromociones.insertRelacionGasolineraPromocion(g, p);
        }
    }
}





















