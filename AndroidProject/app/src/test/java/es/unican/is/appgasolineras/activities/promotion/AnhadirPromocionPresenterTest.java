package es.unican.is.appgasolineras.activities.promotion;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import android.database.sqlite.SQLiteException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Marca;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;
import es.unican.is.appgasolineras.repository.PromocionesRepository;

/**
 * Pruebas unitarias para la clase AnhadirPromocionPresenter.
 */
@RunWith(MockitoJUnitRunner.class)
public class AnhadirPromocionPresenterTest {

    // Objetos Mock
    private IGasolinerasRepository mockGasolinerasRepository;
    private IPromocionesRepository mockPromocionesRepository;
    private IAnhadirPromocionContract.View mockView;

    // SUT
    private IAnhadirPromocionContract.Presenter sut;

    // Lista de gasolineras de prueba
    private List<Gasolinera> gasolineras;

    // Mapas con argumentos que deberia haber pasado la vista con los datos del formulario
    private Map<String, List<String>> infoList; // Mapa para selecciones multiples
    private Map<String, String> infoString; // Mapa para selecciones individuales o inserciones

    @Before
    public void inicializa() {
        // Inicializar mocks
        mockGasolinerasRepository = mock(GasolinerasRepository.class);
        mockPromocionesRepository = mock(PromocionesRepository.class);
        mockView = mock(AnhadirPromocionView.class);

        // Inicializar mapas
        infoList = new HashMap<>();
        infoString = new HashMap<>();

        // Inicializar lista de gasolineras disponibles
        Gasolinera g1 = new Gasolinera("01", "CEPSA", "39526", "CARRETERA 6316 KM. 10,5",
                "Alfoz de Lloredo", "L-D: 08:00-21:00", "1,999", "1,859");
        Gasolinera g2 = new Gasolinera("02", "REPSOL", "39840", "CR N-629 79,7",
                "Ampuero", "L-S: 07:30-21:30; D: 08:30-21:30", "2,009", "1,819");
        Gasolinera g3 = new Gasolinera("03", "PETRONOR", "39450", "CARRETERA N-611 KM. 163,2",
                "Arenas de Iguña", "L-D: 06:00-22:00", "1,969", "1,789");
        Gasolinera g4 = new Gasolinera("04", "CAMPSA", "39195", "CARRETERA ARGOÑOS SOMO KM. 28,7",
                "Arnuero", "L-D: 07:00-22:00", "1,999", "1,819");
        Gasolinera g5 = new Gasolinera("05", "E.S. CARBURANTES DE ARNUERO S.L.", "39195",
                "CARRETERA CASTILLO SIETEVILLAS KM. S\\/N", "Arnuero", "L-D: 07:00-22:30",
                "2,019", "1,829");
        gasolineras = new ArrayList<>();
        gasolineras.add(g1);
        gasolineras.add(g2);
        gasolineras.add(g3);
        gasolineras.add(g4);
        gasolineras.add(g5);

        // Comportamiento del mock "Gasolineras Repository"
        when(mockGasolinerasRepository.getGasolineras()).thenReturn(gasolineras);
        when(mockGasolinerasRepository.getGasolineraByNameDirLocalidad("CEPSA", "CARRETERA 6316 KM. 10,5",
                "Alfoz de Lloredo")).thenReturn(g1);
        when(mockGasolinerasRepository.getGasolineraByNameDirLocalidad("REPSOL", "CR N-629 79,7",
                "Ampuero")).thenReturn(g2);

        // Comportamiento del mock "Anhadir Promocion View"
        when(mockView.getGasolineraRepository()).thenReturn(mockGasolinerasRepository);
        when(mockView.getPromocionRepository()).thenReturn(mockPromocionesRepository);

        // Inicializar SUT
        sut = new AnhadirPromocionPresenter(mockView);
        sut.init();
    }

