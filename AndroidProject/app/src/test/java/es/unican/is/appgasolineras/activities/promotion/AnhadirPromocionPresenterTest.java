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
        List<String> combustibles = new ArrayList<>();
        List<String> marcas = new ArrayList<>();

        /*Descuento porcentual en todas las gasolineras
          CASO 1-A
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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P01")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        Promocion p = new Promocion("P01", 5.0, -1.0, "Diésel");
        verify(mockPromocionesRepository, times(1)).getPromocionById("P01");
        verify(mockPromocionesRepository, times(1)).insertPromocion(p);
        verify(mockPromocionesRepository, times(5)).insertRelacionGasolineraPromocion(any(), any());

        verify(mockGasolinerasRepository, times(1)).getGasolineras();

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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P02")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(2)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        p = new Promocion("P02", 5.0, -1.0, "Diésel");
        verify(mockPromocionesRepository, times(1)).getPromocionById("P02");
        verify(mockPromocionesRepository, times(1)).insertPromocion(p);
        verify(mockPromocionesRepository, times(1)).insertRelacionGasolineraPromocion(gasolineras.get(0),
                p);
        verify(mockGasolinerasRepository, times(1)).getGasolineraByNameDirLocalidad("CEPSA",
                "CARRETERA 6316 KM. 10,5", "Alfoz de Lloredo");

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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P03")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(3)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        p = new Promocion("P03", 10.0, -1.0, "Diésel-Gasolina");
        verify(mockPromocionesRepository, times(1)).getPromocionById("P03");
        verify(mockPromocionesRepository, times(1)).insertPromocion(p);
        verify(mockPromocionesRepository, times(8)).insertRelacionGasolineraPromocion(any(), any());
        verify(mockPromocionesRepository, times(1)).insertRelacionMarcaPromocion(new Marca("Cepsa"),
                p);
        verify(mockPromocionesRepository, times(1)).insertRelacionMarcaPromocion(new Marca("Repsol"),
                p);

        verify(mockGasolinerasRepository, times(2)).getGasolineras();

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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P04")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(4)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        p = new Promocion("P04", -1.0, 0.2, "Gasolina");
        verify(mockPromocionesRepository, times(1)).getPromocionById("P04");
        verify(mockPromocionesRepository, times(1)).insertPromocion(p);
        verify(mockPromocionesRepository, times(13)).insertRelacionGasolineraPromocion(any(), any());

        verify(mockGasolinerasRepository, times(3)).getGasolineras();


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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P05")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(5)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        p = new Promocion("P05", -1.0, 0.25, "Diésel");
        verify(mockPromocionesRepository, times(1)).getPromocionById("P05");
        verify(mockPromocionesRepository, times(1)).insertPromocion(p);
        verify(mockPromocionesRepository, times(1)).insertRelacionGasolineraPromocion(gasolineras.get(1),
                p);
        verify(mockGasolinerasRepository, times(1)).getGasolineraByNameDirLocalidad("REPSOL",
                "CR N-629 79,7", "Ampuero");

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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P06")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(6)).showStatus(EstadoOperacionAnhadirPromocion.EXITO);

        p = new Promocion("P06", -1.0, 10, "Gasolina");
        verify(mockPromocionesRepository, times(1)).getPromocionById("P06");
        verify(mockPromocionesRepository, times(1)).insertPromocion(p);
        verify(mockPromocionesRepository, times(16)).insertRelacionGasolineraPromocion(any(), any());
        verify(mockPromocionesRepository, times(1)).insertRelacionMarcaPromocion(new Marca("Cepsa"),
                p);
        verify(mockPromocionesRepository, times(1)).insertRelacionMarcaPromocion(new Marca("Repsol"),
                p);

        verify(mockGasolinerasRepository, times(4)).getGasolineras();


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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P01")).thenReturn(new Promocion("P01",
                5.0, -1.0, "Diésel"));

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.REPETIDA);
        verify(mockPromocionesRepository, times(2)).getPromocionById("P01");

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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P08")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.SIN_COMB);

        verify(mockPromocionesRepository, times(1)).getPromocionById("P08");

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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P09")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.SIN_GASOLINERA);

        verify(mockPromocionesRepository, times(1)).getPromocionById("P09");

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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P10")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.SIN_DESC);

        verify(mockPromocionesRepository, times(1)).getPromocionById("P10");

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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P11")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.PORC_NO_VALIDO);

        verify(mockPromocionesRepository, times(1)).getPromocionById("P11");

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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P07")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(2)).showStatus(EstadoOperacionAnhadirPromocion.PORC_NO_VALIDO);

        verify(mockPromocionesRepository, times(1)).getPromocionById("P07");

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

        // Definir comportamiento del mock "Promociones Repository"
        when(mockPromocionesRepository.getPromocionById("P12")).thenReturn(null);

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.EURO_L_NO_VALIDO);

        verify(mockPromocionesRepository, times(1)).getPromocionById("P12");

        /*
          Porcentaje no valido
          Caso 13-N
         */
        combustibles.remove("Gasolina");
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
        p = new Promocion("P13", 20, -1.0, "Diésel");
        doThrow(new SQLiteException()).when(mockPromocionesRepository).insertRelacionGasolineraPromocion(
                gasolineras.get(0), p); // Excepcion a la hora de añadir una gasolinera determinada

        sut.onAnhadirClicked(infoList, infoString);

        verify(mockView, times(1)).showStatus(EstadoOperacionAnhadirPromocion.ERROR_BD);

        verify(mockPromocionesRepository, times(1)).getPromocionById("P13");
        verify(mockPromocionesRepository, times(1)).deletePromocion(p);

        return;
    }

}
