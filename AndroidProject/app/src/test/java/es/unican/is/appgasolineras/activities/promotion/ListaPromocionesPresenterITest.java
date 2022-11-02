package es.unican.is.appgasolineras.activities.promotion;

import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import static org.junit.Assert.assertEquals;
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

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;
import es.unican.is.appgasolineras.repository.PromocionesRepository;


@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)

public class ListaPromocionesPresenterITest {


    private List<Promocion> promotionsList = new ArrayList<Promocion>();

    private Promocion promotion1 = new Promocion();

    private List<Gasolinera> fuelStationsList = new ArrayList<>();

    private Gasolinera fuelStation1 = new Gasolinera();

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

        Promocion promocion = new Promocion("Promocion",-1.0,0.20,"Diesel");
        promotionRepository.insertPromocion(promocion);

        sut.init();
    }

    @Test
    public void testListaPromociones(){
        assertEquals("Promocion", sut.getShownPromociones().get(0).getId());

    }



}
