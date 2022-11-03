package es.unican.is.appgasolineras.activities.promotion;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;
import es.unican.is.appgasolineras.repository.PromocionesRepository;

@RunWith(MockitoJUnitRunner.class)
public class EliminarPromocionPresenterITest extends TestCase{
    private List<Gasolinera> fuelStationsList = new ArrayList<>();

    private Gasolinera fuelStation1 = new Gasolinera();

    private ListaPromocionesPresenter sut;
    @Mock
    private IListaPromocionesContract.View mockMainView;
    @Mock
    private IGasolinerasRepository mockFuelStationRepository;
    @Mock
    private IPromocionesRepository mockPromocionesRepository;

    Promocion promocion;
    @Before
    public void start() {

        // Inicializamos nuevas gasolineras
        fuelStation1.setId("111");
        fuelStation1.setRotulo("CEPSA");
        fuelStation1.setCp("11111");
        fuelStation1.setHorario("323232");
        fuelStation1.setDireccion("AABBCC");
        fuelStation1.setMunicipio("COMILLAS");
        fuelStation1.setDieselA("1.5");
        fuelStation1.setNormal95("2.0");

        promocion = new Promocion();
        // Inicializamos nuevas promociones
        promocion.setId("Promocion");
        promocion.setCombustibles("CEPSA");
        promocion.setDescuentoEurosLitro(0.20);
        promocion.setCombustibles("Diesel");

        fuelStationsList.add(fuelStation1);
        MockitoAnnotations.openMocks(this);
        sut = new ListaPromocionesPresenter(mockMainView);

        when(mockMainView.getGasolineraRepository()).thenReturn(mockFuelStationRepository);
        when(mockMainView.getPromocionRepository()).thenReturn(mockPromocionesRepository);
        when(mockPromocionesRepository.getPromocionById("Promocion")).thenReturn(promocion);
        when(mockPromocionesRepository.deletePromocion(promocion)).thenReturn(promocion);


        //when(promocionesRepository.getPromocionById("Promocion")).thenReturn(promocion);

        sut.init();

    }

    @Test
    public void testEliminar() {
        //Caso válido: Eliminación exitosa
        assertEquals(promocion.getId(), sut.deletePromocion("Promocion").getId());

    }
}
