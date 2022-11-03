package es.unican.is.appgasolineras.activities.promotion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;
import es.unican.is.appgasolineras.repository.PromocionesRepository;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)

public class EliminarPromocionPresenterTest {
    Promocion promocion;

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL2();
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
    }

    private ListaPromocionesPresenter sut;
    @Mock
    private IListaPromocionesContract.View mockMainView;

    private IGasolinerasRepository fuelStationRepository;
    private IPromocionesRepository promocionesRepository;
    @Before
    public void start() {
        MockitoAnnotations.openMocks(this);
        sut = new ListaPromocionesPresenter(mockMainView);

        Context context = ApplicationProvider.getApplicationContext();

        fuelStationRepository = new GasolinerasRepository(context);
        when(mockMainView.getGasolineraRepository()).thenReturn(fuelStationRepository);

        promocionesRepository = new PromocionesRepository(context);
        when(mockMainView.getPromocionRepository()).thenReturn(promocionesRepository);


        //when(promocionesRepository.getPromocionById("Promocion")).thenReturn(promocion);


        promocion = new Promocion();
        // Inicializamos nuevas promociones
        promocion.setId("Promocion");
        promocion.setCombustibles("CEPSA");
        promocion.setDescuentoEurosLitro(0.20);
        promocion.setCombustibles("Diesel");

        promocionesRepository.insertPromocion(promocion);
        promocionesRepository.insertRelacionGasolineraPromocion(fuelStationRepository.getGasolineras().get(0), promocion);

        sut.init();

    }

    @Test
    public void testEliminar() {
        //Caso válido: Eliminación exitosa
        assertEquals(promocion, sut.deletePromocion("Promocion"));

    }
}
