package es.unican.is.appgasolineras.common.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import es.unican.is.appgasolineras.model.Promocion;

public class PriceUtilities {
    private static final double ERROR_CONVERSION = -1.0; // Error convirtiendo String a double

    public PriceUtilities() {}

    /**
     * Calculates the discounted price for a fuel based on its price and the promotion applied
     *
     * @param price     the base price
     * @param promotion the promotion which will be applied on the price
     * @return the discounted price
     */
    public double calculateDiscountedPrice(double price, Promocion promotion) {
        if (promotion == null) {
            return price;
        }

        double euros = promotion.getDescuentoEurosLitro();
        if (euros > 0) {
            return price - euros;
        } else {
            return price * (100 - promotion.getDescuentoPorcentual()) / 100;
        }
    }

    /**
     * Converts the summary price from a numeric value to text
     * @param precio the price to be converted
     * @return the price as text
     */
    public String precioSumarioToStr(double precio) {
        String precioTxt = "-";

        if (precio > 0.0) { // Si el precio es valido
            precioTxt = String.format(Locale.FRANCE, "%.2f", precio);
        }

        return precioTxt;
    }

    /**
     * Comprueba que la cadena de texto relativa a un precio de la gasolinera contiene texto
     * a poder mostrar de forma valida.
     *
     * @param texto Texto a comprobar.
     * @return Mismo texto si la comprobacion ha sido satisfactoria.
     *         Guion en caso de que el texto no pase la comprobacion.
     */
    public String checkValidPrice(String texto) {
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

    public double calculateSummary(double dieselPrice, double unleaded95Price) {
        double summary;
        // Determines the summary price based on the validity of both prices
        if (dieselPrice <= 0.0) { // Invalid diesel price
            summary = unleaded95Price;
        } else if(unleaded95Price <= 0.0) { // Invalid 95-octanes price
            summary = dieselPrice;
        } else { // Both prices are valid
            summary = (dieselPrice + unleaded95Price * 2.0) / 3.0;
        }
        return summary;
    }

    /**
     * Produce un valor double valido para un precio de combustible indicado como cadena de texto.
     * @param precio Precio de combustible como cadena de texto.
     * @param formato Formato a aplicar en la conversion.
     * @return Precio de combustible como valor valido convertido de tipo double.
     */
    public double precioToDouble(String precio, NumberFormat formato) {
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
     * Comprueba que la cadena de texto relativa a información de la gasolinera contiene texto
     * a poder mostrar de forma valida.
     *
     * @param texto Texto a comprobar.
     * @return Mismo texto si la comprobacion ha sido satisfactoria.
     *         Guion en caso de que el texto no pase la comprobacion.
     */
    public String checkValid(String texto) {
        String correccion = texto;

        if (texto.equals("")) { // Si no se contiene ninguna informacion
            correccion = "-";
        }

        return correccion;
    }
}
