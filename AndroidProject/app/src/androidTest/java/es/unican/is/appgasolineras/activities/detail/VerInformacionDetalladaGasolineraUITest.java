package es.unican.is.appgasolineras.activities.detail;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

public class VerInformacionDetalladaGasolineraUITest {

    @Rule
    public ActivityScenarioRule<MainView> activityRule =
            new ActivityScenarioRule<>(MainView.class);

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL();
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Test
    public void VerInformacionDetalladaGasolineraTest() {

        // Datos correctos
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0).perform(click());
        onView(withId(R.id.tvDireccion)).check(matches(withText("CARRETERA 6316 KM. 10,5")));
        onView(withId(R.id.tvMunicipio)).check(matches(withText("Alfoz de Lloredo")));
        onView(withId(R.id.tvCP)).check(matches(withText("39526")));
        onView(withId(R.id.tv95PrecioDet)).check(matches(withText("1,85 €/L")));
        onView(withId(R.id.tvDieselAPrecioDet)).check(matches(withText("1,99 €/L")));
        onView(withId(R.id.tvPrecioSumarioDet)).check(matches(withText("1,91 €/L")));
        onView(withId(R.id.tvHorarioDet)).check(matches(withText("L-D: 08:00-21:00")));

        // Falta gasolina
        onView(isRoot()).perform(ViewActions.pressBack());
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(37).perform(click());
        onView(withId(R.id.tvDireccion)).check(matches(withText("PASEO ESTRADA")));
        onView(withId(R.id.tvMunicipio)).check(matches(withText("Comillas")));
        onView(withId(R.id.tvCP)).check(matches(withText("39520")));
        onView(withId(R.id.tv95PrecioDet)).check(matches(withText("- €/L")));
        onView(withId(R.id.tvDieselAPrecioDet)).check(matches(withText("1,97 €/L")));
        onView(withId(R.id.tvPrecioSumarioDet)).check(matches(withText("1,97 €/L")));
        onView(withId(R.id.tvHorarioDet)).check(matches(withText("L-V: 09:00-19:00")));
    }

}
