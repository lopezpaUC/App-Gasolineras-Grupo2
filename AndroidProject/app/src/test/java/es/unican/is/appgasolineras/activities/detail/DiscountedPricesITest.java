package es.unican.is.appgasolineras.activities.detail;

import android.content.Context;
import android.os.Build;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.when;

import androidx.test.core.app.ApplicationProvider;

import java.util.LinkedList;
import java.util.List;

import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;
import es.unican.is.appgasolineras.repository.PromocionesRepository;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class DiscountedPricesITest {

    @Mock
    private IGasolineraDetailContract.View mockDetailView;

    private IPromocionesRepository promocionesRepository;
    private Gasolinera gasStation;
    private Context context;
    private List<Promocion> promotions;
    private Promocion promotion;
    private GasolineraDetailPresenter sut;

    @BeforeClass
    public static void setUpClass() {
        GasolinerasServiceConstants.setStaticURL2();
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Before
    public void setUp() {
        // Gas station data
        gasStation = new Gasolinera();
        gasStation.setId("123");                        // ID
        gasStation.setRotulo("CEPSA");                  // Brand name
        gasStation.setCp("39234");                      // Postal code
        gasStation.setHorario("Nunca");                 // Schedule
        gasStation.setDireccion("Calle Falsa, 123");    // Address
        gasStation.setMunicipio("Santander");           // Municipality
        gasStation.setDieselA("1");                     // Diesel price (€/L)
        gasStation.setNormal95("3");                    // 95-octanes price (€/L)

        MockitoAnnotations.openMocks(this);


        context = ApplicationProvider.getApplicationContext();
        promotions = new LinkedList<>();

        promocionesRepository = new PromocionesRepository(context);
        when(mockDetailView.getPromocionesRepository()).thenReturn(promocionesRepository);

        sut = new GasolineraDetailPresenter(mockDetailView, gasStation);
        sut.init();
    }

    @After
    public void cleanDatabase() {
        promocionesRepository.deleteAllPromociones();
        GasolineraDatabase db = GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext());
        db.close();
    }


    @Test
    public void UT1aTest() {
        // XXX: UT.1a - no promotion applied
        Assert.assertEquals("2,33", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("1,00", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("3,00", sut.getDiscounted95OctanesPriceStr());
    }

    @Test
    public void UT1bTest() {
        // XXX: UT.1b - 20-cent promotion for all fuels
        promotion = new Promocion();
        promotion.setDescuentoEurosLitro(0.2);
        promotion.setDescuentoPorcentual(-1);
        promotion.setCombustibles("Diésel-Gasolina");
        promotions.add(promotion);

        updatePromocionesRepository();

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

        updatePromocionesRepository();

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

        updatePromocionesRepository();

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

        updatePromocionesRepository();

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

        updatePromocionesRepository();

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

        updatePromocionesRepository();

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

        updatePromocionesRepository();

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

        updatePromocionesRepository();

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

        updatePromocionesRepository();

        Assert.assertEquals("1,00", sut.getDiscountedSummaryPriceStr());
        Assert.assertEquals("1,00", sut.getDiscountedDieselPriceStr());
        Assert.assertEquals("-", sut.getDiscounted95OctanesPriceStr());
    }

    /**
     * Restores the promotions repository, inserts a new promotion and programmes the view to
     * return the updated repository
     */
    private void updatePromocionesRepository() {
        promocionesRepository.deleteAllPromociones();
        promocionesRepository.insertPromocion(promotion);
        promocionesRepository.insertRelacionGasolineraPromocion(gasStation, promotion);
        when(mockDetailView.getPromocionesRepository()).thenReturn(promocionesRepository);
    }

}
