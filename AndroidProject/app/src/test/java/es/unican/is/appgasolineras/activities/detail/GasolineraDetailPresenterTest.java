package es.unican.is.appgasolineras.activities.detail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

@RunWith(MockitoJUnitRunner.class)

public class GasolineraDetailPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private IGasolineraDetailContract.View mockDetailView;

    @Mock
    private IGasolinerasRepository mockGasolinerasRepository;

    @Mock
    private IPromocionesRepository mockPromocionesRepository;

    private Gasolinera gasolinera1;
    private Gasolinera gasolinera2;
    private Promocion promotion;
    private GasolineraDetailPresenter sut;

    private NumberFormat format;
    private List<Promocion> promotions;

    @Before
    public void setUp() {
        /* Sumario Sin Descuento */
        gasolinera1 = new Gasolinera();
        gasolinera1.setId("123");
        gasolinera1.setRotulo("shell");
        gasolinera1.setCp("39234");
        gasolinera1.setHorario("323232");
        gasolinera1.setDireccion("dfdf");
        gasolinera1.setMunicipio("erwer");

        gasolinera1.setDieselA("1");
        gasolinera1.setNormal95("3");

        format = NumberFormat.getInstance(Locale.FRANCE);

        when (mockDetailView.getGasolinerasRepository()).thenReturn(mockGasolinerasRepository);
        when (mockDetailView.getPromocionesRepository()).thenReturn(mockPromocionesRepository);
        when (mockPromocionesRepository.getPromocionesRelacionadasConGasolinera(any())).thenReturn(
                new ArrayList<>());

        /* Descuentos*/
        // Gas station data
        gasolinera2 = new Gasolinera();
        gasolinera2.setId("123"); // ID
        gasolinera2.setRotulo("CEPSA"); // Brand name
        gasolinera2.setCp("39234"); // Postal code
        gasolinera2.setHorario("Nunca"); // Schedule
        gasolinera2.setDireccion("Calle Falsa, 123"); // Address
        gasolinera2.setMunicipio("Santander"); // City
        gasolinera2.setDieselA("1"); // Diesel price (€/L)
        gasolinera2.setNormal95("3"); // 95-octanes price (€/L)

        // Creates promotion list
        promotions = new LinkedList<>();
    }

    /* Sumario Sin Descuento */
    @Test
    public void calculaSumarioSinDescuentoTest() {
        sut = new GasolineraDetailPresenter(mockDetailView, gasolinera1);
        sut.init();

        String precioSumario = sut.getPrecioSumario();
        Assert.assertEquals("2,33", precioSumario);

        gasolinera1.setDieselA("-1");
        gasolinera1.setNormal95("-3,5");

        sut.init();

        precioSumario = sut.getPrecioSumario();
        Assert.assertEquals("-", precioSumario);

        gasolinera1.setDieselA("0");
        gasolinera1.setNormal95("3.0");
        sut.init();

        precioSumario = sut.getPrecioSumario();
        Assert.assertEquals("3,00", precioSumario);

        gasolinera1.setDieselA("5.0");
        gasolinera1.setNormal95("0.0");

        sut.init();

        precioSumario = sut.getPrecioSumario();
        Assert.assertEquals("5,00", precioSumario);
    }

    /* Descuentos*/
    @Test
    public void UT1aTest() {

        // XXX: UT.1a - no promotion applied
        // Creates the tested class (SUT - System Under Test) and initialises it
        sut = new GasolineraDetailPresenter(mockDetailView, gasolinera2);
        updatePromotions();

        Assert.assertEquals("2,33", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("1,00", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("3,00", sut.getDiscounted95OctanesPriceStr());

    }

    @Test
    public void UT1bTest() {
        // XXX: UT.1b - 20-cent promotion for all fuels
        // Creates the tested class (SUT - System Under Test) and initialises it
        sut = new GasolineraDetailPresenter(mockDetailView, gasolinera2);
        promotion = null;
        promotion = new Promocion();
        promotion.setDescuentoEurosLitro(0.2);
        promotion.setDescuentoPorcentual(-1);
        promotion.setCombustibles("Diésel-Gasolina");
        promotions.clear();
        promotions.add(promotion);
        updatePromotions();

        when (mockGasolinerasRepository.bestPromotion(1.0, promotions, "Diésel"))
                .thenReturn(promotion);
        when (mockGasolinerasRepository.bestPromotion(3.0, promotions, "Gasolina"))
                .thenReturn(promotion);

        Assert.assertEquals("2,13", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("0,80", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("2,80", sut.getDiscounted95OctanesPriceStr());

    }


    @Test
    public void calculateDiscountedSummaryPriceTest() {

        // XXX: UT.1c - 20-cent promotion only for diesel
        // Creates the tested class (SUT - System Under Test) and initialises it
        sut = new GasolineraDetailPresenter(mockDetailView, gasolinera2);
        promotion = new Promocion();
        promotion.setDescuentoEurosLitro(0.2);
        promotion.setDescuentoPorcentual(-1);
        promotion.setCombustibles("Diésel");
        promotions.clear();
        promotions.add(promotion);
        updatePromotions();

        when (mockGasolinerasRepository.bestPromotion(1.0, promotions, "Diésel"))
                .thenReturn(promotion);
        when (mockGasolinerasRepository.bestPromotion(3.0, promotions, "Gasolina"))
                .thenReturn(null);

        Assert.assertEquals("2,27", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("0,80", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("3,00", sut.getDiscounted95OctanesPriceStr());


        // XXX: UT.1d - 20-cent promotion only for 95-octanes
        promotion = new Promocion();
        promotion.setDescuentoEurosLitro(0.2);
        promotion.setDescuentoPorcentual(-1);
        promotion.setCombustibles("Gasolina");
        promotions.clear();
        promotions.add(promotion);

        when (mockGasolinerasRepository.bestPromotion(1.0, promotions, "Diésel"))
                .thenReturn(null);
        when (mockGasolinerasRepository.bestPromotion(3.0, promotions, "Gasolina"))
                .thenReturn(promotion);

        Assert.assertEquals("2,20", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("1,00", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("2,80", sut.getDiscounted95OctanesPriceStr());

        // XXX: UT.1e - 10% off for all fuels
        promotion = new Promocion();
        promotion.setDescuentoEurosLitro(-1);
        promotion.setDescuentoPorcentual(10.0);
        promotion.setCombustibles("Diésel-Gasolina");
        promotions.clear();
        promotions.add(promotion);

        when (mockGasolinerasRepository.bestPromotion(1.0, promotions, "Diésel"))
                .thenReturn(promotion);
        when (mockGasolinerasRepository.bestPromotion(3.0, promotions, "Gasolina"))
                .thenReturn(promotion);

        Assert.assertEquals("2,10", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("0,90", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("2,70", sut.getDiscounted95OctanesPriceStr());

        // XXX: UT.1f - 10% off only for diesel
        promotion = new Promocion();
        promotion.setDescuentoEurosLitro(-1);
        promotion.setDescuentoPorcentual(10.0);
        promotion.setCombustibles("Diésel");
        promotions.clear();
        promotions.add(promotion);

        when (mockGasolinerasRepository.bestPromotion(1.0, promotions, "Diésel"))
                .thenReturn(promotion);
        when (mockGasolinerasRepository.bestPromotion(3.0, promotions, "Gasolina"))
                .thenReturn(null);

        Assert.assertEquals("2,30", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("0,90", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("3,00", sut.getDiscounted95OctanesPriceStr());

        // XXX: UT.1g - 10% off only for 95-octanes
        promotion = new Promocion();
        promotion.setDescuentoEurosLitro(-1);
        promotion.setDescuentoPorcentual(10.0);
        promotion.setCombustibles("Gasolina");
        promotions.clear();
        promotions.add(promotion);

        when (mockGasolinerasRepository.bestPromotion(1.0, promotions, "Diésel"))
                .thenReturn(null);
        when (mockGasolinerasRepository.bestPromotion(3.0, promotions, "Gasolina"))
                .thenReturn(promotion);

        Assert.assertEquals("2,13", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("1,00", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("2,70", sut.getDiscounted95OctanesPriceStr());

        // XXX: UT.1h - discount on both fuels higher than regular price
        promotion = new Promocion();
        promotion.setDescuentoEurosLitro(4);
        promotion.setDescuentoPorcentual(-1);
        promotion.setCombustibles("Diésel-Gasolina");
        promotions.clear();
        promotions.add(promotion);

        when (mockGasolinerasRepository.bestPromotion(1.0, promotions, "Diésel"))
                .thenReturn(promotion);
        when (mockGasolinerasRepository.bestPromotion(3.0, promotions, "Gasolina"))
                .thenReturn(promotion);

        Assert.assertEquals("-", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("-", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("-", sut.getDiscounted95OctanesPriceStr());

        // XXX: UT.1i - discount on diesel higher than regular price
        promotion = new Promocion();
        promotion.setDescuentoEurosLitro(4);
        promotion.setDescuentoPorcentual(-1);
        promotion.setCombustibles("Diésel");
        promotions.clear();
        promotions.add(promotion);

        when (mockGasolinerasRepository.bestPromotion(1.0, promotions, "Diésel"))
                .thenReturn(promotion);
        when (mockGasolinerasRepository.bestPromotion(3.0, promotions, "Gasolina"))
                .thenReturn(null);

        Assert.assertEquals("3,00", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("-", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("3,00", sut.getDiscounted95OctanesPriceStr());

        // XXX: UT.1j - discount on 95-octanes higher than regular price
        promotion = new Promocion();
        promotion.setDescuentoEurosLitro(4);
        promotion.setDescuentoPorcentual(-1);
        promotion.setCombustibles("Gasolina");
        promotions.clear();
        promotions.add(promotion);

        when (mockGasolinerasRepository.bestPromotion(1.0, promotions, "Diésel"))
                .thenReturn(null);
        when (mockGasolinerasRepository.bestPromotion(3.0, promotions, "Gasolina"))
                .thenReturn(promotion);

        Assert.assertEquals("1,00", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("1,00", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("-", sut.getDiscounted95OctanesPriceStr());
    }

    private void updatePromotions() {
        when (mockPromocionesRepository.getPromocionesRelacionadasConGasolinera(gasolinera2.getId())).
                thenReturn(promotions);
    }
}
