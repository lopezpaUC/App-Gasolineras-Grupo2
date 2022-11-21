package es.unican.is.appgasolineras.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import android.Manifest;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.repository.rest.GasolinerasAPI;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;
import es.unican.is.appgasolineras.utils.ScreenshotTestRule;

@RunWith(AndroidJUnit4.class)

public class FiltrarPorMarcaUITest {

    // IMPORTANTE: No tiene rule, se incluye en el rule de abajo
    public ActivityScenarioRule<MainView> activityRule =
            new ActivityScenarioRule(MainView.class);

    // Aquí se combinan el ActivityScenarioRule y el ScreenshotTestRule,
    // de forma que la captura de pantalla se haga antes de que se cierre la actividad
    @Rule
    public final TestRule activityAndScreenshotRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotTestRule());

    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL2();
    }

    @AfterClass
    public static void clean() {
        GasolinerasService.resetAPI();
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Test
    public void testFiltrarPorMarca() {
        // Caso valido: filtro unico
        /* Pulsamos en el filtro, y acontinuación en el spinner. Después deberíamos pulsar en la checkbox correspondiente
        a Cepsa. A continuación, debería haber un botón para poder cerrar el spinner. Por último, le damos a aplizar cambios.*/
        onView(withId(R.id.menuFilter)).perform(click());
        onView((withId(R.id.spnMarca))).perform(click());
        onView(withText("Cepsa")).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withText("OK")).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withId(R.id.tvApply)).perform(click());

        /* Comprobamos los datos de una gasolinera, los cuáles deberían ser los esperados.*/
        DataInteraction gasolinera = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0);
        gasolinera.onChildView(withId(R.id.tvName)).check(matches(withText("CEPSA")));
        gasolinera.onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA 6316 KM. 10,5")));
        gasolinera.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gasolinera.onChildView(withId(R.id.tv95)).check(matches(withText("1,859")));
        gasolinera.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));
        gasolinera.onChildView(withId(R.id.tvDieselA)).check(matches(withText("1,999")));

    }
}
