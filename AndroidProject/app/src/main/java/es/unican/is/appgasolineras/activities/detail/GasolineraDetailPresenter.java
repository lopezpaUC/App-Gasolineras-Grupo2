package es.unican.is.appgasolineras.activities.detail;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.unican.is.appgasolineras.common.utils.PriceUtilities;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

/**
 * Presenter para la actividad relacionada con la muestra de información detallada de una
 * gasolinera.
 *
 * @author Grupo 02-CarbuRed
 * @version 1.0
 */
public class GasolineraDetailPresenter implements IGasolineraDetailContract.Presenter {

    private final IGasolineraDetailContract.View view;   // Vista encargada de mostrar informacion
    private final Gasolinera gasolinera;                 // Gasolinera a mostrar
    private String precioSumarioStr;                     // Precio sumario de la gasolinera
    private String discountedDieselPriceStr;
    private String discounted95OctanesPriceStr;
    private IPromocionesRepository repPromotions;          // Promotions repository
    private IGasolinerasRepository repGasolineras;         // Gas stations repository
    private PriceUtilities utilities;
    /**
     * Constructor del presenter.
     *
     * @param view Vista con la que comunicarse.
     * @param gasolinera Gasolinera cuya informacion debe tratarse.
     */
    public GasolineraDetailPresenter(IGasolineraDetailContract.View view, Gasolinera gasolinera) {
        this.view = view;
        this.gasolinera = gasolinera;

        repPromotions = view.getPromocionesRepository();
        repGasolineras = view.getGasolinerasRepository();
        utilities = new PriceUtilities();
    }

    @Override
    public void init() {
        // Obtiene el precio sumario
        precioSumarioStr = utilities.precioSumarioToStr(calculateSummaryPrice());
        // Solicita a la vista que muestre la informacion requerida
        loadGasolineraDetails();
    }

    @Override
    public void onAcceptClicked() {
            view.openMainView();
    }

    /**
     * Retorna el precio sumario de la gasolinera.
     * @return Precio sumario de la gasolinera como cadena de caracteres.
     */
    @Override
    public String getPrecioSumario() {
        return precioSumarioStr;
    }

    @Override
    public String getDiscountedDieselPriceStr() {
        return discountedDieselPriceStr;
    }

    @Override
    public String getDiscounted95OctanesPriceStr() {
        return discounted95OctanesPriceStr;
    }

    @Override
    public String getDiscountedSummaryPriceStr() {
        return utilities.precioSumarioToStr(calculateDiscountedSummaryPrice());
    }

    /**
     * Calcula el precio sumario de la gasolinera.
     * @return Precio sumario.
     */
    private double calculateSummaryPrice() {
        // Obtiene los precios de los tipos de combustible como cadena de texto
        String dieselPriceStr = gasolinera.getDieselA();
        String precioGasolinaStr = gasolinera.getNormal95();

        // Variables para almacenar los precios en formato numerico en lugar de cadenas de texto
        double precioDiesel;
        double precioGasolina;

        // Define el formato a utilizar para el 'parseo' de los precios
        NumberFormat formato = NumberFormat.getInstance(Locale.FRANCE);

        // Convierte a double el precio del diesel A
        precioDiesel = utilities.precioToDouble(dieselPriceStr, formato);

        // Convierte a double el precio de la gasolina 95
        precioGasolina = utilities.precioToDouble(precioGasolinaStr, formato);

        return utilities.calculateSummary(precioDiesel, precioGasolina);
    }

    /**
     * Calculates the discounted summary price and updates the discounted diesel and 95-octanes
     * prices
     * @return the discounted summary price
     */
    private double calculateDiscountedSummaryPrice() {
        // Obtains the list of promotions assigned to the gas station
        List<Promocion> promotions = repPromotions.getPromocionesRelacionadasConGasolinera
                (gasolinera.getId());
        // Prices in numerical format
        double dieselPrice;
        double precioGasolina;
        double discountedSummary;

        // Obtains fuel-types prices as text
        String dieselPriceStr = gasolinera.getDieselA();
        String precioGasolinaStr = gasolinera.getNormal95();

        // Price format with decimal comma
        NumberFormat formato = NumberFormat.getInstance(Locale.FRANCE);

        // Converts diesel price to double
        dieselPrice = utilities.precioToDouble(dieselPriceStr, formato);

        // Converts 95-octanes price to double
        precioGasolina = utilities.precioToDouble(precioGasolinaStr, formato);

        // If the gas station has no promotions assigned, all prices remain unaltered
        if (promotions.isEmpty()) {
            discountedDieselPriceStr = utilities.precioSumarioToStr(truncateFuelPrice(dieselPrice));
            discounted95OctanesPriceStr = utilities.precioSumarioToStr(truncateFuelPrice(precioGasolina));
            return calculateSummaryPrice();
        }

        // Gets the best promotion for both diesel and unleaded 95 octanes
        Promocion bestPromotionDiesel = repGasolineras.bestPromotion(dieselPrice, promotions, "Diésel");
        Promocion bestPromotion95Octanes = repGasolineras.bestPromotion(precioGasolina, promotions, "Gasolina");

        // Calculates the best price for both diesel and 95 octanes
        double dieselDiscountedPrice = utilities.
                calculateDiscountedPrice(dieselPrice, bestPromotionDiesel);
        discountedDieselPriceStr = utilities.precioSumarioToStr(truncateFuelPrice(dieselDiscountedPrice));

        double unleaded95DiscountedPrice = utilities.
                calculateDiscountedPrice(precioGasolina, bestPromotion95Octanes);
        discounted95OctanesPriceStr = utilities.precioSumarioToStr(truncateFuelPrice(unleaded95DiscountedPrice));

        // Calculates the summary price according to the validity of the fuel's prices
        discountedSummary = utilities.calculateSummary(dieselDiscountedPrice, unleaded95DiscountedPrice);

        return discountedSummary;
    }

    /**
     * Obtiene los datos necesarios a cargar en la vista, y ordena a esta que se muestren.
     */
    private void loadGasolineraDetails() {
        // Si la gasolinera solo tiene su nombre como informacion
        if (gasolinera.toString().equals(gasolinera.getRotulo())) {
            // Ordena a la vista mostrar un mensaje de error
            view.showLoadError();
        } else { // De lo contrario, si contiene mas informacion, la carga y ordena mostrarla
            Map<String, String> info = new HashMap<>();
            info.put("summary", precioSumarioStr + " €/L");
            info.put("label", utilities.checkValid(gasolinera.getRotulo()));
            info.put("municipality", utilities.checkValid(gasolinera.getMunicipio()));
            info.put("direction", utilities.checkValid(gasolinera.getDireccion()));
            info.put("cp", utilities.checkValid(gasolinera.getCp()));
            info.put("price95", utilities.checkValidPrice(gasolinera.getNormal95()) + " €/L");
            info.put("priceDieselA", utilities.checkValidPrice(gasolinera.getDieselA()) + " €/L");
            info.put("schedule", utilities.checkValid(gasolinera.getHorario()));

            view.showInfo(info);
        }
    }

    /**
     * Truncates price to 2 decimal places
     * @param price the price to be truncated
     * @return the truncated price
     */
    private double truncateFuelPrice(double price) {
        price = price * Math.pow(10, 2);
        price = Math.floor(price);
        return price / Math.pow(10, 2);
    }
}
