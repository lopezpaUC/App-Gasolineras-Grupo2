package es.unican.is.appgasolineras.activities.main;

/**
 * Enumerado con los diferentes tipos de combustibles.
 */
public enum PriceOrderType {
    ASC, DESC;

    public static PriceOrderType getPriceOrder(int value) {
        PriceOrderType priceOrder;
        if(value == 0) {
            priceOrder = ASC;
        }else {
            priceOrder = DESC;
        }
        return priceOrder;
    }

    public static PriceOrderType getPriceOrderFromString(String order) {
        PriceOrderType priceOrder;

        if(order.equals("ASC")) {
            priceOrder = ASC;
        } else {
            priceOrder = DESC;
        }

        return priceOrder;
    }
}
