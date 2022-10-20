package es.unican.is.appgasolineras.activities.main;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

@RunWith(MockitoJUnitRunner.class)

public class MainPresenterTest extends TestCase {

    private List<String> brandsList = new ArrayList<>();
    private List<String> wrongBrandList = new ArrayList<>();
    private List<Gasolinera> fuelStationsList = new ArrayList<>();
    private List<Gasolinera> fuelStationsCepsa = new ArrayList<>();
    private List<Gasolinera> emptyFuelStationsList = new ArrayList<>();

    private Gasolinera fuelStation1 = new Gasolinera();
    private Gasolinera fuelStation2 = new Gasolinera();
    private Gasolinera fuelStation3 = new Gasolinera();
    private Gasolinera fuelStation4 = new Gasolinera();

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
        fuelStation2.setDieselA("1.5");
        fuelStation2.setNormal95("2.0");

        fuelStation3.setId("333");
        fuelStation3.setRotulo("REPSOL");
        fuelStation3.setCp("11111");
        fuelStation3.setHorario("323232");
        fuelStation3.setDireccion("AABBCC");
        fuelStation3.setMunicipio("COMILLAS");
        fuelStation3.setDieselA("1.5");
        fuelStation3.setNormal95("2.0");

        fuelStation4.setId("444");
        fuelStation4.setRotulo("REPSOL");
        fuelStation4.setCp("11111");
        fuelStation4.setHorario("323232");
        fuelStation4.setDireccion("AABBCC");
        fuelStation4.setMunicipio("COMILLAS");
        fuelStation4.setDieselA("1.5");
        fuelStation4.setNormal95("2.0");

        // Anhadimos las gasolineras a una lista
        fuelStationsList.add(fuelStation1);
        fuelStationsList.add(fuelStation2);
        fuelStationsList.add(fuelStation3);
        fuelStationsList.add(fuelStation4);

        // Inicializamos los mocks y se los asignamos al sut
        MockitoAnnotations.openMocks(this);
        sut = new MainPresenter(mockMainView);
        when(mockMainView.getGasolineraRepository()).thenReturn(mockFuelStationRepository);
        when(mockFuelStationRepository.getGasolineras()).thenReturn(fuelStationsList);
        sut.init();
    }

    @Test
    public void testFilterByBrand() {
        // Lista con las gasolineras de Cepsa
        fuelStationsCepsa.add(fuelStation1);
        fuelStationsCepsa.add(fuelStation2);

        // Lista de marcas
        brandsList.add("CEPSA");
        brandsList.add("REPSOL");

        // Lista con una marca no existente
        wrongBrandList.add("REPSOLITO");

        // Caso valido: lista con una marca existente
        sut.filter(CombustibleType.ALL_COMB, brandsList.subList(0, 1));
        assertEquals(fuelStationsCepsa, sut.getShownGasolineras());

        // Caso válido: lista con mas de una marca existente
        sut.filter(CombustibleType.ALL_COMB, brandsList);
        assertEquals(fuelStationsList, sut.getShownGasolineras());

        // Caso valido: lista vacia
        sut.filter(CombustibleType.ALL_COMB, brandsList.subList(0, 0));
        assertEquals(fuelStationsList, sut.getShownGasolineras());

        // Caso no valido: lista con una marca no existente
        sut.filter(CombustibleType.ALL_COMB, wrongBrandList);
        assertNull(sut.getShownGasolineras());
    }
}
