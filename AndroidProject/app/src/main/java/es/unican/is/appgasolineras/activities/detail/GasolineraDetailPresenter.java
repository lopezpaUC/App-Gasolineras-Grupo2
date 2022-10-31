package es.unican.is.appgasolineras.activities.detail;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;
import es.unican.is.appgasolineras.repository.PromocionesRepository;

/**
 * Presenter para la actividad relacionada con la muestra de información detallada de una
 * gasolinera.
 *
 * @author Grupo 02-CarbuRed
 * @version 1.0
 */
public class GasolineraDetailPresenter implements IGasolineraDetailContract.Presenter {

    private static final double ERROR_CONVERSION = -1.0; // Error convirtiendo String a double
    private final IGasolineraDetailContract.View view;   // Vista encargada de mostrar informacion
    private final Gasolinera gasolinera;                 // Gasolinera a mostrar
    private String precioSumarioStr;                     // Precio sumario de la gasolinera
    private String discountedSummaryPriceStr;
    private String discountedDieselPriceStr;
    private String discounted95OctanesPriceStr;
    private IPromocionesRepository repPromotions;          // Promotions repository

    /**
     * Constructor del presenter.
     *
     * @param view Vista con la que comunicarse.
     * @param gasolinera Gasolinera cuya informacion debe tratarse.
     */
    public GasolineraDetailPresenter(IGasolineraDetailContract.View view, Gasolinera gasolinera) {
        this.view = view;
        this.gasolinera = gasolinera;

        repPromotions = new PromocionesRepository(view.getContext());
    }

