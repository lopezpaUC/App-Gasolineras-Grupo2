package es.unican.is.appgasolineras.activities.main;

/**
 * Enumerate with the price order types
 */
public enum PriceFilterType {
    DIESEL, GASOLINA, SUMARIO;

    public static PriceFilterType getPriceFilterType(int type) {
        PriceFilterType typeEnum;
        switch (type) {
            case 1:
                typeEnum = DIESEL;
                break;
            case 2:
                typeEnum = GASOLINA;
                break;
            default:
                typeEnum = SUMARIO;
                break;
        }

        return typeEnum;
    }

    /**
     * Permite la conversion de un string a un tipo de combustible.
     *
     * @param type String que representa un tipo de combustible.
     * @return Tipo de combustible.
     */
    public static PriceFilterType getCombTypeFromString(String type) {
        PriceFilterType typeEnum;
        switch (type) {
            case "Diésel":
                typeEnum = DIESEL;
                break;
            case "Gasolina":
                typeEnum = GASOLINA;
                break;
            default:
                typeEnum = SUMARIO;
                break;
        }

        return typeEnum;
    }
}
