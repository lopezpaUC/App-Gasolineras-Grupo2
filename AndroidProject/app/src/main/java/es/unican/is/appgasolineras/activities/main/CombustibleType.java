package es.unican.is.appgasolineras.activities.main;

/**
 * Enumerado con los diferentes tipos de combustibles.
 */
public enum CombustibleType {
    ALL_COMB, DIESEL, GASOLINA;

    public static CombustibleType getCombTypeFromInt(int type) {
        CombustibleType typeEnum;
        switch (type) {
            case 1:
                typeEnum = DIESEL;
                break;
            case 2:
                typeEnum = GASOLINA;
                break;
            default:
                typeEnum = ALL_COMB;
                break;

        }
        return typeEnum;
    }
}
