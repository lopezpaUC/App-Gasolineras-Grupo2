package es.unican.is.appgasolineras.repository;

import java.util.List;

import es.unican.is.appgasolineras.model.Promocion;

public interface IPromocionRepository {
    List<Promocion> getPromocionesRelacionadasConGasolinera(String idGasolinera);

    public List<Promocion> getPromociones();
}
