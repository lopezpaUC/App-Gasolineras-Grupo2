package es.unican.is.appgasolineras.repository.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import es.unican.is.appgasolineras.model.Marca;
import es.unican.is.appgasolineras.model.MarcaPromocionCrossRef;

@Dao
public interface MarcaDao {

    /**
     * Obtiene las marcas registradas en la base de datos.
     *
     * @return lista de marcas registradas.
     */
    @Query("SELECT * FROM marcas")
    List<Marca> getMarcas();

    /**
     * Obtiene una marca en base a su ID.
     *
     * @param id ID de la marca
     *
     * @return marca con el ID indicado, o null si no esta registrada.
     */
    @Query("SELECT * FROM marcas where nombre = :id")
    Marca getMarcaById(String id);

    /**
     * Obtiene las marcas lowcost de entre aquellas registradas en la base de datos.
     *
     * @return lista de marcas lowcost registradas.
     */
    @Query("SELECT * FROM marcas where esLowcost = 1") // 1 = True
    List<Marca> getMarcasLowcost();

    /**
     * Inserta varias marcas en la base de datos.
     *
     * @param nombre Colección de marcas a insertar.
     */
    @Query("SELECT * FROM marcas where nombre = :nombre")
    Marca getMarca(String nombre);

    @Insert
    void insertAll(Marca... marcas);

    /**
     * Inserta una marca en la base de datos.
     *
     * @param marca Mara a insertar en la base de datos.
     */
    @Insert
    void insert(Marca marca);

    /**
     * Borra todas las marcas de la tabla de marcas.
     */
    @Query("DELETE FROM marcas")
    void deleteAll();

    @Query("UPDATE marcas SET esLowcost = 1 where nombre = :nombre")
    void updateMarcaToLowcost(String nombre);

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
