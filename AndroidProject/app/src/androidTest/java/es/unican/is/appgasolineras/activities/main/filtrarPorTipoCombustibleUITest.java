package es.unican.is.appgasolineras.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.RootMatchers.isTouchable;
import static androidx.test.espresso.matcher.ViewMatchers.hasTextColor;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import android.icu.text.UFormat;
import android.text.Html;

import androidx.test.espresso.DataInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

public class filtrarPorTipoCombustibleUITest {

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
    public void filtrarPorTipoCombustibleTest() {


        onView(withId(R.id.menuFilter)).perform(click());
        onView(withId(R.id.spnTipoCombustible)).perform(click());
        onView(withText("Diésel")).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.tvApply)).inRoot(isTouchable()).perform(click());
        DataInteraction gas;

        gas = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0);
        gas.onChildView(withId(R.id.tvName)).check(matches(withText("CEPSA")));
        gas.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gas.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));

        gas = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1);
        gas.onChildView(withId(R.id.tvName)).check(matches(withText("PETRONOR")));
        gas.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gas.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));

        /*gas = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(2);
        gas.onChildView(withId(R.id.tvName)).check(matches(withText("E.S. CARBURANTES DE ARNUERO S.L.")));
        gas.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gas.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));

        gas = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(3);
        gas.onChildView(withId(R.id.tvName)).check(matches(withText("COBO")));
        gas.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gas.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));

        gas = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(3);
        gas.onChildView(withId(R.id.tvName)).check(matches(withText("REPSOL")));
        gas.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gas.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));*/



    }
}
