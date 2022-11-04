package es.unican.is.appgasolineras.activities.promotion;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import android.content.Context;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import es.unican.is.appgasolineras.R;

import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.repository.PromocionesRepository;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

public class ListaPromocionesUITest {

    @BeforeClass
    public static void setUp() {
        // Por precaucion
        InstrumentationRegistry.getInstrumentation().getTargetContext().deleteDatabase("gasolineras-database");
        GasolinerasServiceConstants.setStaticURL2();
    }

    @After
    public void cleanDatabase() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        PromocionesRepository repPromociones = new PromocionesRepository(context);
        repPromociones.deleteAllPromociones();
    }

    @AfterClass
    public static void clean() {
        GasolinerasService.resetAPI();
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Rule
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    @Test
    public void testListaPromociones() {
        // Caso valido: Carga de lista de promociones completa
        //Se añade la promocion
        try {
            onView(withId(R.id.menuPromotion)).perform(click());
        } catch (NoMatchingViewException e) { // Si la pantalla es pequenha y no se accede por icono
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().
                    getTargetContext());
            onView(withText(R.string.promotion)).perform(click());
        }
        onView(withText(R.string.addPromotion)).perform(click());

        // Indicar el nombre de la promocion
        onView(withId(R.id.etNombre)).perform(typeText("P01"), closeSoftKeyboard());

        // Indicar el tipo de combustible
        onView(withId(R.id.spMultipleCombustibles)).perform(scrollTo(), click());
        onView(withText(R.string.dieselAlabel)).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText("OK")).perform(click());

        // Indicar el criterio de aplicacion a gasolineras
        onView(withId(R.id.spCriterioGasolineras)).perform(scrollTo(), click());
        onData(anything()).atPosition(0).perform(scrollTo(), click());


        // Indicar la cantidad en porcentaje a descontar
        onView(withId(R.id.etDescuento)).perform(scrollTo(), typeText("5"), closeSoftKeyboard());

        // Indicar que el tipo de descuento es por porcentaje
        onView(withId(R.id.spTipoDescuento)).perform(scrollTo(), click());
        onData(anything()).atPosition(1).perform(scrollTo(), click());
        onView(withId(R.id.spTipoDescuento)).check(matches(withSpinnerText(R.string.Porcentajelabel)));

        // Clickar en anhadir
        onView(withId(R.id.btnAnhadir)).perform(scrollTo(), click());

        onView(withId(android.R.id.button1)).perform(click());


        /* Se hace click sobre el boton de ver lista promociones en el menu de promociones.*/
        try {
            onView(withId(R.id.menuPromotion)).perform(click());
        } catch (NoMatchingViewException e) { // Si la pantalla es pequenha y no se accede por icono
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().
                    getTargetContext());
            onView(withText(R.string.promotion)).perform(click());
        }
        onView(withText(R.string.listPromotions)).perform(click());

        /* Comprobamos los datos de una promocion, los cuáles deberían ser los esperados.*/
        DataInteraction promocion = onData(anything()).inAdapterView(withId(R.id.lvPromociones)).atPosition(0);
        promocion.onChildView(withId(R.id.tvNamePromocion)).check(matches(withText("P01")));
        promocion.onChildView(withId(R.id.tvNameGasolinera)).check(matches(withText("Varias")));
        promocion.onChildView(withId(R.id.tvDescuento)).check(matches(withText("5.0%")));
        promocion.onChildView(withId(R.id.tvCombustible)).check(matches(withText("Diésel")));
        promocion.onChildView(withId(R.id.tvGasolinerasAsociadas)).check(matches(withText("Gasolineras:")));
        promocion.onChildView(withId(R.id.tvCombustiblesAsociados)).check(matches(withText("Combustibles:")));

    }

}
