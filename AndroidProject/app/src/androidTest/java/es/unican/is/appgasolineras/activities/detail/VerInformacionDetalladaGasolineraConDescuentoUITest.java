package es.unican.is.appgasolineras.activities.detail;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import android.content.Context;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.PromocionesRepository;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(AndroidJUnit4.class)
public class VerInformacionDetalladaGasolineraConDescuentoUITest {

    @Rule
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    @BeforeClass
    public static void setUp() {
        InstrumentationRegistry.getInstrumentation().getTargetContext().deleteDatabase("gasolineras-database");
        GasolinerasServiceConstants.setStaticURLMostrarPromocionEnGasolinera();
    }

    @Before
    public void insert() {
        addPromotions();
    }

    @After
    public void cleanDB() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        PromocionesRepository repPromociones = new PromocionesRepository(context);
        repPromociones.deleteAllPromociones();
    }

    @AfterClass
    public static void clean() {
        GasolinerasService.resetAPI();
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Test
    public void VerInformacionDetalladaGasolineraConDescuentoTest() {
        UIT1aTest();
        UIT1bTest();
        UIT1cTest();
        UIT1dTest();
        UIT1eTest();
        UIT1fTest();
        UIT1gTest();
        UIT1hTest();
        UIT1iTest();
        UIT1jTest();
    }

    private void UIT1aTest() {
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0).perform(click());
        onView(withId(R.id.tvDireccion)).check(matches(withText("CARRETERA 6316 KM. 10,5")));
        onView(withId(R.id.tvMunicipio)).check(matches(withText("Alfoz de Lloredo")));
        onView(withId(R.id.tvCP)).check(matches(withText("39526")));
        onView(withId(R.id.tvHorarioDet)).check(matches(withText("L-D: 08:00-21:00")));
        onView(withId(R.id.tvRotulo)).check(matches(withText("PETROPRIX")));

        // Base price
        onView(withId(R.id.tv95PrecioDet)).check(matches(withText("1,85 €/L")));
        onView(withId(R.id.tvDieselAPrecioDet)).check(matches(withText("1,99 €/L")));
        onView(withId(R.id.tvPrecioSumarioDet)).check(matches(withText("1,91 €/L")));

        // Discounted price (there isn't as there is no promotion applied)
        onView(withId(R.id.tvDiscounted95Price)).check(matches(withText("")));
        onView(withId(R.id.tvDiscountedDieselPrice)).check(matches(withText("")));
        onView(withId(R.id.tvDiscountedPrecioSumarioDet)).check(matches(withText("")));
    }

    private void UIT1bTest() {
        onView(isRoot()).perform(ViewActions.pressBack());
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1).perform(click());
        onView(withId(R.id.tvDireccion)).check(matches(withText("CR N-629 79,7")));
        onView(withId(R.id.tvMunicipio)).check(matches(withText("Ampuero")));
        onView(withId(R.id.tvCP)).check(matches(withText("39840")));
        onView(withId(R.id.tvHorarioDet)).check(matches(withText("L-S: 07:30-21:30; D: 08:30-21:30")));

        // Base price
        onView(withId(R.id.tv95PrecioDet)).check(matches(withText("- €/L")));
        onView(withId(R.id.tvDieselAPrecioDet)).check(matches(withText("2,00 €/L")));
        onView(withId(R.id.tvPrecioSumarioDet)).check(matches(withText("2,01 €/L")));

        // Discounted price (only for diesel)
        onView(withId(R.id.tvDiscounted95Price)).check(matches(withText("")));
        onView(withId(R.id.tvDiscountedDieselPrice)).check(matches(withText("1,50 €/L")));
        onView(withId(R.id.tvDiscountedPrecioSumarioDet)).check(matches(withText("1,51 €/L")));
    }

    private void UIT1cTest() {
        onView(isRoot()).perform(ViewActions.pressBack());
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(2).perform(click());
        onView(withId(R.id.tvDireccion)).check(matches(withText("CARRETERA N-611 KM. 163,2")));
        onView(withId(R.id.tvMunicipio)).check(matches(withText("Arenas de Iguña")));
        onView(withId(R.id.tvCP)).check(matches(withText("39450")));
        onView(withId(R.id.tvHorarioDet)).check(matches(withText("L-D: 06:00-22:00")));

        onView(withId(R.id.tv95PrecioDet)).check(matches(withText("1,78 €/L")));
        onView(withId(R.id.tvDieselAPrecioDet)).check(matches(withText("- €/L")));
        onView(withId(R.id.tvPrecioSumarioDet)).check(matches(withText("1,79 €/L")));

        onView(withId(R.id.tvDiscounted95Price)).check(matches(withText("")));
        onView(withId(R.id.tvDiscountedDieselPrice)).check(matches(withText("")));

        onView(withId(R.id.tvDiscountedPrecioSumarioDet)).check(matches(withText("")));
    }

    private void UIT1dTest() {
        onView(isRoot()).perform(ViewActions.pressBack());
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(3).perform(click());
        onView(withId(R.id.tvDireccion)).check(matches(withText("CARRETERA ARGOÑOS SOMO KM. 28,7")));
        onView(withId(R.id.tvMunicipio)).check(matches(withText("Arnuero")));
        onView(withId(R.id.tvCP)).check(matches(withText("39195")));
        onView(withId(R.id.tvHorarioDet)).check(matches(withText("L-D: 07:00-22:00")));

        onView(withId(R.id.tv95PrecioDet)).check(matches(withText("1,81 €/L")));
        onView(withId(R.id.tvDieselAPrecioDet)).check(matches(withText("1,99 €/L")));
        onView(withId(R.id.tvPrecioSumarioDet)).check(matches(withText("1,88 €/L")));

        onView(withId(R.id.tvDiscounted95Price)).check(matches(withText("1,61 €/L")));
        onView(withId(R.id.tvDiscountedDieselPrice)).check(matches(withText("")));
        onView(withId(R.id.tvDiscountedPrecioSumarioDet)).check(matches(withText("1,75 €/L")));
    }

    private void UIT1eTest() {
        onView(isRoot()).perform(ViewActions.pressBack());
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(4).perform(click());

        onView(withId(R.id.tvDireccion)).check(matches(withText("CARRETERA CASTILLO SIETEVILLAS KM. S/N")));
        onView(withId(R.id.tvMunicipio)).check(matches(withText("Arnuero")));
        onView(withId(R.id.tvCP)).check(matches(withText("39195")));
        onView(withId(R.id.tvHorarioDet)).check(matches(withText("L-D: 07:00-22:30")));

        onView(withId(R.id.tv95PrecioDet)).check(matches(withText("1,82 €/L")));
        onView(withId(R.id.tvDieselAPrecioDet)).check(matches(withText("2,01 €/L")));
        onView(withId(R.id.tvPrecioSumarioDet)).check(matches(withText("1,89 €/L")));

        onView(withId(R.id.tvDiscounted95Price)).check(matches(withText("1,64 €/L")));
        onView(withId(R.id.tvDiscountedDieselPrice)).check(matches(withText("1,81 €/L")));
        onView(withId(R.id.tvDiscountedPrecioSumarioDet)).check(matches(withText("1,70 €/L")));
    }

    private void UIT1fTest() {
        onView(isRoot()).perform(ViewActions.pressBack());
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(5).perform(click());

        onView(withId(R.id.tvDireccion)).check(matches(withText("CALLE BOO, 52")));
        onView(withId(R.id.tvMunicipio)).check(matches(withText("Astillero (El)")));
        onView(withId(R.id.tvCP)).check(matches(withText("39613")));
        onView(withId(R.id.tvHorarioDet)).check(matches(withText("L-D: 24H")));

        onView(withId(R.id.tv95PrecioDet)).check(matches(withText("1,83 €/L")));
        onView(withId(R.id.tvDieselAPrecioDet)).check(matches(withText("1,87 €/L")));
        onView(withId(R.id.tvPrecioSumarioDet)).check(matches(withText("1,85 €/L")));

        onView(withId(R.id.tvDiscounted95Price)).check(matches(withText("")));
        onView(withId(R.id.tvDiscountedDieselPrice)).check(matches(withText("1,69 €/L")));
        onView(withId(R.id.tvDiscountedPrecioSumarioDet)).check(matches(withText("1,79 €/L")));
    }


    private void UIT1gTest() {
        onView(isRoot()).perform(ViewActions.pressBack());
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(6).perform(click());

        onView(withId(R.id.tvDireccion)).check(matches(withText("CALLE PROSPERIDAD, 61")));
        onView(withId(R.id.tvMunicipio)).check(matches(withText("Astillero (El)")));
        onView(withId(R.id.tvCP)).check(matches(withText("39611")));
        onView(withId(R.id.tvHorarioDet)).check(matches(withText("L-S: 07:00-13:00")));

        onView(withId(R.id.tv95PrecioDet)).check(matches(withText("1,81 €/L")));
        onView(withId(R.id.tvDieselAPrecioDet)).check(matches(withText("1,97 €/L")));
        onView(withId(R.id.tvPrecioSumarioDet)).check(matches(withText("1,87 €/L")));

        onView(withId(R.id.tvDiscounted95Price)).check(matches(withText("1,63 €/L")));
        onView(withId(R.id.tvDiscountedDieselPrice)).check(matches(withText("")));
        onView(withId(R.id.tvDiscountedPrecioSumarioDet)).check(matches(withText("1,75 €/L")));
    }

    private void UIT1hTest() {
        onView(isRoot()).perform(ViewActions.pressBack());
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(7).perform(click());

        onView(withId(R.id.tvDireccion)).check(matches(withText("POLIGONO INDUSTRIAL GUARNIZO PARCELA, 22")));
        onView(withId(R.id.tvMunicipio)).check(matches(withText("Astillero (El)")));
        onView(withId(R.id.tvCP)).check(matches(withText("39611")));
        onView(withId(R.id.tvHorarioDet)).check(matches(withText("L-D: 24H")));

        onView(withId(R.id.tv95PrecioDet)).check(matches(withText("1,76 €/L")));
        onView(withId(R.id.tvDieselAPrecioDet)).check(matches(withText("1,92 €/L")));
        onView(withId(R.id.tvPrecioSumarioDet)).check(matches(withText("1,82 €/L")));

        onView(withId(R.id.tvDiscounted95Price)).check(matches(withText("- €/L")));
        onView(withId(R.id.tvDiscountedDieselPrice)).check(matches(withText("- €/L")));
        onView(withId(R.id.tvDiscountedPrecioSumarioDet)).check(matches(withText("- €/L")));
    }

    private void UIT1iTest() {
        onView(isRoot()).perform(ViewActions.pressBack());
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(8).perform(click());

        onView(withId(R.id.tvDireccion)).check(matches(withText("CALLE PROSPERIDAD, 61")));
        onView(withId(R.id.tvMunicipio)).check(matches(withText("Astillero (El)")));
        onView(withId(R.id.tvCP)).check(matches(withText("39611")));
        onView(withId(R.id.tvHorarioDet)).check(matches(withText("L-D: 07:00-23:00")));

        onView(withId(R.id.tv95PrecioDet)).check(matches(withText("1,81 €/L")));
        onView(withId(R.id.tvDieselAPrecioDet)).check(matches(withText("1,97 €/L")));
        onView(withId(R.id.tvPrecioSumarioDet)).check(matches(withText("1,87 €/L")));

        onView(withId(R.id.tvDiscounted95Price)).check(matches(withText("")));
        onView(withId(R.id.tvDiscountedDieselPrice)).check(matches(withText("- €/L")));
        onView(withId(R.id.tvDiscountedPrecioSumarioDet)).check(matches(withText("1,82 €/L")));
    }

    private void UIT1jTest() {
        onView(isRoot()).perform(ViewActions.pressBack());
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(9).perform(click());

        onView(withId(R.id.tvDireccion)).check(matches(withText("AU A-8, 182")));
        onView(withId(R.id.tvMunicipio)).check(matches(withText("Bárcena de Cicero")));
        onView(withId(R.id.tvCP)).check(matches(withText("39791")));
        onView(withId(R.id.tvHorarioDet)).check(matches(withText("L-D: 06:00-22:00")));

        onView(withId(R.id.tv95PrecioDet)).check(matches(withText("1,80 €/L")));
        onView(withId(R.id.tvDieselAPrecioDet)).check(matches(withText("1,99 €/L")));
        onView(withId(R.id.tvPrecioSumarioDet)).check(matches(withText("1,87 €/L")));

        onView(withId(R.id.tvDiscounted95Price)).check(matches(withText("- €/L")));
        onView(withId(R.id.tvDiscountedDieselPrice)).check(matches(withText("")));
        onView(withId(R.id.tvDiscountedPrecioSumarioDet)).check(matches(withText("2,00 €/L")));
    }



    private static void addPromotions() {

        /*** Promotion 1 - UIT.1b ***/
        clickAdd();
        onView(withId(R.id.etNombre)).perform(scrollTo(), typeText("50centRepsol"),
                closeSoftKeyboard()); // Name

        // Fuels
        onView(withId(R.id.spMultipleCombustibles)).perform(scrollTo(), click());
        onView(withText(R.string.dieselAlabel)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText(R.string.gasolina95label)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText("OK")).perform(click());

        // Determined brand
        onView(withId(R.id.spCriterioGasolineras)).perform(click());
        onData(anything()).atPosition(1).perform(click());

        // REPSOL
        onView(withId(R.id.spMultipleGasolinerasMarcas)).perform(click());
        onData(anything()).atPosition(6).perform(click());
        onView(withText("OK")).perform(click());

        // Euros / liter
        onView(withId(R.id.etDescuento)).perform(scrollTo(), typeText("0.5"), closeSoftKeyboard());

        // Click on add
        onView(withId(R.id.btnAnhadir)).perform(scrollTo(), click());

        // Accept button
        onView(withId(android.R.id.button1)).perform(click());

        /*** Promotion 2 - UIT.1c ***/
        clickAdd();onView(withId(R.id.etNombre)).perform(typeText("20cDPetronor"),
                closeSoftKeyboard()); // Name

        // Fuels
        onView(withId(R.id.spMultipleCombustibles)).perform(click());
        onView(withText(R.string.dieselAlabel)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText("OK")).perform(click());

        // Determined brand
        onView(withId(R.id.spCriterioGasolineras)).perform(click());
        onData(anything()).atPosition(1).perform(click());

        // Petronor
        onView(withId(R.id.spMultipleGasolinerasMarcas)).perform(click());
        onData(anything()).atPosition(5).perform(click());
        onView(withText("OK")).perform(click());

        // Euros / liter
        onView(withId(R.id.etDescuento)).perform(scrollTo(), typeText("0.2"), closeSoftKeyboard());

        // Click on add
        onView(withId(R.id.btnAnhadir)).perform(scrollTo(), click());

        // Accept button
        onView(withId(android.R.id.button1)).perform(click());

        /*** Promotion 3 - UIT.1d ***/
        clickAdd();onView(withId(R.id.etNombre)).perform(typeText("20cGArnuero"),
                closeSoftKeyboard()); // Name

        // Fuels
        onView(withId(R.id.spMultipleCombustibles)).perform(click());
        onView(withText(R.string.gasolina95label)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText("OK")).perform(click());

        // Determined gas station
        onView(withId(R.id.spCriterioGasolineras)).perform(click());
        onData(anything()).atPosition(2).perform(click());

        // Gas station at position 4
        onView(withId(R.id.spGasolinerasAplicables)).perform(click());
        onData(anything()).atPosition(3).perform(click());

        // Euros / liter
        onView(withId(R.id.etDescuento)).perform(scrollTo(), typeText("0.2"), closeSoftKeyboard());

        // Click on add
        onView(withId(R.id.btnAnhadir)).perform(scrollTo(), click());

        // Accept button
        onView(withId(android.R.id.button1)).perform(click());

        /*** Promotion 4 - UIT.1e ***/
        clickAdd();onView(withId(R.id.etNombre)).perform(typeText("10% CEPSA"),
                closeSoftKeyboard()); // Name

        // Fuels
        onView(withId(R.id.spMultipleCombustibles)).perform(click());
        onView(withText(R.string.dieselAlabel)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText(R.string.gasolina95label)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText("OK")).perform(click());

        // Determined brand
        onView(withId(R.id.spCriterioGasolineras)).perform(click());
        onData(anything()).atPosition(1).perform(click());

        // Cepsa
        onView(withId(R.id.spMultipleGasolinerasMarcas)).perform(click());
        onData(anything()).atPosition(3).perform(click());
        onView(withText("OK")).perform(click());


        // %
        onView(withId(R.id.spTipoDescuento)).perform(scrollTo(), click());
        onData(anything()).atPosition(1).perform(scrollTo(), click());
        onView(withId(R.id.etDescuento)).perform(scrollTo(), typeText("10"), closeSoftKeyboard());

        // Click on add
        onView(withId(R.id.btnAnhadir)).perform(scrollTo(), click());

        // Accept button
        onView(withId(android.R.id.button1)).perform(click());

        /*** Promotion 5 - UIT.1f ***/
        clickAdd();onView(withId(R.id.etNombre)).perform(typeText("10% Shell d"),
                closeSoftKeyboard()); // Name

        // Fuels
        onView(withId(R.id.spMultipleCombustibles)).perform(click());
        onView(withText(R.string.dieselAlabel)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText("OK")).perform(click());

        // Determined brand
        onView(withId(R.id.spCriterioGasolineras)).perform(click());
        onData(anything()).atPosition(1).perform(click());

        // Shell
        onView(withId(R.id.spMultipleGasolinerasMarcas)).perform(click());
        onData(anything()).atPosition(7).perform(click());
        onView(withText("OK")).perform(click());

        // %
        onView(withId(R.id.spTipoDescuento)).perform(scrollTo(), click());
        onData(anything()).atPosition(1).perform(scrollTo(), click());
        onView(withId(R.id.etDescuento)).perform(scrollTo(), typeText("10"), closeSoftKeyboard());

        // Click on add
        onView(withId(R.id.btnAnhadir)).perform(scrollTo(), click());

        // Accept button
        onView(withId(android.R.id.button1)).perform(click());

        /*** Promotion 6 - UIT.1g ***/
        clickAdd();onView(withId(R.id.etNombre)).perform(typeText("10% g Palmera"),
                closeSoftKeyboard()); // Name

        // Fuels
        onView(withId(R.id.spMultipleCombustibles)).perform(click());
        onView(withText(R.string.gasolina95label)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText("OK")).perform(click());

        // Determined gas station
        onView(withId(R.id.spCriterioGasolineras)).perform(click());
        onData(anything()).atPosition(2).perform(click());

        // Position 7
        onView(withId(R.id.spGasolinerasAplicables)).perform(click());
        onData(anything()).atPosition(6).perform(click());

        // %
        onView(withId(R.id.spTipoDescuento)).perform(scrollTo(), click());
        onData(anything()).atPosition(1).perform(scrollTo(), click());
        onView(withId(R.id.etDescuento)).perform(scrollTo(), typeText("10"), closeSoftKeyboard());

        // Click on add
        onView(withId(R.id.btnAnhadir)).perform(scrollTo(), click());

        // Accept button
        onView(withId(android.R.id.button1)).perform(click());

        /*** Promotion 7 - UIT.1h ***/
        clickAdd();
        onView(withId(R.id.etNombre)).perform(typeText("4e Cobo"),
                closeSoftKeyboard()); // Name

        // Fuels
        onView(withId(R.id.spMultipleCombustibles)).perform(click());
        onView(withText(R.string.dieselAlabel)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText(R.string.gasolina95label)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText("OK")).perform(click());

        // Determined gas station
        onView(withId(R.id.spCriterioGasolineras)).perform(click());
        onData(anything()).atPosition(2).perform(click());

        // COBO
        onView(withId(R.id.spGasolinerasAplicables)).perform(click());
        onData(anything()).atPosition(7).perform(click());

        // Euros / Liter
        onView(withId(R.id.spTipoDescuento)).perform(scrollTo(), click());
        onData(anything()).atPosition(0).perform(scrollTo(), click());
        onView(withId(R.id.etDescuento)).perform(scrollTo(), typeText("4"), closeSoftKeyboard());

        // Click on add
        onView(withId(R.id.btnAnhadir)).perform(scrollTo(), click());

        // Accept button
        onView(withId(android.R.id.button1)).perform(click());

        /*** Promotion 8 - UIT.1i ***/
        clickAdd();onView(withId(R.id.etNombre)).perform(typeText("4e GALP d"),
                closeSoftKeyboard()); // Name

        // Fuels
        onView(withId(R.id.spMultipleCombustibles)).perform(click());
        onView(withText(R.string.dieselAlabel)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText("OK")).perform(click());

        // Determined brand
        onView(withId(R.id.spCriterioGasolineras)).perform(click());
        onData(anything()).atPosition(1).perform(click());

        // GALP
        onView(withId(R.id.spMultipleGasolinerasMarcas)).perform(click());
        onData(anything()).atPosition(4).perform(click());
        onView(withText("OK")).perform(click());

        // Euros / Liter
        onView(withId(R.id.spTipoDescuento)).perform(scrollTo(), click());
        onData(anything()).atPosition(0).perform(scrollTo(), click());
        onView(withId(R.id.etDescuento)).perform(scrollTo(), typeText("4"), closeSoftKeyboard());

        // Click on add
        onView(withId(R.id.btnAnhadir)).perform(scrollTo(), click());

        // Accept button
        onView(withId(android.R.id.button1)).perform(click());


        /*** Promotion 9 - UIT.1j ***/
        clickAdd();onView(withId(R.id.etNombre)).perform(typeText("4e g Meroil"),
                closeSoftKeyboard()); // Name

        // Fuels
        onView(withId(R.id.spMultipleCombustibles)).perform(click());
        onView(withText(R.string.gasolina95label)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText("OK")).perform(click());

        // Determined gas station
        onView(withId(R.id.spCriterioGasolineras)).perform(click());
        onData(anything()).atPosition(2).perform(click());

        // Position 10
        onView(withId(R.id.spGasolinerasAplicables)).perform(click());
        onData(anything()).atPosition(9).perform(click());

        // Euros / Liter
        onView(withId(R.id.spTipoDescuento)).perform(scrollTo(), click());
        onData(anything()).atPosition(0).perform(scrollTo(), click());
        onView(withId(R.id.etDescuento)).perform(scrollTo(), typeText("4"), closeSoftKeyboard());

        // Click on add
        onView(withId(R.id.btnAnhadir)).perform(scrollTo(), click());

        // Accept button
        onView(withId(android.R.id.button1)).perform(click());
    }

    private static void clickAdd() {

        // Opens activity
        try {
            onView(withId(R.id.menuPromotion)).perform(click());
        } catch (NoMatchingViewException e) { // Cannot be accessed by icon
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().
                    getTargetContext());
            onView(withText(R.string.promotion)).perform(click());
        }
        onView(withText(R.string.addPromotion)).perform(click());
    }

}
