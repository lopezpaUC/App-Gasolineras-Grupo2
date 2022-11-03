package es.unican.is.appgasolineras.activities.promotion;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

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

import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;
import es.unican.is.appgasolineras.repository.PromocionesRepository;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

/**
 * Pruebas de integracion para la clase AnhadirPromocionPresenter.
 */
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

    // Mapas con argumentos que deberia haber pasado la vista como argumento
    private Map<String, List<String>> infoList;
    private Map<String, String> infoString;

    @BeforeClass
    public static void init() {
        GasolinerasServiceConstants.setStaticURL3();
    }

    @Before
    public void inicializaAtributos() {
        Context context = ApplicationProvider.getApplicationContext();

        // Utiliza objetos repositorio reales
        repGasolineras = new GasolinerasRepository(context);
        repPromociones = new PromocionesRepository(context);

        mockView = mock(AnhadirPromocionView.class);
        when(mockView.getPromocionRepository()).thenReturn(repPromociones);
        when(mockView.getGasolineraRepository()).thenReturn(repGasolineras);

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
        GasolinerasService.resetAPI();
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Test
    public void testOnAnhadirClicked() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

        /*Descuento porcentual en todas las gasolineras
          CASO 1
         */
        // Preparar argumentos de entrada
        combustibles.add("Diésel");
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

        /*
          Descuento porcentual en una gasolinera especifica.
          Caso 02
         */
        // Preparar argumentos de entrada
        infoList.put("selectedCombustibles", combustibles);
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P02");
        infoString.put("selectedCriterio", "Por gasolinera");
        infoString.put("selectedGasolinera", "CEPSA // CARRETERA 6316 KM. 10,5 // Alfoz de Lloredo");
        infoString.put("descuento", "5");
        infoString.put("selectedDescuentoTipo", "%");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(2)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        /*
          Descuento porcentual por marca.
          Caso 03
         */
        // Preparar argumentos de entrada
        combustibles.add("Gasolina");
        infoList.put("selectedCombustibles", combustibles);
        marcas.add("Cepsa");
        marcas.add("Repsol");
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P03");
        infoString.put("selectedCriterio", "Por marca");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "10");
        infoString.put("selectedDescuentoTipo", "%");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(3)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        /*Descuento €/L en todas las gasolineras
          CASO 4
         */
        // Preparar argumentos de entrada
        combustibles.remove("Diésel");
        infoList.put("selectedCombustibles", combustibles);
        marcas.clear();
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P04");
        infoString.put("selectedCriterio", "Todas");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "0.2");
        infoString.put("selectedDescuentoTipo", "€/L");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(4)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        /*
          Descuento €/L en una gasolinera especifica.
          Caso 05
         */
        // Preparar argumentos de entrada
        combustibles.add("Diésel");
        combustibles.remove("Gasolina");
        infoList.put("selectedCombustibles", combustibles);
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P05");
        infoString.put("selectedCriterio", "Por gasolinera");
        infoString.put("selectedGasolinera", "REPSOL // CR N-629 79,7 // Ampuero");
        infoString.put("descuento", "0.25");
        infoString.put("selectedDescuentoTipo", "€/L");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(5)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        /*
          Descuento €/L por marca.
          Caso 06
         */
        // Preparar argumentos de entrada
        combustibles.add("Gasolina");
        combustibles.remove("Diésel");
        infoList.put("selectedCombustibles", combustibles);
        marcas.add("Cepsa");
        marcas.add("Repsol");
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P06");
        infoString.put("selectedCriterio", "Por marca");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "10");
        infoString.put("selectedDescuentoTipo", "€/L");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(6)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        /*
          Promocion repetida
          Caso 07-G
         */
        // Preparar argumentos de entrada
        infoList.put("selectedCombustibles", combustibles);
        marcas.clear();
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P01");
        infoString.put("selectedCriterio", "Todas");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "5");
        infoString.put("selectedDescuentoTipo", "%");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.REPETIDA);

        /*
          Sin combustible indicado
          Caso 08-H
         */
        // Preparar argumentos de entrada
        combustibles.clear();
        infoList.put("selectedCombustibles", combustibles);
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P08");
        infoString.put("selectedCriterio", "Todas");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "10");
        infoString.put("selectedDescuentoTipo", "%");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.SIN_COMB);

        /*
          Sin gasolineras indicadas
          Caso 09-I
         */
        // Preparar argumentos de entrada
        combustibles.add("Gasolina");
        combustibles.add("Diésel");
        infoList.put("selectedCombustibles", combustibles);
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P09");
        infoString.put("selectedCriterio", "Por marca");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "10");
        infoString.put("selectedDescuentoTipo", "%");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.SIN_GASOLINERA);

        /*
          Sin descuento indicado
          Caso 10-J
         */
        // Preparar argumentos de entrada
        combustibles.remove("Gasolina");
        infoList.put("selectedCombustibles", combustibles);
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P10");
        infoString.put("selectedCriterio", "Todas");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "");
        infoString.put("selectedDescuentoTipo", "%");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.SIN_DESC);

        /*
          Porcentaje no valido
          Caso 11-K
         */
        combustibles.remove("Diésel");
        combustibles.add("Gasolina");
        infoList.put("selectedCombustibles", combustibles);
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P11");
        infoString.put("selectedCriterio", "Todas");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "120");
        infoString.put("selectedDescuentoTipo", "%");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.PORC_NO_VALIDO);

        /*
          Porcentaje no valido
          Caso 11-L
         */
        infoList.put("selectedCombustibles", combustibles);
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P07");
        infoString.put("selectedCriterio", "Todas");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "-5");
        infoString.put("selectedDescuentoTipo", "%");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(2)).showStatus(EstadoOperacionAnhadirPromocion.PORC_NO_VALIDO);

        /*
          Porcentaje no valido
          Caso 12-M
         */
        infoList.put("selectedCombustibles", combustibles);
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P12");
        infoString.put("selectedCriterio", "Todas");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "-1");
        infoString.put("selectedDescuentoTipo", "€/L");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.EURO_L_NO_VALIDO);

    }


}
