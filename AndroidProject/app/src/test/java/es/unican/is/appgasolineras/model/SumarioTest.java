package es.unican.is.appgasolineras.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

import es.unican.is.appgasolineras.activities.detail.GasolineraDetailPresenter;
import es.unican.is.appgasolineras.activities.detail.IGasolineraDetailContract;


@RunWith(MockitoJUnitRunner.class)

public class SumarioTest {

    @Mock
    IGasolineraDetailContract.View mockDetailContract;

    @Test
    public void calculaSumarioTest() {
        Gasolinera gasolinera = new Gasolinera();
        gasolinera.setId("123");
        gasolinera.setRotulo("shell");
        gasolinera.setCp("39234");
        gasolinera.setHorario("323232");
        gasolinera.setDireccion("dfdf");
        gasolinera.setMunicipio("erwer");

        gasolinera.setDieselA("1");
        gasolinera.setNormal95("3");
        GasolineraDetailPresenter sut = new GasolineraDetailPresenter(mockDetailContract, gasolinera);
        sut.init();
        String precioSumario = sut.getPrecioSumario();
        Assert.assertEquals("2,333", precioSumario);

        gasolinera.setDieselA("-1");
        gasolinera.setNormal95("-3,5");
        sut.init();
        precioSumario = sut.getPrecioSumario();
        Assert.assertEquals("0,000", precioSumario);

        gasolinera.setDieselA("0");
        gasolinera.setNormal95("3.0");
        sut.init();
        precioSumario = sut.getPrecioSumario();
        Assert.assertEquals("3,000", precioSumario);
    }
}

