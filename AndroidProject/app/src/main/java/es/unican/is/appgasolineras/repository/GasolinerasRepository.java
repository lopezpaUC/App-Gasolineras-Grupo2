package es.unican.is.appgasolineras.repository;

import android.content.Context;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

import es.unican.is.appgasolineras.common.Callback;
import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.GasolinerasResponse;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.db.GasolineraDao;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
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
    private static final double ERROR_CONVERSION = -1.0; // Error convirtiendo String a double

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

    public List<Gasolinera> getGasolinerasRelacionadasConPromocion(String promID) {
        GasolineraDatabase db = GasolineraDatabase.getDB(context);
        GasolineraDao gasolinerasDao = db.gasolineraDao();
        return gasolinerasDao.buscaGasolinerasRelacionadasConPromocion(promID);
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
        double bestPrice = Double.POSITIVE_INFINITY;

        // Loops all promotions
        for (Promocion promotion : promotions) {
            // Checks for type of fuel assigned to the promotion
            if (promotion.getCombustibles().contains(fuel) ||
                    fuel.contains(promotion.getCombustibles())) {
                // Calculates the price for the promotion
                double discountedPrice = calculateDiscountedPrice(price, promotion);

                // Updates the best price and the best promotion
                if (discountedPrice < bestPrice) {
                    bestPrice = discountedPrice;
                    bestPromotion = promotion;
                }
            }
        }
        return bestPromotion;
    }

    @Override
    public double calculateDiscountedPrice(double price, Promocion promotion) {
        if (promotion == null) {
            return price;
        }

        double euros = promotion.getDescuentoEurosLitro();
        if (euros > 0) {
            return price - euros;
        } else {
            return price * (100 - promotion.getDescuentoPorcentual()) / 100;
        }
    }

    public String precioSumarioToStr(double precio) {
        String precioTxt = "-";

        if (precio > 0.0) { // Si el precio es valido
            precioTxt = String.format(Locale.FRANCE, "%.2f", precio);
        }

        return precioTxt;
    }

    /**
     * Comprueba que la cadena de texto relativa a un precio de la gasolinera contiene texto
     * a poder mostrar de forma valida.
     *
     * @param texto Texto a comprobar.
     * @return Mismo texto si la comprobacion ha sido satisfactoria.
     *         Guion en caso de que el texto no pase la comprobacion.
     */
    @Override
    public String checkValidPrice(String texto) {
        String correccion = texto;

        if (texto.contains("-") || texto.equals("")) { // Si es negativo o no contiene informacion
            correccion = "-";
        } else { // Prepara el string para que solo muestre dos decimales
            StringBuilder sb = new StringBuilder(correccion);
            sb.deleteCharAt(sb.length()-1);
            correccion = sb.toString();
        }

        return correccion;
    }

    @Override
    public double calculateSummary(double dieselPrice, double unleaded95Price) {
        double summary;
        // Determines the summary price based on the validity of both prices
        if (dieselPrice <= 0.0) { // Invalid diesel price
            summary = unleaded95Price;
        } else if(unleaded95Price <= 0.0) { // Invalid 95-octanes price
            summary = dieselPrice;
        } else { // Both prices are valid
            summary = (dieselPrice + unleaded95Price * 2.0) / 3.0;
        }
        return summary;
    }

    /**
     * Produce un valor double valido para un precio de combustible indicado como cadena de texto.
     * @param precio Precio de combustible como cadena de texto.
     * @param formato Formato a aplicar en la conversion.
     * @return Precio de combustible como valor valido convertido de tipo double.
     */
    @Override
    public double precioToDouble(String precio, NumberFormat formato) {
        double precioDouble;

        try {
            Number number = formato.parse(precio);

            // Comprueba si se puede obtener el valor en formato double
            if (number != null) {
                precioDouble = number.doubleValue();
            } else {
                precioDouble = ERROR_CONVERSION;
            }

        } catch (ParseException e) { // Si hay un error en la conversion
            precioDouble = ERROR_CONVERSION;
        }

        return precioDouble;
    }

    /**
     * Comprueba que la cadena de texto relativa a información de la gasolinera contiene texto
     * a poder mostrar de forma valida.
     *
     * @param texto Texto a comprobar.
     * @return Mismo texto si la comprobacion ha sido satisfactoria.
     *         Guion en caso de que el texto no pase la comprobacion.
     */
    @Override
    public String checkValid(String texto) {
        String correccion = texto;

        if (texto.equals("")) { // Si no se contiene ninguna informacion
            correccion = "-";
        }

        return correccion;
    }
}
