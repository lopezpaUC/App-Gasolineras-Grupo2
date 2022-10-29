package es.unican.is.appgasolineras.repository;

import android.content.Context;

import java.time.Instant;
import java.util.List;

import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.GasolinerasResponse;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.db.GasolineraDao;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.db.PromocionDao;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;

public class PromocionRepository implements IPromocionRepository {

    private final Context context;

    public PromocionRepository(final Context context) {
        this.context = context;
    }

    @Override
    public List<Promocion> getPromocionesRelacionadasConGasolinera(String idGasolinera) {
        List<Promocion> promociones;

        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        PromocionDao promocionesDao = db.promocionDao();
        promociones = promocionesDao.buscaPromocionesRelacionadasConGasolinera(idGasolinera);
        return promociones;
    }

    @Override
    public List<Promocion> getPromociones() {
        List<Promocion> promociones;

        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        PromocionDao promocionesDao = db.promocionDao();
        promociones = promocionesDao.getPromociones();
        return promociones;
    }

    public void insertPromociones (List<Promocion> data) {
        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        PromocionDao promocionesDao = db.promocionDao();

        Promocion[] promociones = data.toArray(new Promocion[data.size()]);
        promocionesDao.insertAll(promociones);
    }

    public void insertRelacionGasolineraPromocion(Gasolinera g, Promocion p) {

    }
}
