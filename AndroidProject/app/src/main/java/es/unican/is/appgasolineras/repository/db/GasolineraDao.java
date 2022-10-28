package es.unican.is.appgasolineras.repository.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
/**
 * DAO defined with Room
 * Usage: https://developer.android.com/training/data-storage/room
 */
@Dao
public interface GasolineraDao {

    @Query("SELECT * FROM gasolineras")
    List<Gasolinera> getAll();

    @Insert
    void insertAll(Gasolinera... gasolineras);

    @Query("DELETE FROM gasolineras")
    void deleteAll();

    @Query("SELECT * FROM promociones inner join gasolinera_promocion " +
    "on promociones.id = gasolinera_promocion.promocionID where gasolinera_promocion.gasolineraID" +
    " = :gasID")
    List<Promocion> findPromocionesRelatedById(String gasID);

}
