package es.unican.is.appgasolineras.common.utils.EnumTypes;

public enum FilterType {
    FUEL_BRAND, PRICE;

    public static FilterType getPriceOrder(int value) {
        FilterType filterType;
        if(value == 0) {
            filterType = FUEL_BRAND;
        } else {
            filterType = PRICE;
        }
        return filterType;
    }

    public static FilterType getPriceOrderFromString(String order) {
        FilterType filterType;

        if(order.equals("COMBUSTIBLES-MARCAS")) {
            filterType = FUEL_BRAND;
        } else {
            filterType = PRICE;
        }
        return filterType;
    }
}
