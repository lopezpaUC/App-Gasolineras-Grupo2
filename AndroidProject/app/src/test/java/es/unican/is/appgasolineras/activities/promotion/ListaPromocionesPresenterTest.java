package es.unican.is.appgasolineras.activities.promotion;

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

import java.util.ArrayList;
import java.util.List;


import es.unican.is.appgasolineras.activities.main.CombustibleType;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

@RunWith(MockitoJUnitRunner.class)

public class ListaPromocionesPresenterTest extends TestCase {


    private List<Promocion> promotionsList = new ArrayList<Promocion>();
    private List<Promocion> promotionsListEmpty = new ArrayList<Promocion>();

    private Promocion promotion1 = new Promocion();
    private Promocion promotion2 = new Promocion();

    private List<Gasolinera> fuelStationsList = new ArrayList<>();

    private Gasolinera fuelStation1 = new Gasolinera();

    private ListaPromocionesPresenter sut;

    @Mock
    IListaPromocionesContract.View mockListaPromocionesView;
    @Mock
    IPromocionesRepository mockPromotionRepository;
    @Mock
    IGasolinerasRepository mockFuelStationRepository;

    @Before
    public void inicializa(){
        // Inicializamos nuevas promociones
        promotion1.setId("Promocion1");
        promotion1.setCombustibles("Diesel");
        promotion1.setDescuentoEurosLitro(0.20);

        promotion2.setId("Promocion2");
        promotion2.setCombustibles("Gasolina");
        promotion2.setDescuentoEurosLitro(0.20);


        fuelStation1.setId("111");
        fuelStation1.setRotulo("CEPSA");
        fuelStation1.setCp("11111");
        fuelStation1.setHorario("323232");
        fuelStation1.setDireccion("AABBCC");
        fuelStation1.setMunicipio("COMILLAS");
        fuelStation1.setDieselA("1.5");
        fuelStation1.setNormal95("2.0");

        fuelStationsList.add(fuelStation1);

        // Anhadimos las gasolineras a una lista
        promotionsList.add(promotion1);
        promotionsList.add(promotion2);

        // Inicializamos los mocks y se los asignamos al sut
        MockitoAnnotations.openMocks(this);
        sut = new ListaPromocionesPresenter(mockListaPromocionesView);
        when(mockListaPromocionesView.getPromocionRepository()).thenReturn(mockPromotionRepository);
        when(mockPromotionRepository.getPromociones()).thenReturn(promotionsList);
        when(mockListaPromocionesView.getGasolineraRepository()).thenReturn(mockFuelStationRepository);
        when(mockFuelStationRepository.getGasolinerasRelacionadasConPromocion("Promocion1")).thenReturn(fuelStationsList);
        when(mockFuelStationRepository.getGasolinerasRelacionadasConPromocion("Promocion2")).thenReturn(fuelStationsList);
        sut.init();
    }

    @Test
    public void testListaPromociones(){

        // Caso válido: lista con varias promociones
        assertEquals(promotionsList, sut.getShownPromociones());
        assertEquals(2, sut.getShownPromociones().size());
        assertEquals("Promocion1", sut.getShownPromociones().get(0).getId());
        assertEquals("Diesel", sut.getShownPromociones().get(0).getCombustibles());
        assertTrue(0.20 == sut.getShownPromociones().get(0).getDescuentoEurosLitro());
        assertEquals("Promocion2",sut.getShownPromociones().get(1).getId());
        assertEquals("Gasolina", sut.getShownPromociones().get(1).getCombustibles());
        assertTrue(0.20 == sut.getShownPromociones().get(1).getDescuentoEurosLitro());

        // Caso válido: lista sin promociones
        assertEquals(0,promotionsListEmpty.size());


    }



}
