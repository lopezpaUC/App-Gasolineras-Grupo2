package es.unican.is.appgasolineras.activities.detail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;


@RunWith(MockitoJUnitRunner.class)

public class SumarioTest {

    @Mock
    private IGasolineraDetailContract.View mockDetailView;

    @Mock
    private IGasolinerasRepository mockGasolinerasRepository;

    @Mock
    private IPromocionesRepository mockPromocionesRepository;

    private Gasolinera gasolinera;
    private GasolineraDetailPresenter sut;
    private NumberFormat format;

    @Before
    public void setUp() {
        gasolinera = new Gasolinera();
        gasolinera.setId("123");
        gasolinera.setRotulo("shell");
        gasolinera.setCp("39234");
        gasolinera.setHorario("323232");
        gasolinera.setDireccion("dfdf");
        gasolinera.setMunicipio("erwer");

        gasolinera.setDieselA("1");
        gasolinera.setNormal95("3");

        format = NumberFormat.getInstance(Locale.FRANCE);

        when (mockDetailView.getGasolinerasRepository()).thenReturn(mockGasolinerasRepository);
        when (mockDetailView.getPromocionesRepository()).thenReturn(mockPromocionesRepository);
        sut = new GasolineraDetailPresenter(mockDetailView, gasolinera);
        /*when (mockGasolinerasRepository.precioToDouble(anyString(), any())).thenReturn(2.333);
        when (mockGasolinerasRepository.calculateSummary(anyDouble(), anyDouble())).thenReturn(2.333);
        when (mockGasolinerasRepository.precioSumarioToStr(anyDouble())).thenReturn("2,33");
        */
        when (mockPromocionesRepository.getPromocionesRelacionadasConGasolinera(any())).thenReturn(
                new ArrayList<>());
        sut.init();
    }

    @Test
    public void calculaSumarioTest() {
        String precioSumario = sut.getPrecioSumario();
        Assert.assertEquals("2,33", precioSumario);

        gasolinera.setDieselA("-1");
        gasolinera.setNormal95("-3,5");
        /*when (mockGasolinerasRepository.precioToDouble(anyString(), any())).thenReturn(-1.0);
        when (mockGasolinerasRepository.calculateSummary(anyDouble(), anyDouble())).thenReturn(-1.0);
        when (mockGasolinerasRepository.precioSumarioToStr(anyDouble())).thenReturn("-");
        */
        sut.init();

        precioSumario = sut.getPrecioSumario();
        Assert.assertEquals("-", precioSumario);

        gasolinera.setDieselA("0");
        gasolinera.setNormal95("3.0");
        /*when (mockGasolinerasRepository.precioToDouble(anyString(), any())).thenReturn(3.0);
        when (mockGasolinerasRepository.calculateSummary(anyDouble(), anyDouble())).thenReturn(3.0);
        when (mockGasolinerasRepository.precioSumarioToStr(anyDouble())).thenReturn("3,00");
        */sut.init();

        precioSumario = sut.getPrecioSumario();
        Assert.assertEquals("3,00", precioSumario);

        gasolinera.setDieselA("5.0");
        gasolinera.setNormal95("0.0");
        /*when (mockGasolinerasRepository.precioToDouble(anyString(), any())).thenReturn(5.0);
        when (mockGasolinerasRepository.calculateSummary(anyDouble(), anyDouble())).thenReturn(5.0);
        when (mockGasolinerasRepository.precioSumarioToStr(anyDouble())).thenReturn("5,00");
*/
        sut.init();

        precioSumario = sut.getPrecioSumario();
        Assert.assertEquals("5,00", precioSumario);
    }
}

