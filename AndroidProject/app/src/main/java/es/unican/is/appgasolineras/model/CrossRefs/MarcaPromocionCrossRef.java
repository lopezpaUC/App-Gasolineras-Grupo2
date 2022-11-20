package es.unican.is.appgasolineras.model.CrossRefs;

import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * Clase que representa una relacion entre una promocion y una marca. Necesaria para modelar
 * una entidad asociativa.
 */
@Entity(primaryKeys = {"promocionID", "marcaID"},
        tableName = "promocion_marca")
public class MarcaPromocionCrossRef {

    @NonNull
    private String promocionID;
    @NonNull
    private String marcaID;

    /**
     * Construye una relacion entre una marca y una promocion.
     *
     * @param promocionID ID Promocion
     * @param marcaID ID Marca
     */
    public MarcaPromocionCrossRef (String promocionID, String marcaID) {
        this.promocionID = promocionID;
        this.marcaID = marcaID;
    }

    public String getPromocionID() {
        return promocionID;
    }

    public void setPromocionID(String id) {
        promocionID = id;
    }

    public String getMarcaID() {
        return marcaID;
    }

    public void setMarcaID(String id) {
        marcaID = id;
    }
}
