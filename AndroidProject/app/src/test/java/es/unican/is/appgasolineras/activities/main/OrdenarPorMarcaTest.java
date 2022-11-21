package es.unican.is.appgasolineras.activities.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.common.utils.EnumTypes.PriceFilterType;
import es.unican.is.appgasolineras.common.utils.EnumTypes.PriceOrderType;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

@RunWith(MockitoJUnitRunner.class)

public class OrdenarPorMarcaTest extends TestCase {
    private List<Gasolinera> fuelStationsList = new ArrayList<>();
    private List<Gasolinera> fuelStationsOrdenada = new ArrayList<>();
    private List<Promocion> promociones = new ArrayList<>();

    private Gasolinera fuelStation1 = new Gasolinera();
    private Gasolinera fuelStation2 = new Gasolinera();
    private Gasolinera fuelStation3 = new Gasolinera();
    private Gasolinera fuelStation4 = new Gasolinera();


    private MainPresenter sut;
    @Mock
    IMainContract.View mockMainView;
    @Mock
    IGasolinerasRepository mockFuelStationRepository;
    @Mock
    IPromocionesRepository mockPromocionesRepository;

    @Before
    public void inicializa(){
        // Inicializamos nuevas gasolineras
        fuelStation1.setId("111");
        fuelStation1.setRotulo("CEPSA");
        fuelStation1.setCp("11111");
        fuelStation1.setHorario("323232");
        fuelStation1.setDireccion("AABBCC");
        fuelStation1.setMunicipio("COMILLAS");
        fuelStation1.setDieselA("1.5");
        fuelStation1.setNormal95("1.5");

        fuelStation2.setId("222");
        fuelStation2.setRotulo("CEPSA");
        fuelStation2.setCp("11111");
        fuelStation2.setHorario("323232");
        fuelStation2.setDireccion("AABBCC");
        fuelStation2.setMunicipio("COMILLAS");
        fuelStation2.setDieselA("0.5");
        fuelStation2.setNormal95("0.5");

        fuelStation3.setId("333");
        fuelStation3.setRotulo("REPSOL");
        fuelStation3.setCp("11111");
        fuelStation3.setHorario("323232");
        fuelStation3.setDireccion("AABBCC");
        fuelStation3.setMunicipio("COMILLAS");
        fuelStation3.setDieselA("2");
        fuelStation3.setNormal95("2");

        fuelStation4.setId("444");
        fuelStation4.setRotulo("REPSOL");
        fuelStation4.setCp("11111");
        fuelStation4.setHorario("323232");
        fuelStation4.setDireccion("AABBCC");
        fuelStation4.setMunicipio("COMILLAS");
        fuelStation4.setDieselA("3");
        fuelStation4.setNormal95("3");

        // Anhadimos las gasolineras a una lista
        fuelStationsList.add(fuelStation1);
        fuelStationsList.add(fuelStation2);
        fuelStationsList.add(fuelStation3);
        fuelStationsList.add(fuelStation4);

        // Inicializamos los mocks y se los asignamos al sut
        MockitoAnnotations.openMocks(this);

        sut = new MainPresenter(mockMainView);

        when(mockMainView.getGasolineraRepository()).thenReturn(mockFuelStationRepository);
        when(mockMainView.getPromotionsRepository()).thenReturn(mockPromocionesRepository);
        when(mockFuelStationRepository.getGasolineras()).thenReturn(fuelStationsList);
        when(mockPromocionesRepository.getPromocionesRelacionadasConGasolinera("111")).thenReturn(promociones);
        when(mockPromocionesRepository.getPromocionesRelacionadasConGasolinera("222")).thenReturn(promociones);
        when(mockPromocionesRepository.getPromocionesRelacionadasConGasolinera("333")).thenReturn(promociones);
        when(mockPromocionesRepository.getPromocionesRelacionadasConGasolinera("444")).thenReturn(promociones);

        sut.init();
    }

    @Test
    public void testOrderGasolineras() {

        //Caso exito: ordena Diesel de forma ascendente
        sut.orderByPrice(PriceOrderType.ASC, PriceFilterType.DIESEL);
        assertEquals(4, sut.getShownGasolineras().size());
        assertEquals(fuelStation2.getId(), sut.getShownGasolineras().get(0).getId());
        assertEquals(fuelStation1.getId(), sut.getShownGasolineras().get(1).getId());
        assertEquals(fuelStation3.getId(), sut.getShownGasolineras().get(2).getId());
        assertEquals(fuelStation4.getId(), sut.getShownGasolineras().get(3).getId());

        //Caso exito: ordena Gasolina de forma ascendente
        sut.orderByPrice(PriceOrderType.ASC, PriceFilterType.GASOLINA);
        assertEquals(4, sut.getShownGasolineras().size());
        assertEquals(fuelStation2.getId(), sut.getShownGasolineras().get(0).getId());
        assertEquals(fuelStation1.getId(), sut.getShownGasolineras().get(1).getId());
        assertEquals(fuelStation3.getId(), sut.getShownGasolineras().get(2).getId());
        assertEquals(fuelStation4.getId(), sut.getShownGasolineras().get(3).getId());



        //Caso exito: ordena PrecioSumario de forma descendente
        sut.orderByPrice(PriceOrderType.DESC, PriceFilterType.SUMARIO);
        assertEquals(4, sut.getShownGasolineras().size());
        assertEquals(fuelStation4.getId(), sut.getShownGasolineras().get(0).getId());
        assertEquals(fuelStation3.getId(), sut.getShownGasolineras().get(1).getId());
        assertEquals(fuelStation1.getId(), sut.getShownGasolineras().get(2).getId());
        assertEquals(fuelStation2.getId(), sut.getShownGasolineras().get(3).getId());



    }
}
