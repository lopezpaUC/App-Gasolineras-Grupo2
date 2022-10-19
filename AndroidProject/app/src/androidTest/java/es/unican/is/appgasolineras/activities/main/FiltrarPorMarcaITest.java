package es.unican.is.appgasolineras.activities.main;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

public class FiltrarPorMarcaITest {

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL();
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Test
    public void testFiltrarPorMarca() {

    }

}
