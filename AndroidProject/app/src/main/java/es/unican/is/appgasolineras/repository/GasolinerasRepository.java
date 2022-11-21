package es.unican.is.appgasolineras.repository;

import android.content.Context;

import java.time.Instant;
import java.util.List;
import java.util.Locale;


import java.util.ArrayList;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.common.Callback;
import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.common.utils.PriceUtilities;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.GasolinerasResponse;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.model.Marca;
import es.unican.is.appgasolineras.repository.db.GasolineraDao;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.db.MarcaDao;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;

/**
 * Implementation of a gas stations repository.
 * In this case, the gas stations are retrieved from a REST API.
 * The repository also persists into a local DB the retrieved list of gas stations.
 */
public class GasolinerasRepository implements IGasolinerasRepository {

    private static final String KEY_LAST_SAVED = "KEY_LAST_SAVED";
    private static final int LOAD_OFFLINE = 1;
    private static final int LOAD_ONLINE = 0;

    private final Context context;
    private int loadingMethod;

    public GasolinerasRepository(final Context context) {
        this.context = context;
    }

    @Override
    public void requestGasolineras(Callback<List<Gasolinera>> cb) {
        GasolinerasService.requestGasolineras(new Callback<GasolinerasResponse>() {
            @Override
            public void onSuccess(GasolinerasResponse data) {
                List<Gasolinera> gasolineras = data.getStations();
                persistToDB(gasolineras);
                cb.onSuccess(gasolineras);
            }

            @Override
            public void onFailure() {
                cb.onFailure();
            }
        });
    }

    @Override
    public List<Gasolinera> getGasolineras() {
        GasolinerasResponse response = GasolinerasService.getGasolineras();
        List<Gasolinera> gasolineras;

        if (response != null) {
            gasolineras = response.getStations();
            persistToDB(gasolineras);
            loadingMethod = LOAD_ONLINE;
        } else {
            GasolineraDatabase db = GasolineraDatabase.getDB(context);
            GasolineraDao gasolinerasDao = db.gasolineraDao();
            gasolineras = gasolinerasDao.getAll();
            loadingMethod = LOAD_OFFLINE;
        }

        return gasolineras;
    }

    @Override
    public Gasolinera getGasolineraByNameDirLocalidad(String name, String dir, String municipio) {
        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        GasolineraDao gasolinerasDao = db.gasolineraDao();
        return gasolinerasDao.getByNameDirLocalidad(name, dir, municipio);
    }

    @Override
    public List<Gasolinera> getGasolinerasRelacionadasConPromocion(String promID) {
        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        GasolineraDao gasolinerasDao = db.gasolineraDao();
        return gasolinerasDao.buscaGasolinerasRelacionadasConPromocion(promID);
    }

    @Override
    public List<Marca> getMarcasLowcost() {
        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        MarcaDao marcasDao = db.marcaDao();

        List<Marca> marcasLowcost = marcasDao.getMarcasLowcost();
        if (marcasLowcost.isEmpty()) {
            insertLowcost();
            marcasLowcost = marcasDao.getMarcasLowcost();
        }

        return marcasLowcost;
    }

    public List<Gasolinera> getGasolinerasLowcost() {
        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        GasolineraDao gasolinerasDao = db.gasolineraDao();

        List<Marca> marcasLowcost = getMarcasLowcost();

        List<Gasolinera> gasolinerasLowcost = new ArrayList<>();
        for (Gasolinera g:gasolinerasDao.getAll()) {
            for (Marca m : marcasLowcost) {
                if (m.getNombre().toUpperCase(Locale.ROOT).equals(g.getRotulo())) {
                    gasolinerasLowcost.add(g);
                }
            }
        }

        return gasolinerasLowcost;
    }

    @Override
    public int getLoadingMethod() {
        return loadingMethod;
    }

    /**
     * Persist Gasolineras to local DB using Room
     * It also saves into the app preferences the time instant in which
     * this save occured. This information can be used to determine the age
     * of this data stored in the local DB
     * @param data
     */
    private void persistToDB(List<Gasolinera> data) {
        if (data != null) {
            final GasolineraDatabase db = GasolineraDatabase.getDB(context);
            final GasolineraDao gasolinerasDao = db.gasolineraDao();
            gasolinerasDao.deleteAll();

            Gasolinera[] gasolineras = data.toArray(new Gasolinera[data.size()]);
            gasolinerasDao.insertAll(gasolineras);

            // save the current time to the app preferences
            Prefs.from(context).putInstant(KEY_LAST_SAVED, Instant.now());
        }
    }

    private void insertLowcost() {
        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        MarcaDao marcasDao = db.marcaDao();

        for (String s:context.getResources().getStringArray(R.array.lowcost_array)) {
            if (marcasDao.getMarcaById(s) != null) {
                marcasDao.updateMarcaToLowcost(s);
            } else {
                marcasDao.insert(new Marca(s, true));
            }
        }
    }

    /**
     * Returns true if the gas stations currently stored in the local DB is older than the specified
     * amount of minutes
     * @param minutes
     * @return true if the data currently stored in the local DB is older than the specified
     * amount of minutes

    private boolean lastDownloadOlderThan(int minutes) {
        Instant lastDownloaded = Prefs.from(context).getInstant(KEY_LAST_SAVED);
        if (lastDownloaded == null) {
            return true;
        } else {
            Instant now = Instant.now();
            long sinceLastDownloaded = ChronoUnit.MINUTES.between(lastDownloaded, now);  // minutes
            return (sinceLastDownloaded > minutes) ? true : false;
        }
    }*/ // No usado por el momento

    @Override
    public Promocion bestPromotion(double price, List<Promocion> promotions, String fuel) {
        Promocion bestPromotion = null;
        PriceUtilities utilities = new PriceUtilities();
        double bestPrice = Double.POSITIVE_INFINITY;

        // Loops all promotions
        for (Promocion promotion : promotions) {
            // Checks for type of fuel assigned to the promotion
            if (promotion.getCombustibles().contains(fuel) ||
                    fuel.contains(promotion.getCombustibles())) {
                // Calculates the price for the promotion
                double discountedPrice = utilities.calculateDiscountedPrice(price, promotion);

                // Updates the best price and the best promotion
                if (discountedPrice < bestPrice) {
                    bestPrice = discountedPrice;
                    bestPromotion = promotion;
                }
            }
        }
        return bestPromotion;
    }

}
