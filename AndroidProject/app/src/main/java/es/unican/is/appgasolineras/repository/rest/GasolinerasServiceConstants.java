package es.unican.is.appgasolineras.repository.rest;

public class GasolinerasServiceConstants {

    private static final String MINECO_API_URL =
            "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/";

    private static final String STATIC_API_URL =
            "https://raw.githubusercontent.com/isunican/App-Gasolineras-Grupo2/master/StaticREST/ServiciosRESTCarburantes/PrecioCarburantes/";

    private static String API_URL = MINECO_API_URL;

    public static void setStaticURL() {
        API_URL = STATIC_API_URL;
    }

    public static void setMinecoURL() {
        API_URL = MINECO_API_URL;
    }

    public static final String getAPIURL() {
        return API_URL;
    }
}
