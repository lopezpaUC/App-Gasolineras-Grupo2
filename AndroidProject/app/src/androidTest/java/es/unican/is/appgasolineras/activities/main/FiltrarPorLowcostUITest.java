package es.unican.is.appgasolineras.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import androidx.test.espresso.DataInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

public class FiltrarPorLowcostUITest {

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURLFiltrarPorLowcost();
    }

    @AfterClass
    public static void clean() {
        GasolinerasService.resetAPI();
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Rule
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    @Test
    public void testFiltrarPorLowcost() {
        // Caso valido: filtro unico
        onView(withId(R.id.menuFilter)).perform(click());
        onView((withId(R.id.chckLowcost))).perform(click());
        onView(withId(R.id.tvApply)).perform(click());

        /* Comprobamos los datos de una gasolinera, los cuáles deberían ser los esperados.*/
        DataInteraction gasolinera = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0);
        gasolinera.onChildView(withId(R.id.tvName)).check(matches(withText("BALLENOIL")));
        gasolinera.onChildView(withId(R.id.tvAddress)).check(matches(withText("CALLE GUTIERREZ SOLANA 24, 24")));
        gasolinera.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gasolinera.onChildView(withId(R.id.tv95)).check(matches(withText("1,699")));
        gasolinera.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));
        gasolinera.onChildView(withId(R.id.tvDieselA)).check(matches(withText("1,799")));

    }

}
