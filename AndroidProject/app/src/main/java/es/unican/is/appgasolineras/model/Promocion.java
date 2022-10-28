package es.unican.is.appgasolineras.model;

import java.util.LinkedList;
import java.util.List;

import es.unican.is.appgasolineras.activities.main.CombustibleType;

public class Promocion {

    private CombustibleType combustibleType;
    private String nombre;
    private boolean porMarca;
    private double valor;
    private List<Gasolinera> gasolineras;
    private boolean esPorcentaje;
    private String marca;



    public String getNombre() {
        return nombre;
    }


    public double getValor() {
        return valor;
    }



    public Promocion(String nombre, CombustibleType combustibleType, boolean porMarca, String marca, double valor, boolean esPorcentaje, List<Gasolinera> gasolineras) {
        this.combustibleType = combustibleType;
        this.nombre = nombre;
        this.porMarca = porMarca;
        this.valor = valor;
        this.gasolineras = gasolineras;
        this.esPorcentaje = esPorcentaje;
        this.marca = marca;
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

    public List<Gasolinera> getGasolineras() {
        return gasolineras;
    }

    public boolean isEsPorcentaje() {
        return esPorcentaje;
    }

    public CombustibleType getCombustibleType() {
        return this.combustibleType;
    }
}
