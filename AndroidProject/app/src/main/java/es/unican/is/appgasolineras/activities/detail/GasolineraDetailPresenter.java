package es.unican.is.appgasolineras.activities.detail;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.unican.is.appgasolineras.model.Gasolinera;

/**
 * Presenter para la actividad relacionada con la muestra de información detallada de una
 * gasolinera.
 *
 * @author Grupo 02-CarbuRed
 * @version 1.0
 */
public class GasolineraDetailPresenter implements IGasolineraDetailContract.Presenter {

    private static final double ERROR_CONVERSION = -1.0; // Error convirtiendo String a double
    private final IGasolineraDetailContract.View view;   // Vista encargada de mostrar informacion
    private final Gasolinera gasolinera;                 // Gasolinera a mostrar
    private String precioSumarioStr;                     // Precio sumario de la gasolinera

    /**
     * Constructor del presenter.
     *
     * @param view Vista con la que comunicarse.
     * @param gasolinera Gasolinera cuya informacion debe tratarse.
     */
    public GasolineraDetailPresenter(IGasolineraDetailContract.View view, Gasolinera gasolinera) {
        this.view = view;
        this.gasolinera = gasolinera;
    }

    @Override
    public void init() {
        // Obtiene el precio sumario
        precioSumarioStr = precioSumarioToStr(calculateSummaryPrice());
        // Solicita a la vista que muestre la informacion requerida
        loadGasolineraDetails();
    }

    @Override
    public void onAcceptClicked() {
            view.openMainView();
    }

    /**
     * Retorna el precio sumario de la gasolinera.
     * @return Precio sumario de la gasolinera como cadena de caracteres.
     */
    @Override
    public String getPrecioSumario() {
        return precioSumarioStr;
    }

    /**
     * Calcula el precio sumario de la gasolinera.
     * @return Precio sumario.
     */
    private double calculateSummaryPrice() {
        // Obtiene los precios de los tipos de combustible como cadena de texto
        String precioDieselStr = gasolinera.getDieselA();
        String precioGasolinaStr = gasolinera.getNormal95();

        // Variables para almacenar los precios en formato numerico en lugar de cadenas de texto
        double precioDiesel;
        double precioGasolina;

        // Define el formato a utilizar para el 'parseo' de los precios
        NumberFormat formato = NumberFormat.getInstance(Locale.FRANCE);

        // Convierte a double el precio del diesel A
        precioDiesel = precioToDouble(precioDieselStr, formato);

        // Convierte a double el precio de la gasolina 95
        precioGasolina = precioToDouble(precioGasolinaStr, formato);

        double sumario;

        // Determina el precio de sumario en base a la validez de los precios del combustible
        if (precioDiesel <= 0.0) { // Si no hay un precio de diesel valido
            sumario = precioGasolina;
        } else if(precioGasolina <= 0.0) { // Si no hay un precio de gasolina valido
            sumario = precioDiesel;
        } else { // Si todos los precios son validos
            sumario = (precioDiesel + precioGasolina * 2.0) / 3.0;
        }

        return sumario;
    }

    /**
     * Obtiene los datos necesarios a cargar en la vista, y ordena a esta que se muestren.
     */
    private void loadGasolineraDetails() {
        // Si la gasolinera solo tiene su nombre como informacion
        if (gasolinera.toString().equals(gasolinera.getRotulo())) {
            // Ordena a la vista mostrar un mensaje de error
            view.showLoadError();
        } else { // De lo contrario, si contiene mas informacion, la carga y ordena mostrarla
            Map<String, String> info = new HashMap<>();
            info.put("summary", precioSumarioStr + " €/L");
            info.put("label", checkValid(gasolinera.getRotulo()));
            info.put("municipality", checkValid(gasolinera.getMunicipio()));
            info.put("direction", checkValid(gasolinera.getDireccion()));
            info.put("cp", checkValid(gasolinera.getCp()));
            info.put("price95", checkValidPrice(gasolinera.getNormal95()) + " €/L");
            info.put("priceDieselA", checkValidPrice(gasolinera.getDieselA()) + " €/L");
            info.put("schedule", checkValid(gasolinera.getHorario()));

            view.showInfo(info);
        }
    }

    /**
     * Produce un valor double valido para un precio de combustible indicado como cadena de texto.
     * @param precio Precio de combustible como cadena de texto.
     * @param formato Formato a aplicar en la conversion.
     * @return Precio de combustible como valor valido convertido de tipo double.
     */
    private double precioToDouble(String precio, NumberFormat formato) {
        double precioDouble;

        try {
            Number number = formato.parse(precio);

            // Comprueba si se puede obtener el valor en formato double
            if (number != null) {
                precioDouble = number.doubleValue();
            } else {
                precioDouble = ERROR_CONVERSION;
            }

        } catch (ParseException e) { // Si hay un error en la conversion
            precioDouble = ERROR_CONVERSION;
        }

        return precioDouble;
    }

    /**
     * Produce el String correspondiente para el precio de sumario de la gasolinera.
     *
     * @param precio Precio de sumario, tipo double.
     * @return Precio de sumario como cadena de texto. "-" en caso de haber ocurrido algún error.
     */
    private String precioSumarioToStr(double precio) {
        String precioTxt = "-";

        if (precio > 0.0) { // Si el precio es valido
            precioTxt = String.format(Locale.FRANCE, "%.2f", precio);
        }

        return precioTxt;
    }

    /**
     * Comprueba que la cadena de texto relativa a información de la gasolinera contiene texto
     * a poder mostrar de forma valida.
     *
     * @param texto Texto a comprobar.
     * @return Mismo texto si la comprobacion ha sido satisfactoria.
     *         Guion en caso de que el texto no pase la comprobacion.
     */
    private String checkValid(String texto) {
        String correccion = texto;

        if (texto.equals("")) { // Si no se contiene ninguna informacion
            correccion = "-";
        }

        return correccion;
    }

    /**
     * Comprueba que la cadena de texto relativa a un precio de la gasolinera contiene texto
     * a poder mostrar de forma valida.
     *
     * @param texto Texto a comprobar.
     * @return Mismo texto si la comprobacion ha sido satisfactoria.
     *         Guion en caso de que el texto no pase la comprobacion.
     */
    private String checkValidPrice(String texto) {
        String correccion = texto;

        if (texto.contains("-") || texto.equals("")) { // Si es negativo o no contiene informacion
            correccion = "-";
        } else { // Prepara el string para que solo muestre dos decimales
            StringBuilder sb = new StringBuilder(correccion);
            sb.deleteCharAt(sb.length()-1);
            correccion = sb.toString();
        }

        return correccion;
    }
}
