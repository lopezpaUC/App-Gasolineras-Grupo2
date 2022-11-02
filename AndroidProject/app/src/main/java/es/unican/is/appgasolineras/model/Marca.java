package es.unican.is.appgasolineras.model;

import androidx.annotation.NonNull;
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

    /**
     * Crea una marca, cuyo nombre es su identificador.
     */
    public Marca(String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setId(@NonNull String nombre) {
        this.nombre = nombre;
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
}
