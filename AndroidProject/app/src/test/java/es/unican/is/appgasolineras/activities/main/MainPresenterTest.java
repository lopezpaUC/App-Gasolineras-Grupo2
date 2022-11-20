package es.unican.is.appgasolineras.activities.main;

import static org.mockito.Mockito.verify;
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

import es.unican.is.appgasolineras.common.utils.EnumTypes.CombustibleType;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

@RunWith(MockitoJUnitRunner.class)

public class MainPresenterTest extends TestCase {

    private List<String> brandsList = new ArrayList<>();
    private List<String> brandsList1Element = new ArrayList<>();
    private List<String> brandsList2Element = new ArrayList<>();
    private List<String> wrongBrandList = new ArrayList<>();
    private List<String> emptyBrandList = new ArrayList<>();
    private List<Gasolinera> fuelStationsList = new ArrayList<>();
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
        fuelStation2.setRotulo("REPSOL");
        fuelStation2.setCp("11111");
        fuelStation2.setHorario("323232");
        fuelStation2.setDireccion("AABBCC");
        fuelStation2.setMunicipio("COMILLAS");
        fuelStation2.setDieselA("1.5");
        fuelStation2.setNormal95("2.0");

        fuelStation3.setId("333");
        fuelStation3.setRotulo("Petroprix");
        fuelStation3.setCp("11111");
        fuelStation3.setHorario("323232");
        fuelStation3.setDireccion("AABBCC");
        fuelStation3.setMunicipio("COMILLAS");
        fuelStation3.setDieselA("1.779");
        fuelStation3.setNormal95("1.699");

        fuelStation4.setId("444");
        fuelStation4.setRotulo("CEPSA");
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
        fuelStation5.setDieselA("");
        fuelStation5.setNormal95("");

        fuelStation6.setId("666");
        fuelStation6.setRotulo("Ballenoil");
        fuelStation6.setCp("11111");
        fuelStation6.setHorario("323232");
        fuelStation6.setDireccion("AABBCC");
        fuelStation6.setMunicipio("COMILLAS");
        fuelStation6.setDieselA("1.5");
        fuelStation6.setNormal95("");


        // Anhadimos las gasolineras a una lista
        fuelStationsList.add(fuelStation1);
        fuelStationsList.add(fuelStation2);
        fuelStationsList.add(fuelStation3);
        fuelStationsList.add(fuelStation4);
        fuelStationsList.add(fuelStation5);
        fuelStationsList.add(fuelStation6);

        fuelStationsListLowcost.add(fuelStation3);
        fuelStationsListLowcost.add(fuelStation6);

        // Inicializamos los mocks y se los asignamos al sut
        MockitoAnnotations.openMocks(this);
        sut = new MainPresenter(mockMainView);
        when(mockMainView.getGasolineraRepository()).thenReturn(mockFuelStationRepository);
        when(mockFuelStationRepository.getGasolineras()).thenReturn(fuelStationsList);
        when(mockFuelStationRepository.getGasolinerasLowcost()).thenReturn(fuelStationsListLowcost);
        sut.init();
    }

    @Test
    public void testFilter() {
        // Listas con las gasolineras
        fuelStationsCepsaYRepsol.add(fuelStation1);
        fuelStationsCepsaYRepsol.add(fuelStation2);
        fuelStationsRepsol.add(fuelStation2);
        fuelStationsLowcost.add(fuelStation3);
        fuelStationsLowcost.add(fuelStation6);


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
