package es.unican.is.appgasolineras.activities.main;

/**
 * Enumerado con los diferentes tipos de combustibles.
 */
public enum PriceOrderType {
    ASC, DESC;

    public static PriceOrderType getPriceOrder(int value) {
        PriceOrderType priceOrder;
        switch (value) {
            case 0:
                priceOrder = ASC;
                break;
            default:
                priceOrder = DESC;
                break;
        }
        return priceOrder;
    }

    public static PriceOrderType getPriceOrderFromString(String order) {
        PriceOrderType priceOrder;
        switch (order) {
            case "ASC":
                priceOrder = ASC;
                break;
            default:
                priceOrder = DESC;
                break;
        }
        return priceOrder;
    }
}
