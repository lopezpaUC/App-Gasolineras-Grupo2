package es.unican.is.appgasolineras.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import static java.lang.Thread.*;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

public class OrdenarPorMarcaUITest {

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL2();
    }

    @AfterClass
    public static void clean() {
        GasolinerasService.resetAPI();
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Rule
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    @Test
    public void testOrdenarPorPrecio() {
        // Caso valido: filtro unico
        /* Pulsamos en el filtro, y acontinuación en el spinner. Después deberíamos pulsar en la checkbox correspondiente
        a Cepsa. A continuación, debería haber un botón para poder cerrar el spinner. Por último, le damos a aplizar cambios.*/
        //openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        DataInteraction elementoLista;
        try {
            onView(withId(R.id.menuOrder)).perform(click());
        } catch (NoMatchingViewException e) { // Si la pantalla es pequenha y no se accede por icono
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().
                    getTargetContext());
            onView(withText("Ordenar por")).perform(click());

        }
        onView(withText("Ordenar por precio")).perform(click());
        onView((withId(R.id.spnTipoOrdenacion))).perform(click());
        onView(withText("Descendente")).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withId(R.id.tvApply)).perform(click());

        /* Comprobamos los datos de una gasolinera, los cuáles deberían ser los esperados.*/
        DataInteraction gasolinera = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0);
        gasolinera.onChildView(withId(R.id.tvName)).check(matches(withText("E.S. CARBURANTES DE ARNUERO S.L.")));
        gasolinera.onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA CASTILLO SIETEVILLAS KM. S/N")));
        gasolinera.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gasolinera.onChildView(withId(R.id.tv95)).check(matches(withText("1,829")));
        gasolinera.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));
        gasolinera.onChildView(withId(R.id.tvDieselA)).check(matches(withText("2,019")));

    }
}
