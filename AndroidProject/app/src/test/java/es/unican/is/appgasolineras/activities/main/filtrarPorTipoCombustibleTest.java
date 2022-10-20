package es.unican.is.appgasolineras.activities.main;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)

public class filtrarPorTipoCombustibleTest {

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL2();
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
    }

    private MainPresenter sut;
    private List<String> ls = new ArrayList<>();

    @Mock
    private IMainContract.View mockMainContractView;
    private IGasolinerasRepository r;

    @Before
    public void inicializa() {
        MockitoAnnotations.openMocks(this);
        sut = new MainPresenter(mockMainContractView);
        Context context = ApplicationProvider.getApplicationContext();
        r = ApplicationProvider.getApplicationContext();
        when(mockMainContractView.getGasolineraRepository()).thenReturn(r);
        sut.init();
    }

    @Test
    public void testFilterByCombustible() {

        // Filtrar por diésel
        sut.filter(CombustibleType.DIESEL, ls.subList(0,0));
        assertEquals(6, sut.getShownGasolineras().size());


        // Filtrar por gasolina



        // Filtrar por todos


        // Lista de gasolineras vacía

    }

}