    @Override
    public void init() {
        // Obtiene el precio sumario
        precioSumarioStr = precioSumarioToStr(calculateSummaryPrice());
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
        return precioSumarioToStr(calculateDiscountedSummaryPrice());
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
        precioDiesel = precioToDouble(dieselPriceStr, formato);

        // Convierte a double el precio de la gasolina 95
        precioGasolina = precioToDouble(precioGasolinaStr, formato);

        return calculateSummary(precioDiesel, precioGasolina);
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
            info.put("label", checkValid(gasolinera.getRotulo()));
            info.put("municipality", checkValid(gasolinera.getMunicipio()));
            info.put("direction", checkValid(gasolinera.getDireccion()));
            info.put("cp", checkValid(gasolinera.getCp()));
            info.put("price95", checkValidPrice(gasolinera.getNormal95()) + " €/L");
            info.put("priceDieselA", checkValidPrice(gasolinera.getDieselA()) + " €/L");
            info.put("schedule", checkValid(gasolinera.getHorario()));

            view.showInfo(info);
        }
    }

    /**
     * Produce un valor double valido para un precio de combustible indicado como cadena de texto.
     * @param precio Precio de combustible como cadena de texto.
     * @param formato Formato a aplicar en la conversion.
     * @return Precio de combustible como valor valido convertido de tipo double.
     */
    private double precioToDouble(String precio, NumberFormat formato) {
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
     * Produce el String correspondiente para el precio de sumario de la gasolinera.
     *
     * @param precio Precio de sumario, tipo double.
     * @return Precio de sumario como cadena de texto. "-" en caso de haber ocurrido algún error.
     */
    private String precioSumarioToStr(double precio) {
        String precioTxt = "-";

        if (precio > 0.0) { // Si el precio es valido
            precioTxt = String.format(Locale.FRANCE, "%.2f", precio);
        }

        return precioTxt;
    }

    /**
     * Comprueba que la cadena de texto relativa a información de la gasolinera contiene texto
     * a poder mostrar de forma valida.
     *
     * @param texto Texto a comprobar.
     * @return Mismo texto si la comprobacion ha sido satisfactoria.
     *         Guion en caso de que el texto no pase la comprobacion.
     */
    private String checkValid(String texto) {
        String correccion = texto;

        if (texto.equals("")) { // Si no se contiene ninguna informacion
            correccion = "-";
        }

        return correccion;
    }

    /**
     * Comprueba que la cadena de texto relativa a un precio de la gasolinera contiene texto
     * a poder mostrar de forma valida.
     *
     * @param texto Texto a comprobar.
     * @return Mismo texto si la comprobacion ha sido satisfactoria.
     *         Guion en caso de que el texto no pase la comprobacion.
     */
    private String checkValidPrice(String texto) {
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

    public double calculateDiscountedPrice(double price, Promocion promotion) {
        if (promotion == null) {
            return price;
        }

        double euros = promotion.getDescuentoEurosLitro();
        if (euros > 0) {
            return price - euros;
        } else {
            return price * (100 - promotion.getDescuentoPorcentual());
        }
    }


    private double calculateDiscountedSummaryPrice() {
        // Obtains the list of promotions assigned to the gas station
        List<Promocion> promotions = repPromotions.getPromocionesRelacionadasConGasolinera
                (gasolinera.getId());

        double discountedSummary;

        // Obtains fuel-types prices as text
        String dieselPriceStr = gasolinera.getDieselA();
        String precioGasolinaStr = gasolinera.getNormal95();

        // Prices in numerical format
        double dieselPrice;
        double precioGasolina;

        // Price format with decimal comma
        NumberFormat formato = NumberFormat.getInstance(Locale.FRANCE);

        // Converts diesel price to double
        dieselPrice = precioToDouble(dieselPriceStr, formato);

        // Converts 95-octanes price to double
        precioGasolina = precioToDouble(precioGasolinaStr, formato);

        // If the gas station has no promotions assigned, all prices remain unaltered
        if (promotions.isEmpty()) {
            discountedDieselPriceStr = precioSumarioToStr(truncateFuelPrice(dieselPrice));
            discounted95OctanesPriceStr = precioSumarioToStr(truncateFuelPrice(precioGasolina));
            return calculateSummaryPrice();
        }

        // Gets the best promotion for both diesel and unleaded 95 octanes
        Promocion bestPromotionDiesel = bestPromotion(dieselPrice, promotions, "DIESEL");
        Promocion bestPromotion95Octanes = bestPromotion(precioGasolina, promotions, "GASOLINA");

        // Calculates the best price for both diesel and 95 octanes
        double dieselDiscountedPrice = truncateFuelPrice(calculateDiscountedPrice(dieselPrice,
                bestPromotionDiesel));
        discountedDieselPriceStr = precioSumarioToStr(dieselDiscountedPrice);

        double unleaded95DiscountedPrice = truncateFuelPrice(calculateDiscountedPrice(precioGasolina,
                bestPromotion95Octanes));
        discounted95OctanesPriceStr = precioSumarioToStr(unleaded95DiscountedPrice);

        // Calculates the summary price according to the validity of the fuel's prices
        discountedSummary = calculateSummary(dieselDiscountedPrice, unleaded95DiscountedPrice);
        discountedSummaryPriceStr = precioSumarioToStr(discountedSummary);

        return discountedSummary;
    }

    /**
     * Obtains the best promotion from a list of promotions, according to the given price
     * @param price the base price
     * @param promotions a list of promotions
     * @return the best promotion
     */
    private Promocion bestPromotion(double price, List<Promocion> promotions, String fuel) {
        Promocion bestPromotion = null;
        double bestPrice = Double.POSITIVE_INFINITY;

        // Loops all promotions
        for (Promocion promotion : promotions) {
            // Checks for type of fuel assigned to the promotion
            if (promotion.getCombustibles().contains(fuel) || fuel.contains(promotion.getCombustibles())) {
                // Calculates the lowest price for the promotion
                double discountedPrice = calculateDiscountedPrice(price, bestPromotion);

                // Updates the best price and the best promotion
                if (discountedPrice < bestPrice) {
                    bestPrice = discountedPrice;
                    bestPromotion = promotion;
                }
            }
        }
        return bestPromotion;
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

    private double calculateSummary(double dieselPrice, double unleaded95) {
        double summary;
        // Determina el precio de sumario en base a la validez de los precios del combustible
        if (dieselPrice <= 0.0) { // Si no hay un precio de diesel valido
            summary = unleaded95;
        } else if(unleaded95 <= 0.0) { // Si no hay un precio de gasolina valido
            summary = dieselPrice;
        } else { // Si todos los precios son validos
            summary = (dieselPrice + unleaded95 * 2.0) / 3.0;
        }
        return summary;
    }
}
