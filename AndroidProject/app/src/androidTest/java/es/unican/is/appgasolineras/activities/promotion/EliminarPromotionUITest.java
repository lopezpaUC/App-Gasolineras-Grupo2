package es.unican.is.appgasolineras.activities.promotion;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import android.content.Context;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.repository.PromocionesRepository;
import es.unican.is.appgasolineras.repository.rest.GasolinerasAPI;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(AndroidJUnit4.class)

public class EliminarPromotionUITest {
    @Rule
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    @BeforeClass
    public static void init() {
        // Por precaucion
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
    public void testEliminarPromocion() {
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
        onView(withId(R.id.etNombre)).perform(typeText("P02"), closeSoftKeyboard());

        // Indicar el tipo de combustible
        onView(withId(R.id.spMultipleCombustibles)).perform(click());
        onView(withText(R.string.dieselAlabel)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText("OK")).perform(click());

        // Indicar el criterio de aplicacion a gasolineras
        onView(withId(R.id.spCriterioGasolineras)).perform(click());
        onData(Matchers.anything()).atPosition(0).perform(click());


        // Indicar la cantidad en porcentaje a descontar
        onView(withId(R.id.etDescuento)).perform(typeText("5"), closeSoftKeyboard());

        // Indicar que el tipo de descuento es por porcentaje
        onView(withId(R.id.spTipoDescuento)).perform(click());
        onData(Matchers.anything()).atPosition(1).perform(click());

        // Clickar en anhadir
        onView(withId(R.id.btnAnhadir)).perform(click());

        // Confirmar que se muestra el cuadro de dialogo correcto
        onView(withText(R.string.promoExito)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());

        // Abrir actividad para anhadir promocion
        try {
            onView(withId(R.id.menuPromotion)).perform(click());
        } catch (NoMatchingViewException e) { // Si la pantalla es pequenha y no se accede por icono
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().
                    getTargetContext());
            onView(withText(R.string.promotion)).perform(click());
        }
        onView(withText(R.string.listPromotions)).perform(click());
        DataInteraction elementoLista = onData(anything()).inAdapterView(withId(R.id.lvPromociones)).atPosition(0);
        elementoLista.onChildView(withId(R.id.ivBin)).perform(click());
        elementoLista.onChildView(withId(R.id.ivBin)).perform(click());

        //Comprobar que se muestra el cuado de dialogo correcto
        onView(withText("Aceptar")).perform(click());


        return;
    }
}
