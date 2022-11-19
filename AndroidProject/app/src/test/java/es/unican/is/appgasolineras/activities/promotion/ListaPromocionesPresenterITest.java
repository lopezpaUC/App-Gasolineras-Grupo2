package es.unican.is.appgasolineras.activities.promotion;

import static org.mockito.Mockito.when;

import android.content.Context;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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


import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;
import es.unican.is.appgasolineras.repository.PromocionesRepository;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;


@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk = {Build.VERSION_CODES.O_MR1})

public class ListaPromocionesPresenterITest {

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL2();
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
    }

    @After
    public void cleanDatabase() {
        promotionRepository.deleteAllPromociones();
        GasolineraDatabase db = GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext());
        db.close();
    }

    private ListaPromocionesPresenter sut;

    @Mock
    IListaPromocionesContract.View mockListaPromocionesView;

    IPromocionesRepository promotionRepository;
    IGasolinerasRepository fuelStationRepository;

    @Before
    public void inicializa(){

        // Inicializamos los mocks y se los asignamos al sut
        MockitoAnnotations.openMocks(this);
        sut = new ListaPromocionesPresenter(mockListaPromocionesView);

        Context context = ApplicationProvider.getApplicationContext();

        fuelStationRepository = new GasolinerasRepository(context);
        when(mockListaPromocionesView.getGasolineraRepository()).thenReturn(fuelStationRepository);
        promotionRepository = new PromocionesRepository(context);
        when(mockListaPromocionesView.getPromocionRepository()).thenReturn(promotionRepository);

        promotionRepository.deleteAllPromociones();

        Promocion promocion1 = new Promocion("Promocion1",-1.0,0.20,"Diesel");
        Promocion promocion2 = new Promocion("Promocion2",0.20,-1.0,"Gasolina");
        promotionRepository.insertPromocion(promocion2);
        promotionRepository.insertPromocion(promocion1);
        promotionRepository.insertRelacionGasolineraPromocion(fuelStationRepository.getGasolineras().get(0),promocion1);
        promotionRepository.insertRelacionGasolineraPromocion(fuelStationRepository.getGasolineras().get(1),promocion2);

        sut.init();
    }

    @Test
    public void testListaPromociones(){

        // Caso válido: lista con varias promociones
        assertEquals(2, sut.getShownPromociones().size());
        assertEquals("Promocion1", sut.getShownPromociones().get(1).getId());
        assertEquals("Diesel", sut.getShownPromociones().get(1).getCombustibles());
        assertEquals(0.20,sut.getShownPromociones().get(1).getDescuentoEurosLitro(), 0.001);
        assertEquals("Promocion2", sut.getShownPromociones().get(0).getId());
        assertEquals("Gasolina", sut.getShownPromociones().get(0).getCombustibles());
        assertEquals(0.20,sut.getShownPromociones().get(0).getDescuentoPorcentual(), 0.001);

        //Elimina las promociones
        promotionRepository.deleteAllPromociones();

        // Caso válido: lista sin promociones
        assertEquals(0,promotionRepository.getPromociones().size());
    }



}
