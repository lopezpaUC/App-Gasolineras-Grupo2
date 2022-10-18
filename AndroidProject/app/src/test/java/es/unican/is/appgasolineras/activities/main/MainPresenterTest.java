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

    private MainPresenter sut;
    private List<String> brandsList = new ArrayList<>();
    private List<Gasolinera> fuelStationsList = new ArrayList<>();

    private Gasolinera fuelStation1 = new Gasolinera();
    private Gasolinera fuelStation2 = new Gasolinera();
    private Gasolinera fuelStation3 = new Gasolinera();
    private Gasolinera fuelStation4 = new Gasolinera();

    @Mock
    IMainContract.View mockMainPresenter;
    @Mock
    IGasolinerasRepository mockFuelStationRepository;

    @Before
    public void inicializa(){
        MockitoAnnotations.openMocks(this);
        sut = new MainPresenter(mockMainPresenter);
        sut.init();

        fuelStation1.setId("111");
        fuelStation1.setRotulo("CEMPSA");
        fuelStation1.setCp("11111");
        fuelStation1.setHorario("323232");
        fuelStation1.setDireccion("AABBCC");
        fuelStation1.setMunicipio("COMILLAS");
        fuelStation1.setDieselA("1.5");
        fuelStation1.setNormal95("2.0");

        fuelStation2.setId("222");
        fuelStation2.setRotulo("CEMPSA");
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

        fuelStationsList.add(fuelStation1);
        fuelStationsList.add(fuelStation2);
        fuelStationsList.add(fuelStation3);
        fuelStationsList.add(fuelStation4);

        when(mockFuelStationRepository.getGasolineras()).thenReturn(fuelStationsList);

        brandsList.add("CEMPSA");
    }

    @Test
    public void testFilterByBrand() {
        // sut.filterByBrand();
        /* Debería retornar la lista ?? */
    }

}
