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
    @Query("SELECT * FROM promociones")
    List<Promocion> getPromociones();

    @Insert
    void insertAll(Promocion... promociones);

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
}
