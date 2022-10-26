package es.unican.is.appgasolineras.model;

import java.util.LinkedList;
import java.util.List;

import es.unican.is.appgasolineras.activities.main.CombustibleType;

public class Promocion {

    private LinkedList<CombustibleType> combustibles;
    private String nombre;
    private boolean EsPorcentaje;
    private double valor;
    private LinkedList<Gasolinera> gasolineras;

    public LinkedList<CombustibleType> getCombustibles() {
        return combustibles;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isEsPorcentaje() {
        return EsPorcentaje;
    }

    public double getValor() {
        return valor;
    }

    public LinkedList<Gasolinera> getListaGasolineras() {
        return gasolineras;
    }

    public Promocion(LinkedList<CombustibleType> combustibles, String nombre, boolean esPorcentaje, double valor, LinkedList<Gasolinera> gasolineras) {
        this.combustibles = combustibles;
        this.nombre = nombre;
        EsPorcentaje = esPorcentaje;
        this.valor = valor;
        this.gasolineras = gasolineras;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o.getClass() != this.getClass()) {
            return false;
        } else {
            return((Promocion) o).nombre.equals(this.nombre);
        }
    }
}
