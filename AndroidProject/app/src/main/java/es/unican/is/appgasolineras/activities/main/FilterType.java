package es.unican.is.appgasolineras.activities.main;

public enum FilterType {
    FUEL_BRAND, PRICE;

    public static FilterType getPriceOrder(int value) {
        FilterType filterType;
        switch (value) {
            case 0:
                filterType = FUEL_BRAND;
                break;
            default:
                filterType = PRICE;
                break;
        }
        return filterType;
    }

    public static FilterType getPriceOrderFromString(String order) {
        FilterType filterType;
        switch (order) {
            case "COMBUSTIBLES-MARCAS":
                filterType = FUEL_BRAND;
                break;
            default:
                filterType = PRICE;
                break;
        }
        return filterType;
    }
}
