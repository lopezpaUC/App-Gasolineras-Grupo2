package es.unican.is.appgasolineras.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static es.unican.is.appgasolineras.utils.Matchers.hasElements;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(AndroidJUnit4.class)

public class FiltrarPorMarcaUITest {

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL2();
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Rule
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    @Test
    public void testFiltrarPorMarca() {
        // Caso valido: filtro unico
        /*onView(withId(R.id.menuFilter)).perform(click());
        onView(withId(R.id.spnMarca)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.spnMarca)).atPosition(3).onChildView(withId(R.id.checkbox)).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.tvApply)).perform(click());*/

        /*DataInteraction gasolinera1 = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0);
        gasolinera1.onChildView(withId(R.id.tvName)).check(matches(withText("CEPSA")));
        gasolinera1.onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA 6316 KM. 10,5")));
        gasolinera1.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gasolinera1.onChildView(withId(R.id.tv95)).check(matches(withText("1,819")));
        gasolinera1.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));
        gasolinera1.onChildView(withId(R.id.tvDieselA)).check(matches(withText("2,009")));*/

        /*DataInteraction gasolinera2 = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1);
        gasolinera2.onChildView(withId(R.id.tvName)).check(matches(withText("REPSOL")));
        gasolinera2.onChildView(withId(R.id.tvAddress)).check(matches(withText("CR N-629 79,7")));
        gasolinera2.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gasolinera2.onChildView(withId(R.id.tv95)).check(matches(withText("1,819")));
        gasolinera2.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));
        gasolinera2.onChildView(withId(R.id.tvDieselA)).check(matches(withText("-")));*/

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
