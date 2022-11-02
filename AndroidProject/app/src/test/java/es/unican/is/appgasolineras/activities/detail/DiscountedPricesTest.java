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

import java.util.LinkedList;
import java.util.List;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
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

    @Mock
    IGasolineraDetailContract.View mockDetailView;

    @Mock
    IPromocionesRepository mockPromocionesRepository;

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

        // Defines mock behaviour
        when (mockDetailView.getPromocionesRepository()).thenReturn(mockPromocionesRepository);

        // Creates the tested class (SUT - System Under Test) and initialises it
        sut = new GasolineraDetailPresenter(mockDetailView, gasStation);
        sut.init();
    }

    @Test
    public void calculateDiscountedSummaryPriceTest() {
        // XXX: UT.1a - no promotion applied
        updatePromotions();

        Assert.assertEquals("2,33", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("1,00", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("3,00", sut.getDiscounted95OctanesPriceStr());

        // XXX: UT.1b - 20-cent promotion for all fuels
        Promocion promotion = new Promocion();
        promotion.setDescuentoEurosLitro(0.2);
        promotion.setDescuentoPorcentual(-1);
        promotion.setCombustibles("Diésel-Gasolina");
        promotions.clear();
        promotions.add(promotion);
        updatePromotions();

        Assert.assertEquals("2,13", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("0,80", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("2,80", sut.getDiscounted95OctanesPriceStr());

        // XXX: UT.1c - 20-cent promotion only for diesel
        promotion = new Promocion();
        promotion.setDescuentoEurosLitro(0.2);
        promotion.setDescuentoPorcentual(-1);
        promotion.setCombustibles("Diésel");
        promotions.clear();
        promotions.add(promotion);
        updatePromotions();

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
    }

    private void updatePromotions() {
        when (mockPromocionesRepository.getPromocionesRelacionadasConGasolinera(gasStation.getId())).
                thenReturn(promotions);
    }
}
