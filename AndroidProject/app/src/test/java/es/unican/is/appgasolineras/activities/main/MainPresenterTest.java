package es.unican.is.appgasolineras.activities.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.common.utils.EnumTypes.CombustibleType;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    private List<String> brandsList = new ArrayList<>();
    private List<String> wrongBrandList = new ArrayList<>();
    private List<String> emptyBrandList = new ArrayList<>();
    private List<String> brandsList1Element = new ArrayList<>();
    private List<String> brandsList2Element = new ArrayList<>();

    private List<Gasolinera> fuelStationsList = new ArrayList<>();
    private List<Gasolinera> fuelStationsCepsa = new ArrayList<>();
    private List<Gasolinera> fuelStationsListLowcost = new ArrayList<>();
    private List<Gasolinera> fuelStationsCepsaYRepsol = new ArrayList<>();
    private List<Gasolinera> fuelStationsRepsol = new ArrayList<>();
    private List<Gasolinera> fuelStationsLowcost = new ArrayList<>();

    private Gasolinera fuelStation1 = new Gasolinera();
    private Gasolinera fuelStation2 = new Gasolinera();
    private Gasolinera fuelStation3 = new Gasolinera();
    private Gasolinera fuelStation4 = new Gasolinera();
    private Gasolinera fuelStation5 = new Gasolinera();
    private Gasolinera fuelStation6 = new Gasolinera();
    private Gasolinera fuelStation7 = new Gasolinera();
    private Gasolinera fuelStation8 = new Gasolinera();
    private Gasolinera fuelStation9 = new Gasolinera();

    private MainPresenter sut;

    @Mock
    IMainContract.View mockMainView;
    @Mock
    IGasolinerasRepository mockFuelStationRepository;

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
        fuelStation1.setNormal95("2.0");

        fuelStation2.setId("222");
        fuelStation2.setRotulo("CEPSA");
        fuelStation2.setCp("11111");
        fuelStation2.setHorario("323232");
        fuelStation2.setDireccion("AABBCC");
        fuelStation2.setMunicipio("COMILLAS");
        fuelStation2.setDieselA("");
        fuelStation2.setNormal95("2.0");

        fuelStation3.setId("333");
        fuelStation3.setRotulo("REPSOL");
        fuelStation3.setCp("11111");
        fuelStation3.setHorario("323232");
        fuelStation3.setDireccion("AABBCC");
        fuelStation3.setMunicipio("COMILLAS");
        fuelStation3.setDieselA("1.5");
        fuelStation3.setNormal95("");

        fuelStation4.setId("444");
        fuelStation4.setRotulo("REPSOL");
        fuelStation4.setCp("11111");
        fuelStation4.setHorario("323232");
        fuelStation4.setDireccion("AABBCC");
        fuelStation4.setMunicipio("COMILLAS");
        fuelStation4.setDieselA("");
        fuelStation4.setNormal95("");

        fuelStation5.setId("555");
        fuelStation5.setRotulo("REPSOL");
        fuelStation5.setCp("11111");
        fuelStation5.setHorario("323232");
        fuelStation5.setDireccion("AABBCC");
        fuelStation5.setMunicipio("COMILLAS");
        fuelStation5.setDieselA("1.5");
        fuelStation5.setNormal95("2.0");

        fuelStation6.setId("666");
        fuelStation6.setRotulo("Petroprix");
        fuelStation6.setCp("11111");
        fuelStation6.setHorario("323232");
        fuelStation6.setDireccion("AABBCC");
        fuelStation6.setMunicipio("COMILLAS");
        fuelStation6.setDieselA("1.779");
        fuelStation6.setNormal95("1.699");

        fuelStation7.setId("444");
        fuelStation7.setRotulo("CEPSA");
        fuelStation7.setCp("11111");
        fuelStation7.setHorario("323232");
        fuelStation7.setDireccion("AABBCC");
        fuelStation7.setMunicipio("COMILLAS");
        fuelStation7.setDieselA("");
        fuelStation7.setNormal95("");

        fuelStation8.setId("555");
        fuelStation8.setRotulo("REPSOL");
        fuelStation8.setCp("11111");
        fuelStation8.setHorario("323232");
        fuelStation8.setDireccion("AABBCC");
        fuelStation8.setMunicipio("COMILLAS");
        fuelStation8.setDieselA("");
        fuelStation8.setNormal95("");

        fuelStation9.setId("666");
        fuelStation9.setRotulo("Ballenoil");
        fuelStation9.setCp("11111");
        fuelStation9.setHorario("323232");
        fuelStation9.setDireccion("AABBCC");
        fuelStation9.setMunicipio("COMILLAS");
        fuelStation9.setDieselA("1.5");
        fuelStation9.setNormal95("");
        fuelStationsList.clear();

        fuelStationsListLowcost.add(fuelStation3);
        fuelStationsListLowcost.add(fuelStation6);

        // Inicializamos los mocks y se los asignamos al sut
        MockitoAnnotations.openMocks(this);
        when(mockMainView.getGasolineraRepository()).thenReturn(mockFuelStationRepository);
        when(mockFuelStationRepository.getGasolineras()).thenReturn(fuelStationsList);
        when(mockFuelStationRepository.getGasolinerasLowcost()).thenReturn(fuelStationsListLowcost);
    }

    @Test
    public void testFilterByBrand() {
        // Anhadimos las gasolineras a una lista
        fuelStationsList.add(fuelStation1);
        fuelStationsList.add(fuelStation2);
        fuelStationsList.add(fuelStation3);
        fuelStationsList.add(fuelStation4);

        sut = new MainPresenter(mockMainView);
        sut.init();
        // Lista con las gasolineras de Cepsa
        fuelStationsCepsa.add(fuelStation1);
        fuelStationsCepsa.add(fuelStation2);

        // Lista de marcas
        brandsList.add("CEPSA");
        brandsList.add("REPSOL");

        // Lista con una marca no existente
        wrongBrandList.add("REPSOLITO");

        // Caso valido: lista con una marca existente

        sut.filter(CombustibleType.ALL_COMB, brandsList.subList(0, 1), false);
        assertEquals(fuelStationsCepsa, sut.getShownGasolineras());
        verify(mockMainView).showGasolinerasAdvanced(sut.getShownGasolineras(), CombustibleType.ALL_COMB);

        // Caso válido: lista con mas de una marca existente
        sut.filter(CombustibleType.ALL_COMB, brandsList, false);
        assertEquals(fuelStationsList, sut.getShownGasolineras());
        verify(mockMainView, atLeast(1)).showGasolinerasAdvanced(sut.getShownGasolineras(), CombustibleType.ALL_COMB);

        // Caso valido: lista vacia
        sut.filter(CombustibleType.ALL_COMB, brandsList.subList(0, 0), false);
        assertEquals(fuelStationsList, sut.getShownGasolineras());
        verify(mockMainView, atLeast(2)).showGasolinerasAdvanced(sut.getShownGasolineras(), CombustibleType.ALL_COMB);

        // Caso no valido: lista con una marca no existente
        sut.filter(CombustibleType.ALL_COMB, wrongBrandList, false);
        assertNull(sut.getShownGasolineras());
        verify(mockMainView).showLoadEmpty();
    }

    @Test
    public void testFilterByCombustible() {
        // Anhadimos las gasolineras a una lista
        fuelStationsList.add(fuelStation1);
        fuelStationsList.add(fuelStation2);
        fuelStationsList.add(fuelStation3);
        fuelStationsList.add(fuelStation4);
        sut = new MainPresenter(mockMainView);
        sut.init();
        // Filtrar por diésel
        sut.filterByCombustible(CombustibleType.DIESEL);
        assertEquals("111", sut.getShownGasolineras().get(0).getId());
        assertEquals("333", sut.getShownGasolineras().get(1).getId());
        assertEquals(2, sut.getShownGasolineras().size());


        // Filtrar por gasolina
        sut.init();
        sut.filterByCombustible(CombustibleType.GASOLINA);
        assertEquals("111", sut.getShownGasolineras().get(0).getId());
        assertEquals("222", sut.getShownGasolineras().get(1).getId());
        assertEquals(2, sut.getShownGasolineras().size());


        // Filtrar por todos
        sut.init();
        sut.filterByCombustible(CombustibleType.ALL_COMB);
        assertEquals("111", sut.getShownGasolineras().get(0).getId());
        assertEquals("222", sut.getShownGasolineras().get(1).getId());
        assertEquals("333", sut.getShownGasolineras().get(2).getId());
        assertEquals("444", sut.getShownGasolineras().get(3).getId());
        assertEquals(4, sut.getShownGasolineras().size());

        // Lista de gasolineras vacía
        fuelStationsList.clear();
        when(mockFuelStationRepository.getGasolineras()).thenReturn(fuelStationsList);
        sut.init();
        sut.filterByCombustible(CombustibleType.ALL_COMB);
        assertTrue(sut.getShownGasolineras().isEmpty());
    }

    @Test
    public void testFilterByLowcost() {
        fuelStationsList.add(fuelStation1);
        fuelStationsList.add(fuelStation5);
        fuelStationsList.add(fuelStation6);
        fuelStationsList.add(fuelStation7);
        fuelStationsList.add(fuelStation8);
        fuelStationsList.add(fuelStation9);

        sut = new MainPresenter(mockMainView);
        sut.init();

        fuelStationsCepsaYRepsol.add(fuelStation1);
        fuelStationsCepsaYRepsol.add(fuelStation5);
        fuelStationsRepsol.add(fuelStation5);
        fuelStationsLowcost.add(fuelStation6);
        fuelStationsLowcost.add(fuelStation9);


        //Lista 1 elemento
        brandsList1Element.add("REPSOL");

        //Lista 2 elementos
        brandsList2Element.add("CEPSA");
        brandsList2Element.add("REPSOL");

        // Lista con una marca no existente
        wrongBrandList.add("REPSOLITO");


        // Caso valido: lista de gasolineras Repsol con diesel
        sut.filter(CombustibleType.DIESEL, brandsList1Element, false);
        assertEquals(fuelStationsRepsol, sut.getShownGasolineras());
        verify(mockMainView).showGasolinerasAdvanced(sut.getShownGasolineras(), CombustibleType.DIESEL);

        // Caso valido: lista de gasolineras Cepsa y Repsol con gasolina
        sut.filter(CombustibleType.GASOLINA, brandsList2Element, false);
        assertEquals(fuelStationsCepsaYRepsol, sut.getShownGasolineras());
        verify(mockMainView).showGasolinerasAdvanced(sut.getShownGasolineras(), CombustibleType.GASOLINA);

        // Caso valido: lista de gasolineras lowcost con gasolina y diesel
        sut.filter(CombustibleType.ALL_COMB, emptyBrandList, true);
        assertEquals(fuelStationsLowcost, sut.getShownGasolineras());
        verify(mockMainView).showGasolinerasAdvanced(sut.getShownGasolineras(), CombustibleType.ALL_COMB);

        // Caso no valido: lista con una marca no existente
        sut.filter(CombustibleType.ALL_COMB, wrongBrandList, false);
        assertNull(sut.getShownGasolineras());
        verify(mockMainView).showLoadEmpty();

    }
}
