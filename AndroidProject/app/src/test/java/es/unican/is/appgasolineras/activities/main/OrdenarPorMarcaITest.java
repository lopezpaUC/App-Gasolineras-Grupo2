package es.unican.is.appgasolineras.activities.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.activities.detail.IGasolineraDetailContract;
import es.unican.is.appgasolineras.activities.main.IMainContract;
import es.unican.is.appgasolineras.activities.main.MainPresenter;
import es.unican.is.appgasolineras.activities.main.PriceFilterType;
import es.unican.is.appgasolineras.activities.main.PriceOrderType;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;
import es.unican.is.appgasolineras.repository.PromocionesRepository;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk = {Build.VERSION_CODES.O_MR1})

public class OrdenarPorMarcaITest {
    Promocion promocion;


    @BeforeClass
    public static void init() {
        GasolinerasServiceConstants.setStaticURL3();
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
    }

    private MainPresenter sut;
    @Mock
    private IMainContract.View mockMainView;

    private IGasolinerasRepository fuelStationRepository;
    private IPromocionesRepository promocionesRepository;

    @Before
    public void start() {
        MockitoAnnotations.openMocks(this);
        sut = new MainPresenter(mockMainView);

        Context context = ApplicationProvider.getApplicationContext();
        fuelStationRepository = new GasolinerasRepository(context);
        when(mockMainView.getGasolineraRepository()).thenReturn(fuelStationRepository);

        promocionesRepository = new PromocionesRepository(context);
        when(mockMainView.getPromotionsRepository()).thenReturn(promocionesRepository);


        sut.init();
    }

    @After
    public void cleanDatabase() {
        promocionesRepository.deleteAllPromociones();
        GasolineraDatabase db = GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext());
        db.close();
    }

    @AfterClass
    public static void end() {
        GasolinerasService.resetAPI();
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Test
    public void testOrderGasolineras() {
        //Caso exito: ordena Diesel de forma ascendente
        sut.orderByPrice(PriceOrderType.ASC, PriceFilterType.DIESEL);

        assertEquals(5, sut.getShownGasolineras().size());
        assertEquals(sut.getShownGasolineras().get(0).getId(), "1036");
        assertEquals(sut.getShownGasolineras().get(1).getId(), "1039");
        assertEquals(sut.getShownGasolineras().get(2).getId(), "1095");

        //Caso exito: ordena Gasolina de forma ascendente
        sut.orderByPrice(PriceOrderType.ASC, PriceFilterType.GASOLINA);

        assertEquals(5, sut.getShownGasolineras().size());
        assertEquals(sut.getShownGasolineras().get(0).getId(), "1048");
        assertEquals(sut.getShownGasolineras().get(1).getId(), "1095");
        assertEquals(sut.getShownGasolineras().get(2).getId(), "1039");

        //Caso exito: ordena PrecioSumario de forma descendente
        sut.orderByPrice(PriceOrderType.DESC, PriceFilterType.SUMARIO);

        assertEquals(5, sut.getShownGasolineras().size());
        assertEquals(sut.getShownGasolineras().get(0).getId(), "1039");
        assertEquals(sut.getShownGasolineras().get(1).getId(), "1095");
        assertEquals(sut.getShownGasolineras().get(2).getId(), "1048");

    }
}
