package es.unican.is.appgasolineras.activities.promotion;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)

public class AnhadirPromocionPresenterITest {
    // Objetos Mock
    private IAnhadirPromocionContract.View mockView;

    // Repositorios
    private IPromocionesRepository repPromociones;
    private IGasolinerasRepository repGasolineras;

    // SUT
    private IAnhadirPromocionContract.Presenter sut;

    // Mapas con argumentos que deberia haber pasado la vista
    private Map<String, List<String>> infoList;
    private Map<String, String> infoString;

    @BeforeClass
    public static void init() {
        GasolinerasServiceConstants.setStaticURL3();
    }

    @Before
    public void inicializaAtributos() {
        mockView = mock(AnhadirPromocionView.class);
        when(mockView.getPromocionRepository()).thenReturn(repPromociones);
        when(mockView.getGasolineraRepository()).thenReturn(repGasolineras);

        repPromociones = mockView.getPromocionRepository();
        repGasolineras = mockView.getGasolineraRepository();

        infoList = new HashMap<>();
        infoString = new HashMap<>();

        sut = new AnhadirPromocionPresenter(mockView);
        sut.init();
    }

    @After
    public void cleanDatabase() {
        repPromociones.deleteAllPromociones();
    }

    @AfterClass
    public static void end() {
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Test
    public void testOnAnhadirClicked() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();
        combustibles.add("Diésel");

        /*Descuento porcentual en todas las gasolineras
          CASO 1
         */
        // Preparar argumentos de entrada
        infoList.put("selectedCombustibles", combustibles);
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P01");
        infoString.put("selectedCriterio", "Todas");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "5");
        infoString.put("selectedDescuentoTipo", "%");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);
        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);


    }


}
