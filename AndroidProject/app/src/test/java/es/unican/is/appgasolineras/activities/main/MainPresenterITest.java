package es.unican.is.appgasolineras.activities.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

@Config(manifest=Config.NONE , sdk = {Build.VERSION_CODES.O_MR1})

@RunWith(RobolectricTestRunner.class)

public class MainPresenterITest {

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURLFiltrarPorLowcost();
    }

    @AfterClass
    public static void clean() {
        GasolinerasService.resetAPI();
        GasolinerasServiceConstants.setMinecoURL();
    }

    @After
    public void cleanDatabase() {
        GasolineraDatabase db = GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext());
        db.close();
    }

    private List<String> brandsList1Element = new ArrayList<>();
    private List<String> brandsList2Element = new ArrayList<>();
    private List<String> emptyBrandList = new ArrayList<>();
    private List<String> wrongBrandList = new ArrayList<>();

    private MainPresenter sut;

    @Mock
    private IMainContract.View mockMainView;

    private IGasolinerasRepository fuelStationRepository;

    @Before
    public void start(){
        MockitoAnnotations.openMocks(this);
        sut = new MainPresenter(mockMainView);

        Context context = ApplicationProvider.getApplicationContext();

        fuelStationRepository = new GasolinerasRepository(context);
        when(mockMainView.getGasolineraRepository()).thenReturn(fuelStationRepository);

        sut.init();

        //Lista 1 elemento
        brandsList1Element.add("REPSOL");

        //Lista 2 elementos
        brandsList2Element.add("CEPSA");
        brandsList2Element.add("REPSOL");

        // Lista con una marca no existente
        wrongBrandList.add("REPSOLITO");



    }

    @After
    public void end() {
        GasolineraDatabase db = GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext());
        db.close();
    }

    @Test
    public void testFilter() {

        // Caso valido: lista de gasolineras Repsol con diesel
        sut.filter(CombustibleType.DIESEL, brandsList1Element, false);
        assertEquals(37, sut.getShownGasolineras().size());
        assertEquals("REPSOL", sut.getShownGasolineras().get(0).getRotulo());
        assertEquals("REPSOL", sut.getShownGasolineras().get(1).getRotulo());
        assertEquals("2,009", sut.getShownGasolineras().get(0).getDieselA());
        assertEquals("1,979", sut.getShownGasolineras().get(1).getDieselA());

        // Caso valido: lista de gasolineras Cepsa y Repsol con gasolina
        sut.filter(CombustibleType.GASOLINA, brandsList2Element, false);
        assertEquals(55, sut.getShownGasolineras().size());
        assertEquals("CEPSA", sut.getShownGasolineras().get(0).getRotulo());
        assertEquals("REPSOL", sut.getShownGasolineras().get(54).getRotulo());
        assertEquals("1,829", sut.getShownGasolineras().get(0).getNormal95());
        assertEquals("1,879", sut.getShownGasolineras().get(54).getNormal95());

        // Caso valido: lista de gasolineras lowcost con gasolina y diesel
        sut.filter(CombustibleType.ALL_COMB, emptyBrandList, true);
        assertEquals(10, sut.getShownGasolineras().size());
        assertEquals("PETROPRIX", sut.getShownGasolineras().get(0).getRotulo());
        assertEquals("BALLENOIL", sut.getShownGasolineras().get(1).getRotulo());


        // Caso no valido: lista con una marca no existente
        sut.filter(CombustibleType.ALL_COMB, wrongBrandList, false);
        assertNull(sut.getShownGasolineras());
    }


}
