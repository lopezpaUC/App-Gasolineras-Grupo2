package es.unican.is.appgasolineras.activities.promotion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;
import es.unican.is.appgasolineras.repository.PromocionesRepository;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
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
        GasolineraDatabase db = GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext());
        db.close();
    }

    @AfterClass
    public static void end() {
        GasolinerasService.resetAPI();
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Test
    public void testOnAnhadirClicked() {

        // IT-1A
        anhadirExitoPorcentualTodasGasolineras();

        // IT-1B
        anhadirExitoPorcentualGasolinera();

        // IT-1C
        anhadirExitoPorcentualMarcas();

        // IT-1D
        anhadirExitoEurosLitroTodasGasolineras();

        // IT-1E
        anhadirExitoEurosLitroGasolinera();

        // IT-1F
        anhadirExitoEurosLitroMarcas();

        // IT-1G
        anhadirPromocionRepetida();

        // IT-1H
        anhadirPromocionSinCombSeleccionado();

        // IT-1I
        anhadirPromocionSinGasolinerasSeleccionadas();

        // IT-1J
        anhadirPromocionSinDescuento();

        // IT-1K & IT-1L
        anhadirPromocionPorcentajeNoValido();

        // IT-1M
        anhadirPromocionEurosLitroNoValido();

    }

    /**
     * Comprueba que se anhade correctamente una promocion con descuento porcentual para todas
     * las gasolineras.
     *
     * IT-1A
     */
    private void anhadirExitoPorcentualTodasGasolineras() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

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

        // Verifica persistencia en BD
        Promocion p = new Promocion("P01", 5, -1.0, "Diésel");
        assertEquals(p, repPromociones.getPromocionById("P01"));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1039").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1048").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1036").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1080").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1095").contains(p));
    }

    /**
     * Comprueba que se anhade correctamente una promocion con descuento porcentual para una
     * gasolinera especifica.
     *
     * IT-1B
     */
    private void anhadirExitoPorcentualGasolinera() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

        combustibles.add("Diésel");

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

        // Verifica persistencia en BD
        Promocion p = new Promocion("P02", 5, -1.0, "Diésel");
        assertEquals(p, repPromociones.getPromocionById("P02"));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1039").contains(p));
        assertFalse(repPromociones.getPromocionesRelacionadasConGasolinera("1048").contains(p));
        assertFalse(repPromociones.getPromocionesRelacionadasConGasolinera("1036").contains(p));
        assertFalse(repPromociones.getPromocionesRelacionadasConGasolinera("1080").contains(p));
        assertFalse(repPromociones.getPromocionesRelacionadasConGasolinera("1095").contains(p));
    }

    /**
     * Comprueba que se anhade correctamente una promocion con descuento porcentual para unas
     * marcas determinadas.
     *
     * IT-1C
     */
    private void anhadirExitoPorcentualMarcas() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

        combustibles.add("Diésel");
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

        // Verifica persistencia en BD
        Promocion p = new Promocion("P03", 10, -1.0, "Diésel-Gasolina");
        assertEquals(p, repPromociones.getPromocionById("P03"));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1039").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1048").contains(p));
        assertFalse(repPromociones.getPromocionesRelacionadasConGasolinera("1036").contains(p));
        assertFalse(repPromociones.getPromocionesRelacionadasConGasolinera("1080").contains(p));
        assertFalse(repPromociones.getPromocionesRelacionadasConGasolinera("1095").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConMarca("Cepsa").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConMarca("Repsol").contains(p));
    }

    /**
     * Comprueba que se anhade correctamente una promocion con descuento €/L para todas las
     * gasolineras.
     *
     * IT-1D
     */
    private void anhadirExitoEurosLitroTodasGasolineras() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();
        combustibles.add("Gasolina");

        infoList.put("selectedCombustibles", combustibles);
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P04");
        infoString.put("selectedCriterio", "Todas");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "0.2");
        infoString.put("selectedDescuentoTipo", "€/L");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(4)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        // Verifica persistencia en BD
        Promocion p = new Promocion("P04", -1.0, 0.2, "Gasolina");
        assertEquals(p, repPromociones.getPromocionById("P04"));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1039").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1048").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1036").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1080").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1095").contains(p));
    }

    /**
     * Comprueba que se anhade correctamente una promocion con descuento €/L para una gasolinera
     * determinada.
     *
     * IT-1E
     */
    private void anhadirExitoEurosLitroGasolinera() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

        combustibles.add("Diésel");

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

        // Verifica persistencia en BD
        Promocion p = new Promocion("P05", -1.0, 0.25, "Diésel");
        assertEquals(p, repPromociones.getPromocionById("P05"));
        assertFalse(repPromociones.getPromocionesRelacionadasConGasolinera("1039").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1048").contains(p));
        assertFalse(repPromociones.getPromocionesRelacionadasConGasolinera("1036").contains(p));
        assertFalse(repPromociones.getPromocionesRelacionadasConGasolinera("1080").contains(p));
        assertFalse(repPromociones.getPromocionesRelacionadasConGasolinera("1095").contains(p));
    }

    /**
     * Comprueba que se anhade correctamente una promocion con descuento €/L para un conjunto
     * de marcas.
     *
     * IT-1F
     */
    private void anhadirExitoEurosLitroMarcas() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

        combustibles.add("Gasolina");

        infoList.put("selectedCombustibles", combustibles);
        marcas.add("Cepsa");
        marcas.add("Repsol");
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P06");
        infoString.put("selectedCriterio", "Por marca");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "0.25");
        infoString.put("selectedDescuentoTipo", "€/L");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(6)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        // Verifica persistencia en BD
        Promocion p = new Promocion("P06", -1.0, 0.25, "Gasolina");
        assertEquals(p, repPromociones.getPromocionById("P06"));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1039").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConGasolinera("1048").contains(p));
        assertFalse(repPromociones.getPromocionesRelacionadasConGasolinera("1036").contains(p));
        assertFalse(repPromociones.getPromocionesRelacionadasConGasolinera("1080").contains(p));
        assertFalse(repPromociones.getPromocionesRelacionadasConGasolinera("1095").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConMarca("Cepsa").contains(p));
        assertTrue(repPromociones.getPromocionesRelacionadasConMarca("Repsol").contains(p));
    }

    /**
     * Comprueba que no se anhade una promocion cuando esta repetida (repetida si tienen mismo ID).
     *
     * IT-1G
     */
    private void anhadirPromocionRepetida() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

        combustibles.add("Gasolina");

        infoList.put("selectedCombustibles", combustibles);
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P01");
        infoString.put("selectedCriterio", "Todas");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "10");
        infoString.put("selectedDescuentoTipo", "%");
        infoString.put("stringValueAllGas", "Todas");

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.REPETIDA);

        // Verifica persistencia en BD
        Promocion p = new Promocion("P01", 5, -1.0, "Diésel");
        assertEquals(p, repPromociones.getPromocionById("P01")); // Misma promocion que la inicial
    }

    /**
     * Comprueba que no se anhade una promocion cuando no se ha indicado un combustible.
     *
     * IT-1H
     */
    private void anhadirPromocionSinCombSeleccionado() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

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

        assertNull(repPromociones.getPromocionById("P08"));
    }

    /**
     * Comprueba que no se anhade una promocion si no se indica una marca al seleccionar
     * gasolineras por marca.
     *
     * IT-1I
     */
    private void anhadirPromocionSinGasolinerasSeleccionadas() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

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

        assertNull(repPromociones.getPromocionById("P09"));
    }

    /**
     * Comprueba que no se anhade una promocion si no se indica un valor de descuento.
     *
     * IT-1J
     */
    private void anhadirPromocionSinDescuento() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

        combustibles.add("Diésel");

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

        assertNull(repPromociones.getPromocionById("P10"));
    }

    /**
     * Comprueba que no se anhade una promocion si no se indica un valor de descuento porcentual
     * valido.
     *
     * IT-1K & IT-1L
     */
    private void anhadirPromocionPorcentajeNoValido() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

        combustibles.add("Gasolina");

        /*
        IT-1K
         */
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

        assertNull(repPromociones.getPromocionById("P11"));

        /*
        IT-1L
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

        assertNull(repPromociones.getPromocionById("P07"));
    }

    /**
     * Comprueba que no se anhade una promocion si no se indica un valor de descuento euros/litro
     * valido.
     *
     * IT-1M
     */
    private void anhadirPromocionEurosLitroNoValido() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

        combustibles.add("Gasolina");

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

        assertNull(repPromociones.getPromocionById("P12"));
    }

}
