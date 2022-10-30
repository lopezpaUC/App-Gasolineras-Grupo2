package es.unican.is.appgasolineras.repository.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import es.unican.is.appgasolineras.model.Gasolinera;
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

    @Query("SELECT * FROM gasolineras WHERE rotulo = :name and direccion = :dir and " +
            "municipio = :mun")
    Gasolinera getByNameDirLocalidad(String name, String dir, String mun);

    /**
     * Devuelve las gasolineras relacionadas con una promocion.
     *
     * @param promID ID Promocion
     * @return lista de gasolineras relacionadas con la promocion.
     */
    @Query("SELECT * FROM gasolineras inner join gasolinera_promocion " + "on gasolineras.id = " +
            "gasolinera_promocion.gasolineraID where gasolinera_promocion.promocionID " +
            "= :promID")
    List<Gasolinera> buscaGasolinerasRelacionadasConPromocion(String promID);
}
