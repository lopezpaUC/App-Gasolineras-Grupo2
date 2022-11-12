package es.unican.is.appgasolineras.repository;

import java.text.NumberFormat;
import java.util.List;

import es.unican.is.appgasolineras.common.Callback;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;

/**
 * A Repository to access gas stations/
 * This class abstracts away the source of the gas stations (REST API, local DB, etc.)
 */
public interface IGasolinerasRepository {

    /**
     * Request gas stations asynchronously
     * This method returns immediately. Once the gas stations have been retrieved from the source,
     * the provided callback is called
     *
     * @param cb
     */
    void requestGasolineras(Callback<List<Gasolinera>> cb);

    /**
     * Request gas stations synchronously
     * This method returns the list of gas stations directly, therefore it may impose a delay in
     * the execution until the list is retrieved from the source.
     *
     * @return the list of gas stations, or null if some error occurred
     */
    List<Gasolinera> getGasolineras();

    /**
     * Retorna gasolinera que se corresponda con los parametros indicados.
     *
     * @param name      Nombre / Rotulo
     * @param dir       Direccion
     * @param municipio Municipio
     * @return la gasolinera coincidente, o null si esta no existe.
     */
    Gasolinera getGasolineraByNameDirLocalidad(String name, String dir, String municipio);

    /**
     * Retorna lista de gasolineras que posean la promocion indicada.
     *
     * @param promID ID Promocion
     * @return lista de gasolinera relacionadas con la promocion indicada.
     */
    List<Gasolinera> getGasolinerasRelacionadasConPromocion(String promID);

    /**
     * Requests how the gas stations were loaded.
     */
    int getLoadingMethod();

    /**
     * Calculates the discounted price for a fuel based on its price and the promotion applied
     *
     * @param price     the base price
     * @param promotion the promotion which will be applied on the price
     * @return the discounted price
     */
    double calculateDiscountedPrice(double price, Promocion promotion);

    /**
     * Obtains the best promotion from a list of promotions, according to the given price
     *
     * @param price      the base price
     * @param promotions a list of promotions
     * @return the best promotion
     */
    Promocion bestPromotion(double price, List<Promocion> promotions, String fuel);

    /**
     * Produce el String correspondiente para el precio de sumario de la gasolinera.
     *
     * @param precio Precio de sumario, tipo double.
     * @return Precio de sumario como cadena de texto. "-" en caso de haber ocurrido algún error.
     */
    String precioSumarioToStr(double precio);

    /**
     * Comprueba que la cadena de texto relativa a un precio de la gasolinera contiene texto
     * a poder mostrar de forma valida.
     *
     * @param texto Texto a comprobar.
     * @return Mismo texto si la comprobacion ha sido satisfactoria.
     * Guion en caso de que el texto no pase la comprobacion.
     */
    String checkValidPrice(String texto);

    /**
     * Produce un valor double valido para un precio de combustible indicado como cadena de texto.
     *
     * @param precio  Precio de combustible como cadena de texto.
     * @param formato Formato a aplicar en la conversion.
     * @return Precio de combustible como valor valido convertido de tipo double.
     */
    double precioToDouble(String precio, NumberFormat formato);

    /**
     * Calculates the summary price given the diesel and 95-octanes prices
     *
     * @param dieselPrice     the price for diesel
     * @param unleaded95Price the price for 95-octanes
     * @return the summary price
     */
    double calculateSummary(double dieselPrice, double unleaded95Price);

    /**
     * Comprueba que la cadena de texto relativa a información de la gasolinera contiene texto
     * a poder mostrar de forma valida.
     *
     * @param texto Texto a comprobar.
     * @return Mismo texto si la comprobacion ha sido satisfactoria.
     *         Guion en caso de que el texto no pase la comprobacion.
     */
    String checkValid(String texto);
}
