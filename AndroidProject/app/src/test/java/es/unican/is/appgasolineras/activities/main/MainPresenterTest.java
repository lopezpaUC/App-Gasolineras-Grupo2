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

        fuelStation1.setId("111");
        fuelStation1.setRotulo("Cepsa");
        fuelStation1.setCp("11111");
        fuelStation1.setHorario("323232");
        fuelStation1.setDireccion("AABBCC");
        fuelStation1.setMunicipio("COMILLAS");
        fuelStation1.setDieselA("1.5");
        fuelStation1.setNormal95("2.0");

        fuelStation2.setId("222");
        fuelStation2.setRotulo("Cepsa");
        fuelStation2.setCp("11111");
        fuelStation2.setHorario("323232");
        fuelStation2.setDireccion("AABBCC");
        fuelStation2.setMunicipio("COMILLAS");
        fuelStation2.setDieselA("");
        fuelStation2.setNormal95("2.0");

        fuelStation3.setId("333");
        fuelStation3.setRotulo("Repsol");
        fuelStation3.setCp("11111");
        fuelStation3.setHorario("323232");
        fuelStation3.setDireccion("AABBCC");
        fuelStation3.setMunicipio("COMILLAS");
        fuelStation3.setDieselA("1.5");
        fuelStation3.setNormal95("");

        fuelStation4.setId("444");
        fuelStation4.setRotulo("Repsol");
        fuelStation4.setCp("11111");
        fuelStation4.setHorario("323232");
        fuelStation4.setDireccion("AABBCC");
        fuelStation4.setMunicipio("COMILLAS");
        fuelStation4.setDieselA("");
        fuelStation4.setNormal95("");

        fuelStationsList.add(fuelStation1);
        fuelStationsList.add(fuelStation2);
        fuelStationsList.add(fuelStation3);
        fuelStationsList.add(fuelStation4);

        MockitoAnnotations.openMocks(this);
        sut = new MainPresenter(mockMainView);
        when(mockMainView.getGasolineraRepository()).thenReturn(mockFuelStationRepository);
        when(mockFuelStationRepository.getGasolineras()).thenReturn(fuelStationsList);
        sut.init();




    }

    @Test
    public void testFilterByCombustible() {
        sut.filterByCombustible(CombustibleType.DIESEL);
        assertEquals("111", sut.getShownGasolineras().get(0).getId());
        assertEquals("333", sut.getShownGasolineras().get(1).getId());
        assertTrue(2 == sut.getShownGasolineras().size());

        sut.filterByCombustible(CombustibleType.GASOLINA);
        assertEquals("111", sut.getShownGasolineras().get(0).getId());
        assertTrue(1 == sut.getShownGasolineras().size());

        sut.filterByCombustible(CombustibleType.ALL_COMB);
        assertEquals("111", sut.getShownGasolineras().get(0).getId());
        assertEquals("222", sut.getShownGasolineras().get(1).getId());
        assertEquals("333", sut.getShownGasolineras().get(2).getId());
        assertEquals("444", sut.getShownGasolineras().get(3).getId());
        assertTrue(4 == sut.getShownGasolineras().size());

        sut.init();
        sut.filterByCombustible(CombustibleType.GASOLINA);
        assertEquals("111", sut.getShownGasolineras().get(0).getId());
        assertEquals("222", sut.getShownGasolineras().get(1).getId());
        assertTrue(2 == sut.getShownGasolineras().size());

        sut.filterByCombustible(CombustibleType.DIESEL);
        assertEquals("111", sut.getShownGasolineras().get(0).getId());
        assertTrue(1 == sut.getShownGasolineras().size());




    }

}