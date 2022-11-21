package es.unican.is.appgasolineras.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Clase que representa una marca registrada para promociones.
 */
@Entity(tableName = "marcas")
public class Marca {

    @NonNull
    @PrimaryKey
    private String nombre;

    @ColumnInfo(name = "esLowcost")
    private boolean esLowcost;

    /**
     * Crea una marca, cuyo nombre es su identificador.
     *
     * @param nombre Nombre de la marca.
     * @param esLowcost Booleano que indica si la marca es lowcost.
     */
    public Marca(@NonNull String nombre, boolean esLowcost) {
        this.nombre = nombre;
        this.esLowcost = esLowcost;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setId(@NonNull String nombre) {
        this.nombre = nombre;
    }

    public boolean getEsLowcost() {
        return esLowcost;
    }

    public void setEsLowcost(boolean lowcost) {
        this.esLowcost = lowcost;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o.getClass() != this.getClass()) {
            return false;
        } else {
            return ((Marca) o).nombre.equals(this.nombre);
        }
    }

    @Override
    public int hashCode() {
        return this.nombre.hashCode();
    }
}
