package es.unican.is.appgasolineras.activities.addPromotion;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;

import android.Manifest;
import android.content.Context;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.repository.PromocionesRepository;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;
import es.unican.is.appgasolineras.utils.ScreenshotTestRule;

/**
 * Prueba de interfaz para la historia de usuario: Anhadir Promocion.
 */
@RunWith(AndroidJUnit4.class)
public class AnhadirPromocionUITest {
    // IMPORTANTE: No tiene rule, se incluye en el rule de abajo
    public ActivityScenarioRule<MainView> activityRule =
            new ActivityScenarioRule(MainView.class);

    // Aquí se combinan el ActivityScenarioRule y el ScreenshotTestRule,
    // de forma que la captura de pantalla se haga antes de que se cierre la actividad
    @Rule
    public final TestRule activityAndScreenshotRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotTestRule());

    // Regla para garantizar permisos
    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @BeforeClass
    public static void init() {
        // Por precaucion
        InstrumentationRegistry.getInstrumentation().getTargetContext().deleteDatabase("gasolineras-database");
        GasolinerasServiceConstants.setStaticURL3();
    }

    @After
    public void cleanDatabase() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        PromocionesRepository repPromociones = new PromocionesRepository(context);
        repPromociones.deleteAllPromociones();
    }

    @AfterClass
    public static void end() {
        GasolinerasService.resetAPI();
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Test
    public void testAnhadirPromocion() {
        // CASO 01

        // Abrir actividad para anhadir promocion
        try {
            onView(withId(R.id.menuPromotion)).perform(click());
        } catch (NoMatchingViewException e) { // Si la pantalla es pequenha y no se accede por icono
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().
                    getTargetContext());
            onView(withText(R.string.promotion)).perform(click());
        }
        onView(withText(R.string.addPromotion)).perform(click());

        // Indicar el nombre de la promocion
        onView(withId(R.id.etNombre)).perform(scrollTo(), typeText("P01"), closeSoftKeyboard());

        // Indicar el tipo de combustible
        onView(withId(R.id.spMultipleCombustibles)).perform(scrollTo(), click());
        onView(withText(R.string.dieselAlabel)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText("OK")).perform(click());

        // Indicar el criterio de aplicacion a gasolineras
        onView(withId(R.id.spCriterioGasolineras)).perform(scrollTo(), click());
        onData(anything()).atPosition(0).perform(scrollTo(), click());
        onView(withId(R.id.spCriterioGasolineras)).check(matches(withSpinnerText(R.string.allA)));

        // Indicar la cantidad en porcentaje a descontar
        onView(withId(R.id.etDescuento)).perform(scrollTo(), typeText("5"), closeSoftKeyboard());

        // Indicar que el tipo de descuento es por porcentaje
        onView(withId(R.id.spTipoDescuento)).perform(scrollTo(), click());
        onData(anything()).atPosition(1).perform(scrollTo(), click());
        onView(withId(R.id.spTipoDescuento)).check(matches(withSpinnerText(R.string.Porcentajelabel)));

        // Clickar en anhadir
        onView(withId(R.id.btnAnhadir)).perform(scrollTo(), click());

        // Confirmar que se muestra el cuadro de dialogo correcto
        onView(withText(R.string.promoExito)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());

        // Abrir actividad para ver promociones
        try {
            onView(withId(R.id.menuPromotion)).perform(click());
        } catch (NoMatchingViewException e) { // Si la pantalla es pequenha y no se accede por icono
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().
                    getTargetContext());
            onView(withText(R.string.promotion)).perform(click());
        }
        onView(withText(R.string.listPromotions)).perform(click());


        DataInteraction p = onData(anything()).inAdapterView(withId(R.id.lvPromociones)).
                atPosition(0);
        p.onChildView(withId(R.id.tvNamePromocion)).check(matches(withText("P01")));
        p.onChildView(withId(R.id.tvNameGasolinera)).check(matches(withText(R.string.varias)));
        p.onChildView(withId(R.id.tvDescuento)).check(matches(withText("5.0%")));
        p.onChildView(withId(R.id.tvCombustible)).check(matches(withText(R.string.dieselAlabel)));
    }
}
