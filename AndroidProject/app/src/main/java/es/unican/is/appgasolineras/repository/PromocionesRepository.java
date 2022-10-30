package es.unican.is.appgasolineras.repository;

import android.content.Context;

import java.util.List;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Marca;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.db.MarcaDao;
import es.unican.is.appgasolineras.repository.db.PromocionDao;

public class PromocionesRepository implements IPromocionesRepository {

    private final Context context;

    public PromocionesRepository(final Context context) {
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

    @Override
    public Promocion getPromocionById(String id) {
        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        PromocionDao promocionesDao = db.promocionDao();
        return promocionesDao.getPromocionById(id);
    }

    @Override
    public void insertPromociones (List<Promocion> data) {
        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        PromocionDao promocionesDao = db.promocionDao();

        Promocion[] promociones = data.toArray(new Promocion[data.size()]);
        promocionesDao.insertAll(promociones);
    }

    @Override
    public void insertPromocion (Promocion data) {
        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        PromocionDao promocionesDao = db.promocionDao();
        promocionesDao.insert(data);
    }

    @Override
    public void insertMarcas (List<Marca> data) {
        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        MarcaDao marcasDao = db.marcaDao();

        Marca[] marcas = data.toArray(new Marca[data.size()]);
        marcasDao.insertAll(marcas);
    }

    @Override
    public void insertMarca (Marca data) {
        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        MarcaDao marcasDao = db.marcaDao();
        marcasDao.insert(data);
    }

    @Override
    public List<Promocion> getPromocionesRelacionadasConMarca(String idMarca) {
        List<Promocion> promociones;

        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        PromocionDao promocionesDao = db.promocionDao();
        promociones = promocionesDao.buscaPromocionesRelacionadasConMarca(idMarca);
        return promociones;
    }

    @Override
    public List<Marca> getMarcas() {
        List<Marca> marcas;

        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        MarcaDao marcasDao = db.marcaDao();
        marcas = marcasDao.getMarcas();
        return marcas;
    }

    @Override
    public void insertRelacionGasolineraPromocion(Gasolinera g, Promocion p) {
        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        PromocionDao promocionesDao = db.promocionDao();
        promocionesDao.insertRelationGasolineraPromocion(g.getId(), p.getId());
    }

    @Override
    public void insertRelacionMarcaPromocion(Marca m, Promocion p) {
        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        MarcaDao marcasDao = db.marcaDao();
        marcasDao.insertRelationMarcaPromocion(m.getNombre(), p.getId());
    }
}
