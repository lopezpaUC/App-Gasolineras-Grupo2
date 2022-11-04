package es.unican.is.appgasolineras.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Clase que representa una promocion registrada para gasolineras, registrada por un usuario.
 */
@Entity(tableName = "promociones")
public class Promocion {

    @NonNull @PrimaryKey
    private String id;

    @ColumnInfo(name = "descuento_porcentual")
    private double descuentoPorcentual; // 0.0 - 100.0 %

    @ColumnInfo(name = "decuento_euros_litro")
    private double descuentoEurosLitro; // > 0.0

    @ColumnInfo(name = "combustible")
    private String combustibles;

    /**
     * Crea una promocion por defecto o vacia.
     */
    public Promocion() {
        id = "";
        descuentoPorcentual = -1.0;
        descuentoEurosLitro = -1.0;
        combustibles = null;
    }

    /**
     * Crea una promocion detallada. Solo uno de los dos descuentos debe contener un valor valido.
     *
     * @param id Nombre de la promocion, unico y que la identifica.
     * @param descuentoPorcentual Descuento porcentual.
     * @param descuentoEurosLitro Descuento en euros litro.
     * @param combustibles Combustible.
     */
    public Promocion(String id, double descuentoPorcentual, double descuentoEurosLitro,
                     String combustibles) {
        this.id = id;
        this.descuentoPorcentual = descuentoPorcentual;
        this.descuentoEurosLitro = descuentoEurosLitro;
        this.combustibles = combustibles;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public double getDescuentoPorcentual() {
        return descuentoPorcentual;
    }

    public void setDescuentoPorcentual(double descuentoPorcentual) {
        this.descuentoPorcentual = descuentoPorcentual;
    }

    public double getDescuentoEurosLitro() {
        return descuentoEurosLitro;
    }

    public void setDescuentoEurosLitro(double descuentoEurosLitro) {
        this.descuentoEurosLitro = descuentoEurosLitro;
    }

    public String getCombustibles() {
        return combustibles;
    }

    public void setCombustibles(String combustibles) {
        this.combustibles = combustibles;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o.getClass() != this.getClass()) {
            return false;
        } else {
            return((Promocion) o).id.equals(this.id);
        }
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
