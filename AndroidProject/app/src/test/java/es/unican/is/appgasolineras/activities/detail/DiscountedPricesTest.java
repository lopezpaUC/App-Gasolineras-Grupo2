package es.unican.is.appgasolineras.activities.detail;

import static org.mockito.Mockito.*;

import android.content.Context;

import androidx.room.RoomDatabase;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

/**
 * Test class for the calculation of discounted prices given one or more promotions
 */
@RunWith(MockitoJUnitRunner.class)
public class DiscountedPricesTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private Gasolinera gasStation;
    private Promocion promotion;
    private GasolineraDetailPresenter sut;

    private Context context;
    private List<Promocion> promotions;
    private NumberFormat format;

    @Mock
    IGasolineraDetailContract.View mockDetailView;

    @Mock
    IPromocionesRepository mockPromocionesRepository;

    @Mock
    IGasolinerasRepository mockGasolinerasRepository;

    @Before
    public void setUp() {
        // Gas station data
        gasStation = new Gasolinera();
        gasStation.setId("123"); // ID
        gasStation.setRotulo("CEPSA"); // Brand name
        gasStation.setCp("39234"); // Postal code
        gasStation.setHorario("Nunca"); // Schedule
        gasStation.setDireccion("Calle Falsa, 123"); // Address
        gasStation.setMunicipio("Santander"); // City
        gasStation.setDieselA("1"); // Diesel price (€/L)
        gasStation.setNormal95("3"); // 95-octanes price (€/L)

        // Creates promotion list
        promotions = new LinkedList<>();

        format = NumberFormat.getInstance(Locale.FRANCE);

        // Defines mock behaviour
        when (mockDetailView.getPromocionesRepository()).thenReturn(mockPromocionesRepository);
        when (mockDetailView.getGasolinerasRepository()).thenReturn(mockGasolinerasRepository);

        // Creates the tested class (SUT - System Under Test) and initialises it
        sut = new GasolineraDetailPresenter(mockDetailView, gasStation);
        when (mockGasolinerasRepository.precioToDouble("1", format)).thenReturn(1.0);
        when (mockGasolinerasRepository.precioToDouble("3", format)).thenReturn(3.0);
        when (mockGasolinerasRepository.precioToDouble("2,33", format)).thenReturn(2.333);
        when (mockGasolinerasRepository.calculateSummary(anyDouble(), anyDouble())).thenReturn(2.333);
        when (mockGasolinerasRepository.precioSumarioToStr(2.333)).thenReturn("2,33");
        when (mockGasolinerasRepository.precioSumarioToStr(1.0)).thenReturn("1,00");
        when (mockGasolinerasRepository.precioSumarioToStr(3.0)).thenReturn("3,00");
        sut.init();
    }

    @Test
    public void UT1aTest() {

        // XXX: UT.1a - no promotion applied
        updatePromotions();

        Assert.assertEquals("2,33", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("1,00", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("3,00", sut.getDiscounted95OctanesPriceStr());

    }

    @Test
    public void UT1bTest() {
        // XXX: UT.1b - 20-cent promotion for all fuels
        promotion = null;
        promotion = new Promocion();
        promotion.setId("a");
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

        when (mockGasolinerasRepository.precioToDouble("0,8", format)).thenReturn(0.8);
        when (mockGasolinerasRepository.precioToDouble("2,8", format)).thenReturn(2.8);
        when (mockGasolinerasRepository.precioToDouble("2,13", format)).thenReturn(2.133);
        when (mockGasolinerasRepository.precioSumarioToStr(2.133)).thenReturn("2,13");
        when (mockGasolinerasRepository.precioSumarioToStr(0.8)).thenReturn("0,80");
        when (mockGasolinerasRepository.precioSumarioToStr(2.8)).thenReturn("2,80");
        when (mockGasolinerasRepository.calculateSummary(anyDouble(), anyDouble())).thenReturn(2.133);

        when (mockGasolinerasRepository.calculateDiscountedPrice(1.0, promotion)).thenReturn(0.8);
        when (mockGasolinerasRepository.calculateDiscountedPrice(3.0, promotion)).thenReturn(2.8);
        sut.init();

        Assert.assertEquals("2,13", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("0,80", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("2,80", sut.getDiscounted95OctanesPriceStr());

    }


    @Test
    public void calculateDiscountedSummaryPriceTest() {

        // XXX: UT.1c - 20-cent promotion only for diesel
        promotion = new Promocion();
        promotion.setDescuentoEurosLitro(0.2);
        promotion.setDescuentoPorcentual(-1);
        promotion.setCombustibles("Diésel");
        promotions.clear();
        promotions.add(promotion);
        updatePromotions();

        // XXX: UT.1d - 20-cent promotion only for 95-octanes
        promotion = new Promocion();
        promotion.setDescuentoEurosLitro(0.2);
        promotion.setDescuentoPorcentual(-1);
        promotion.setCombustibles("Gasolina");
        promotions.clear();
        promotions.add(promotion);

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

        Assert.assertEquals("1,00", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("1,00", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("-", sut.getDiscounted95OctanesPriceStr());
    }

    private void updatePromotions() {
        when (mockPromocionesRepository.getPromocionesRelacionadasConGasolinera(gasStation.getId())).
                thenReturn(promotions);
    }
}
