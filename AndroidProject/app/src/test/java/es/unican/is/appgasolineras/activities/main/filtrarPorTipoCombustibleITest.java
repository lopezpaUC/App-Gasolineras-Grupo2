package es.unican.is.appgasolineras.activities.main;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import static es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants.setStaticURL2;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.List;

import es.unican.is.appgasolineras.common.Callback;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(MockitoJUnitRunner.class)
public class filtrarPorTipoCombustibleITest {


    @BeforeClass
    public static void setUp() {
        setStaticURL2();
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Mock
    IMainContract.View mockMainView;

    private MainPresenter sut;
    IGasolinerasRepository iGR = null;


    @Before
    public void inicializa() {

        IGasolinerasRepository iGR = new IGasolinerasRepository();
        MockitoAnnotations.openMocks(this);
        sut = new MainPresenter(mockMainView);
        when(mockMainView.getGasolineraRepository()).thenReturn(iGR);

    }

    @Test
    public void testFilterByCombustible() {
        sut.init();
        sut.filterByCombustible(CombustibleType.DIESEL);
        /*assertEquals("111", sut.getShownGasolineras().get(0).getId());
        assertEquals("333", sut.getShownGasolineras().get(1).getId());*/
        assertTrue(6 == sut.getShownGasolineras().size());





    }
}
