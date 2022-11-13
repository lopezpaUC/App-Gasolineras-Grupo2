package es.unican.is.appgasolineras.activities.main;

/**
 * Enumerado con los diferentes tipos de combustibles.
 */
public enum CombustibleType {
    ALL_COMB, DIESEL, GASOLINA;

    /**
     * Permite la conversion de un entero a un tipo de combustible.
     *
     * @param type Entero que representa un tipo de combustible.
     * @return Tipo de combustible.
     */
    public static CombustibleType getCombTypeFromInt(int type) {
        CombustibleType typeEnum;
        switch (type) {
            case 1:
                typeEnum = DIESEL;
                break;
            case 2:
                typeEnum = GASOLINA;
                break;
            default: // Cualquier otro valor, representa todos los combustibles en conjunto
                typeEnum = ALL_COMB;
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
    public static CombustibleType getCombTypeFromString(String type) {
        CombustibleType typeEnum;
        switch (type) {
            case "Diésel":
                typeEnum = DIESEL;
                break;
            case "Gasolina":
                typeEnum = GASOLINA;
                break;
            default: // Cualquier otro valor, representa todos los combustibles en conjunto
                typeEnum = ALL_COMB;
                break;
        }

        return typeEnum;
    }

    public String getCombStringFromInt(int value) {
        String comb;

        switch (value) {
            case 0:
                comb = "Diésel";
                break;
            case 1:
                comb = "Gasolina";
                break;
            default:
                comb = "Todos";
        }
        return comb;
    }
}