    @Test
    public void testOnAnhadirClicked() {
        // UT-1A
        anhadirExitoPorcentualTodasGasolineras();

        // UT-1B
        anhadirExitoPorcentualGasolinera();

        // UT-1C
        anhadirExitoPorcentualMarcas();

        // UT-1D
        anhadirExitoEurosLitroTodasGasolineras();

        // UT-1E
        anhadirExitoEurosLitroGasolinera();

        // UT-1F
        anhadirExitoEurosLitroMarcas();

        // UT-1G
        anhadirPromocionRepetida();

        // UT-1H
        anhadirPromocionSinCombSeleccionado();

        // UT-1I
        anhadirPromocionSinGasolinerasSeleccionadas();

        // UT-1J
        anhadirPromocionSinDescuento();

        // UT-1K & UT-1L
        anhadirPromocionPorcentajeNoValido();

        // UT-1M
        anhadirPromocionEurosLitroNoValido();

        // UT-1N
        anhadirPromocionBDError();
    }

    /**
     * Comprueba que se anhade correctamente una promocion con descuento porcentual para todas
     * las gasolineras.
     *
     * UT-1A
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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P01")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        Promocion p = new Promocion("P01", 5.0, -1.0, "Diésel");
        verify(mockPromocionesRepository, times(1)).getPromocionById("P01");
        verify(mockPromocionesRepository, times(1)).insertPromocion(p);
        verify(mockPromocionesRepository, times(5)).insertRelacionGasolineraPromocion(any(), any());

        verify(mockGasolinerasRepository, times(1)).getGasolineras();
    }

    /**
     * Comprueba que se anhade correctamente una promocion con descuento porcentual para una
     * gasolinera especifica.
     *
     * UT-1B
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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P02")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(2)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        Promocion p = new Promocion("P02", 5.0, -1.0, "Diésel");
        verify(mockPromocionesRepository, times(1)).getPromocionById("P02");
        verify(mockPromocionesRepository, times(1)).insertPromocion(p);
        verify(mockPromocionesRepository, times(1)).insertRelacionGasolineraPromocion(gasolineras.get(0),
                p);
        verify(mockGasolinerasRepository, times(1)).getGasolineraByNameDirLocalidad("CEPSA",
                "CARRETERA 6316 KM. 10,5", "Alfoz de Lloredo");
    }

    /**
     * Comprueba que se anhade correctamente una promocion con descuento porcentual para unas
     * marcas determinadas.
     *
     * UT-1C 
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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P03")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(3)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        Promocion p = new Promocion("P03", 10.0, -1.0, "Diésel-Gasolina");
        verify(mockPromocionesRepository, times(1)).getPromocionById("P03");
        verify(mockPromocionesRepository, times(1)).insertPromocion(p);
        verify(mockPromocionesRepository, times(8)).insertRelacionGasolineraPromocion(any(), any());
        verify(mockPromocionesRepository, times(1)).insertRelacionMarcaPromocion(new Marca("Cepsa"),
                p);
        verify(mockPromocionesRepository, times(1)).insertRelacionMarcaPromocion(new Marca("Repsol"),
                p);

        verify(mockGasolinerasRepository, times(2)).getGasolineras();
    }

    /**
     * Comprueba que se anhade correctamente una promocion con descuento €/L para todas las
     * gasolineras.
     *
     * UT-1D
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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P04")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(4)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        Promocion p = new Promocion("P04", -1.0, 0.2, "Gasolina");
        verify(mockPromocionesRepository, times(1)).getPromocionById("P04");
        verify(mockPromocionesRepository, times(1)).insertPromocion(p);
        verify(mockPromocionesRepository, times(13)).insertRelacionGasolineraPromocion(any(), any());

        verify(mockGasolinerasRepository, times(3)).getGasolineras();
    }

    /**
     * Comprueba que se anhade correctamente una promocion con descuento €/L para una gasolinera
     * determinada.
     *
     * UT-1E
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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P05")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(5)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        Promocion p = new Promocion("P05", -1.0, 0.25, "Diésel");
        verify(mockPromocionesRepository, times(1)).getPromocionById("P05");
        verify(mockPromocionesRepository, times(1)).insertPromocion(p);
        verify(mockPromocionesRepository, times(1)).insertRelacionGasolineraPromocion(gasolineras.get(1),
                p);
        verify(mockGasolinerasRepository, times(1)).getGasolineraByNameDirLocalidad("REPSOL",
                "CR N-629 79,7", "Ampuero");
    }

    /**
     * Comprueba que se anhade correctamente una promocion con descuento €/L para un conjunto
     * de marcas.
     *
     * UT-1F
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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P06")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(6)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        Promocion p = new Promocion("P06", -1.0, 0.25, "Gasolina");
        verify(mockPromocionesRepository, times(1)).getPromocionById("P06");
        verify(mockPromocionesRepository, times(1)).insertPromocion(p);
        verify(mockPromocionesRepository, times(16)).insertRelacionGasolineraPromocion(any(), any());
        verify(mockPromocionesRepository, times(1)).insertRelacionMarcaPromocion(new Marca("Cepsa"),
                p);
        verify(mockPromocionesRepository, times(1)).insertRelacionMarcaPromocion(new Marca("Repsol"),
                p);

        verify(mockGasolinerasRepository, times(4)).getGasolineras();
    }

    /**
     * Comprueba que no se anhade una promocion cuando esta repetida.
     *
     * UT-1G
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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P01")).thenReturn(new Promocion("P01",
                5.0, -1.0, "Diésel"));

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.REPETIDA);
        verify(mockPromocionesRepository, times(2)).getPromocionById("P01");
    }

    /**
     * Comprueba que no se anhade una promocion cuando no se ha indicado un combustible.
     *
     * UT-1H
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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P08")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.SIN_COMB);

        verify(mockPromocionesRepository, times(1)).getPromocionById("P08");
    }

    /**
     * Comprueba que no se anhade una promocion si no se indica una marca al seleccionar
     * gasolineras por marca.
     *
     * UT-1I
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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P09")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.SIN_GASOLINERA);

        verify(mockPromocionesRepository, times(1)).getPromocionById("P09");
    }

    /**
     * Comprueba que no se anhade una promocion si no se indica un valor de descuento.
     *
     * UT-1J
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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P10")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.SIN_DESC);

        verify(mockPromocionesRepository, times(1)).getPromocionById("P10");
    }

    /**
     * Comprueba que no se anhade una promocion si no se indica un valor de descuento porcentual
     * valido.
     *
     * UT-1K & UT-1L
     */
    private void anhadirPromocionPorcentajeNoValido() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

