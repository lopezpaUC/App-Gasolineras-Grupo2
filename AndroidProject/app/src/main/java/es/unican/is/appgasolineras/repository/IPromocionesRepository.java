package es.unican.is.appgasolineras.repository;

import java.util.List;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Marca;
import es.unican.is.appgasolineras.model.MarcaPromocionCrossRef;
import es.unican.is.appgasolineras.model.Promocion;

public interface IPromocionesRepository {

    /**
     * Retorna lista de promociones relacionadas con la gasolinera indicada.
     *
     * @param idGasolinera ID Gasolinera.
     * @return lista de promociones relacionadas con la gasolinera indicada.
     */
    List<Promocion> getPromocionesRelacionadasConGasolinera(String idGasolinera);

    /**
     * Retorna lista de promociones registradas.
     *
     * @return lista de promociones registradas.
     */
    List<Promocion> getPromociones();

    /**
     * Retorna promocion en base a su ID.
     *
     * @param id ID Promocion.
     * @return promocion con el ID indicado.
     */
    Promocion getPromocionById(String id);

    /**
     * Inserta promociones indicadas en una lista.
     *
     * @param data promociones a insertar.
     */
    void insertPromociones (List<Promocion> data);

    /**
     * Inserta promocion indicada.
     *
     * @param data Promocion.
     */
    void insertPromocion (Promocion data);

    /**
     * Inserta marcas indicadas en una lista.
     *
     * @param data marcas a insertar.
     */
    void insertMarcas (List<Marca> data);

    /**
     * Inserta marca indicada.
     *
     * @param data Marca.
     */
    void insertMarca (Marca data);

    /**
     * Obtiene promociones relacionadas con una marca.
     *
     * @param idMarca ID Marca.
     * @return promociones relacionadas con la marca indicada.
     */
    List<Promocion> getPromocionesRelacionadasConMarca(String idMarca);

    /**
     * Obtiene marcas registradas.
     *
     * @return lista de marcas registradas.
     */
    List<Marca> getMarcas();

    /**
     * Inserta relacion entre gasolinera y promocion.
     *
     * @param g Gasolinera
     * @param p Promocion
     */
    void insertRelacionGasolineraPromocion(Gasolinera g, Promocion p);

    /**
     * Inserta relacion entre marca y promocion.
     *
     * @param m Marca
     * @param p Promocion
     */
    void insertRelacionMarcaPromocion(Marca m, Promocion p);

    /**
     * Borra una promocion y lo relacionado con ella.
     *
     * @param p Promocion
     * @return
     */
    Promocion deletePromocion(Promocion p);

    /**
     * Borra todas las promociones y lo relacionado con ellas.
     */
    void deleteAllPromociones();

    /**
     * Devuelve una lista de las marcas relacionadas con una promocion.
     * @param idPromocion id de la promocion
     * @return lista de marcas
     */
    List<MarcaPromocionCrossRef> getMarcasRelacionadasConPromocion(String idPromocion);
}
