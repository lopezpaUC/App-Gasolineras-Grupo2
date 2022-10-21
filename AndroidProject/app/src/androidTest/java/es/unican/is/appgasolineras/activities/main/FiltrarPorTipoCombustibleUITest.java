package es.unican.is.appgasolineras.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(AndroidJUnit4.class)

public class FiltrarPorTipoCombustibleUITest {

    @Rule
    public ActivityScenarioRule<MainView> activityRule =
            new ActivityScenarioRule<>(MainView.class);

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL2();
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Test
    public void testFiltrarPorTipoCombustible() {
        onView(withId(R.id.menuFilter)).perform(click());
        onView(withId(R.id.spnTipoCombustible)).perform(click());
        onView(withText("Diésel")).inRoot(RootMatchers.isDialog()).perform(click());
        onView(withId(R.id.tvApply)).perform(click());
        DataInteraction gas;

        gas = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0);
        gas.onChildView(withId(R.id.tvName)).check(matches(withText("CEPSA")));
        gas.onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA 6316 KM. 10,5")));
        gas.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gas.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));
        gas.onChildView(withId(R.id.tv95)).check(matches(withText("1,859")));
        gas.onChildView(withId(R.id.tvDieselA)).check(matches(withText("1,999")));

        gas = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1);
        gas.onChildView(withId(R.id.tvName)).check(matches(withText("PETRONOR")));
        gas.onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA N-611 KM. 163,2")));
        gas.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gas.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));
        gas.onChildView(withId(R.id.tv95)).check(matches(withText("-")));
        gas.onChildView(withId(R.id.tvDieselA)).check(matches(withText("1,969")));

        gas = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(2);
        gas.onChildView(withId(R.id.tvName)).check(matches(withText("E.S. CARBURANTES DE ARNUERO S.L.")));
        gas.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gas.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));
        gas.onChildView(withId(R.id.tv95)).check(matches(withText("1,829")));
        gas.onChildView(withId(R.id.tvDieselA)).check(matches(withText("2,019")));

        gas = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(3);
        gas.onChildView(withId(R.id.tvName)).check(matches(withText("G2")));
        gas.onChildView(withId(R.id.tvAddress)).check(matches(withText("CALLE BOO, 52")));
        gas.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gas.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));
        gas.onChildView(withId(R.id.tv95)).check(matches(withText("1,839")));
        gas.onChildView(withId(R.id.tvDieselA)).check(matches(withText("1,879")));

        gas = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(4);
        gas.onChildView(withId(R.id.tvName)).check(matches(withText("COBO")));
        gas.onChildView(withId(R.id.tvAddress)).check(matches(withText("POLIGONO INDUSTRIAL GUARNIZO PARCELA, 22")));
        gas.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gas.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));
        gas.onChildView(withId(R.id.tv95)).check(matches(withText("1,769")));
        gas.onChildView(withId(R.id.tvDieselA)).check(matches(withText("1,929")));

        gas = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(5);
        gas.onChildView(withId(R.id.tvName)).check(matches(withText("REPSOL")));
        gas.onChildView(withId(R.id.tvAddress)).check(matches(withText("AU A-8, 182")));
        gas.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gas.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));
        gas.onChildView(withId(R.id.tv95)).check(matches(withText("1,809")));
        gas.onChildView(withId(R.id.tvDieselA)).check(matches(withText("1,999")));
    }
}
