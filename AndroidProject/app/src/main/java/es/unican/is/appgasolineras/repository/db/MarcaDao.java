package es.unican.is.appgasolineras.repository.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import es.unican.is.appgasolineras.model.Marca;
import es.unican.is.appgasolineras.model.MarcaPromocionCrossRef;

@Dao
public interface MarcaDao {

    @Query("SELECT * FROM marcas")
    List<Marca> getMarcas();

    @Query("SELECT * FROM marcas where nombre = :nombre")
    Marca getMarca(String nombre);

    @Insert
    void insertAll(Marca... marcas);

    @Insert
    void insert(Marca marca);

    @Query("DELETE FROM marcas")
    void deleteAll();

    /**
     * Inserta una relacion entre marca y promocion.
     * @param marcaID ID Gasolinera
     * @param promID ID Promocion
     */
    @Query("INSERT INTO promocion_marca values(:promID, :marcaID)")
    void insertRelationMarcaPromocion(String marcaID, String promID);

    /**
     * Elimina una relacion entre marca y promocion.
     * @param marcaID ID Marca
     * @param promID ID Promocion
     */
    @Query("DELETE FROM promocion_marca where marcaID = :marcaID and promocionID = :promID")
    void deleteRelationMarcaPromocion(String marcaID, String promID);

    /**
     * Devuelve las parejas Marca-Promocion correspondientes a una promocion.
     * @param promID Promocion de la que interesa obtener las gasolineras a las que afecta.
     * @return lista de parejas IDGasolinera-IDPromocion, para la promocion indicada.
     */
    @Query("SELECT * FROM promocion_marca where promocionID = :promID")
    List<MarcaPromocionCrossRef> findMarcasRelatedByID(String promID);
}