        combustibles.add("Gasolina");

        /*
        UT-1K
         */
        infoList.put("selectedCombustibles", combustibles);
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P11");
        infoString.put("selectedCriterio", "Todas");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "120");
        infoString.put("selectedDescuentoTipo", "%");
        infoString.put("stringValueAllGas", "Todas");

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P11")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.PORC_NO_VALIDO);

        verify(mockPromocionesRepository, times(1)).getPromocionById("P11");

        /*
        UT-1L
         */
        infoList.put("selectedCombustibles", combustibles);
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P07");
        infoString.put("selectedCriterio", "Todas");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "-5");
        infoString.put("selectedDescuentoTipo", "%");
        infoString.put("stringValueAllGas", "Todas");

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P07")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(2)).showStatus(EstadoOperacionAnhadirPromocion.PORC_NO_VALIDO);

        verify(mockPromocionesRepository, times(1)).getPromocionById("P07");
    }

    /**
     * Comprueba que no se anhade una promocion si no se indica un valor de descuento euros/litro
     * valido.
     *
     * UT-1M
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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P12")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.EURO_L_NO_VALIDO);

        verify(mockPromocionesRepository, times(1)).getPromocionById("P12");
    }

    /**
     * Comprueba que no se anhade una promocion si ocurre un error con la Base de Datos.
     *
     * UT-1N
     */
    private void anhadirPromocionBDError() {
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

        combustibles.add("Diésel");

        infoList.put("selectedCombustibles", combustibles);
        infoList.put("selectedMarcas", marcas);

        infoString.put("idPromocion", "P13");
        infoString.put("selectedCriterio", "Todas");
        infoString.put("selectedGasolinera", "-");
        infoString.put("descuento", "20");
        infoString.put("selectedDescuentoTipo", "%");
        infoString.put("stringValueAllGas", "Todas");

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P13")).thenReturn(null);
        Promocion p = new Promocion("P13", 20, -1.0, "Diésel");
        doThrow(new SQLiteException()).when(mockPromocionesRepository).insertRelacionGasolineraPromocion(
                gasolineras.get(0), p); // Excepcion a la hora de añadir una gasolinera determinada

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.ERROR_BD);

        verify(mockPromocionesRepository, times(1)).getPromocionById("P13");
        verify(mockPromocionesRepository, times(1)).deletePromocion(p);
    }
}
