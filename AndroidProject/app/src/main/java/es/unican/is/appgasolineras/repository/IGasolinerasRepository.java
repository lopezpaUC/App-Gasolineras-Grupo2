package es.unican.is.appgasolineras.repository;

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
     * @param cb
     */
    void requestGasolineras(Callback<List<Gasolinera>> cb);

    /**
     * Request gas stations synchronously
     * This method returns the list of gas stations directly, therefore it may impose a delay in
     * the execution until the list is retrieved from the source.
     * @return the list of gas stations, or null if some error occurred
     */
    List<Gasolinera> getGasolineras();

    /**
     * Retorna gasolinera que se corresponda con los parametros indicados.
     * @param name Nombre / Rotulo
     * @param dir Direccion
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
     * @param price the base price
     * @param promotion the promotion which will be applied on the price
     * @return the discounted price
     */
    double calculateDiscountedPrice(double price, Promocion promotion);

    /**
     * Obtains the best promotion from a list of promotions, according to the given price
     * @param price the base price
     * @param promotions a list of promotions
     * @return the best promotion
     */
    Promocion bestPromotion(double price, List<Promocion> promotions, String fuel);
}
