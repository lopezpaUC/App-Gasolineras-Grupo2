package es.unican.is.appgasolineras.repository.rest;

public class GasolinerasServiceConstants {

    private GasolinerasServiceConstants() {}

    private static final String MINECO_API_URL =
            "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/";

    private static final String STATIC_API_URL =
            "https://raw.githubusercontent.com/isunican/App-Gasolineras-Grupo2/master/StaticREST/ServiciosRESTCarburantes/PrecioCarburantes/";

    private static final String STATIC_API_URL2 =
            "https://raw.githubusercontent.com/isunican/App-Gasolineras-Grupo2/feature/454094-FiltrarPorTipoDeCombustible/StaticREST/ServiciosRESTCarburantes/PrecioCarburantes2/";

    private static final String STATIC_API_URL3 =
            "https://raw.githubusercontent.com/isunican/App-Gasolineras-Grupo2/feature/454095-AnhadirPromociones/StaticREST/ServiciosRESTCarburantes/PrecioCarburantes_SP02_Anhadir/";

    private static final String STATIC_API_URL_VIEW_PROMOTION_IN_GAS_STATION =
            "https://raw.githubusercontent.com/isunican/App-Gasolineras-Grupo2/feature/464976-MostrarPromocionEnGasolinera/StaticREST/ServiciosRESTCarburantes/PrecioCarburantes_SP02_VerPromocionGasolinera/";

    private static final String STATIC_API_URL_FILTER_BY_LOWCOST =
            "https://raw.githubusercontent.com/isunican/App-Gasolineras-Grupo2/feature/466920-FiltrarPorLowcost/StaticREST/ServiciosRESTCarburantes/PrecioCarburantes_SP03_FiltrarPorLowcost/";


    private static String apiURL = MINECO_API_URL;

    public static void setStaticURL() {
        apiURL = STATIC_API_URL;
    }

    public static void setStaticURL2() {
        apiURL = STATIC_API_URL2;
    }

    public static void setStaticURL3() {apiURL = STATIC_API_URL3;}

    public static void setStaticURLMostrarPromocionEnGasolinera() {
        apiURL = STATIC_API_URL_VIEW_PROMOTION_IN_GAS_STATION;
    }

    public static void setStaticURLFiltrarPorLowcost() {
        apiURL = STATIC_API_URL_FILTER_BY_LOWCOST;
    }

    public static void setMinecoURL() {
        apiURL = MINECO_API_URL;
    }

    public static final String getAPIURL() {
        return apiURL;
    }
}
