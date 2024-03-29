package es.unican.is.appgasolineras.activities.main;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
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

import es.unican.is.appgasolineras.common.utils.EnumTypes.CombustibleType;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})

public class FiltrarPorTipoCombustibleITest {

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL2();
    }

    @AfterClass
    public static void clean() {
        GasolinerasService.resetAPI();
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
        r = new GasolinerasRepository(context);
        when(mockMainContractView.getGasolineraRepository()).thenReturn(r);
        sut.init();
    }

    @After
    public void end() {
        GasolineraDatabase db = GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext());
        db.close();
    }

    @Test
    public void testFilterByCombustible() {

        // Filtrar por diésel
        sut.filter(CombustibleType.DIESEL, ls.subList(0,0), false);
        assertEquals(6, sut.getShownGasolineras().size());
        assertEquals("CEPSA", sut.getShownGasolineras().get(0).getRotulo());
        assertEquals("PETRONOR", sut.getShownGasolineras().get(1).getRotulo());
        assertEquals("E.S. CARBURANTES DE ARNUERO S.L.", sut.getShownGasolineras().get(2).getRotulo());
        assertEquals("G2", sut.getShownGasolineras().get(3).getRotulo());
        assertEquals("COBO", sut.getShownGasolineras().get(4).getRotulo());


        // Filtrar por gasolina
        sut.filter(CombustibleType.GASOLINA, ls.subList(0,0), false);
        assertEquals(8, sut.getShownGasolineras().size());
        assertEquals("CEPSA", sut.getShownGasolineras().get(0).getRotulo());
        assertEquals("REPSOL", sut.getShownGasolineras().get(1).getRotulo());
        assertEquals("E.S. CARBURANTES DE ARNUERO S.L.", sut.getShownGasolineras().get(2).getRotulo());
        assertEquals("G2", sut.getShownGasolineras().get(3).getRotulo());
        assertEquals("AREA DE SERVICIO LA PALMERA", sut.getShownGasolineras().get(4).getRotulo());
        assertEquals("COBO", sut.getShownGasolineras().get(5).getRotulo());
        assertEquals("GALP", sut.getShownGasolineras().get(6).getRotulo());



        // Filtrar por todos
        sut.filter(CombustibleType.ALL_COMB, ls.subList(0,0), false);
        assertEquals(10, sut.getShownGasolineras().size());
        assertEquals("CEPSA", sut.getShownGasolineras().get(0).getRotulo());
        assertEquals("REPSOL", sut.getShownGasolineras().get(1).getRotulo());
        assertEquals("PETRONOR", sut.getShownGasolineras().get(2).getRotulo());
        assertEquals("CAMPSA", sut.getShownGasolineras().get(3).getRotulo());
        assertEquals("E.S. CARBURANTES DE ARNUERO S.L.", sut.getShownGasolineras().get(4).getRotulo());
        assertEquals("G2", sut.getShownGasolineras().get(5).getRotulo());
        assertEquals("AREA DE SERVICIO LA PALMERA", sut.getShownGasolineras().get(6).getRotulo());
        assertEquals("COBO", sut.getShownGasolineras().get(7).getRotulo());
        assertEquals("GALP", sut.getShownGasolineras().get(8).getRotulo());

    }

}
