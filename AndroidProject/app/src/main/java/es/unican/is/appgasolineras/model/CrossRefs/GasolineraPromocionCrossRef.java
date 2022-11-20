package es.unican.is.appgasolineras.model.CrossRefs;

import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * Clase que representa una relacion entre una promocion y una gasolinera. Necesaria para modelar
 * una entidad asociativa.
 */
@Entity(primaryKeys = {"gasolineraID", "promocionID"},
        tableName = "gasolinera_promocion")
public class GasolineraPromocionCrossRef {

    @NonNull
    private String gasolineraID;
    @NonNull
    private String promocionID;

    /**
     * Construye una relacion entre una gasolinera y una promocion.
     *
     * @param gasolineraID ID Gasolinera
     * @param promocionID ID Promocion
     */
    public GasolineraPromocionCrossRef (String gasolineraID, String promocionID) {
        this.gasolineraID = gasolineraID;
        this.promocionID = promocionID;
    }

    public String getGasolineraID() {
        return gasolineraID;
    }

    public void setGasolineraID(String id) {
        gasolineraID = id;
    }

    public String getPromocionID() {
        return promocionID;
    }

    public void setPromocionID(String id) {
        promocionID = id;
    }
}
