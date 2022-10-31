package es.unican.is.appgasolineras.repository.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import es.unican.is.appgasolineras.model.GasolineraPromocionCrossRef;
import es.unican.is.appgasolineras.model.Promocion;

/**
 * DAO defined with Room
 * Usage: https://developer.android.com/training/data-storage/room
 */
@Dao
public interface PromocionDao {
    @Query("SELECT * FROM promociones where id = :id")
    Promocion getPromocionById(String id);

    @Query("SELECT * FROM promociones")
    List<Promocion> getPromociones();

    @Insert
    void insertAll(Promocion... promociones);

    @Insert
    void insert(Promocion promocion);

    @Query("DELETE FROM promociones")
    void deleteAll();

    /**
     * Inserta una relacion entre gasolinera y promocion.
     * @param gasID ID Gasolinera
     * @param promID ID Promocion
     */
    @Query("INSERT INTO gasolinera_promocion values(:gasID, :promID)")
    void insertRelationGasolineraPromocion(String gasID, String promID);

    /**
     * Elimina una relacion entre gasolinera y promocion.
     * @param gasID ID Gasolinera
     * @param promID ID Promocion
     */
    @Query("DELETE FROM gasolinera_promocion where gasolineraID = :gasID and promocionID = :promID")
    void deleteRelationGasolineraPromocion(String gasID, String promID);

    /**
     * Devuelve las parejas Gasolinera-Promocion correspondientes a una promocion.
     * @param promID Promocion de la que interesa obtener las gasolineras a las que afecta.
     * @return lista de parejas IDGasolinera-IDPromocion, para la promocion indicada.
     */
    @Query("SELECT * FROM gasolinera_promocion where promocionID = :promID")
    List<GasolineraPromocionCrossRef> findGasolinerasRelatedByID(String promID);

    /**
     * Devuelve las promociones relacionadas con una gasolinera.
     *
     * @param gasID ID Gasolinera
     * @return lista de promociones relacionadas con la gasolinera
     */
    @Query("SELECT * FROM promociones inner join gasolinera_promocion " + "on promociones.id = " +
            "gasolinera_promocion.promocionID where gasolinera_promocion.gasolineraID " +
            "= :gasID")
    List<Promocion> buscaPromocionesRelacionadasConGasolinera(String gasID);

    /**
     * Devuelve las promociones relacionadas con una marca.
     *
     * @param marcaID ID Marca.
     * @return lista de promociones relacionadas con la marca.
     */
    @Query("SELECT * FROM promociones inner join promocion_marca " + "on promociones.id = " +
            "promocion_marca.promocionID where promocion_marca.marcaID " +
            "= :marcaID")
    List<Promocion> buscaPromocionesRelacionadasConMarca(String marcaID);
}
