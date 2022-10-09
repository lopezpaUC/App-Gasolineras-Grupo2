package es.unican.is.appgasolineras.activities.detail;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;


import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.IMainContract;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

public class MostrarInformacionGasolinerasUITest {

    @Rule
    public ActivityScenarioRule<MainView> activityRule =
            new ActivityScenarioRule<>(MainView.class);

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL();
    }

    /**@Before
    public void initialize() {
        mainView = new MainView();
        detailView = new GasolineraDetailView();
    }*/

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Test
    public void mostrarInformacionGasolineraTest() {
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0).perform(click());

        onView(withId(R.id.tvDireccion)).check(matches(withText("CARRETERA 6316 KM. 10,5")));
        onView(withId(R.id.tvMunicipio)).check(matches(withText("Alfoz de Lloredo")));
        onView(withId(R.id.tvCP)).check(matches(withText("39526")));
        onView(withId(R.id.tv95PrecioDet)).check(matches(withText("1,859 €/L")));
        onView(withId(R.id.tvDieselAPrecioDet)).check(matches(withText("1,999 €/L")));
        onView(withId(R.id.tvPrecioSumarioDet)).check(matches(withText("1,906 €/L")));
        onView(withId(R.id.tvHorarioDet)).check(matches(withText("L-D: 08:00-21:00")));
    }

    @Test
    public void comprobarLogoGasolineraCorrectoTest() {
        /**
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0).perform(click());
        String idLogoMV = onView(withId(R.id.tvRotulo)).toString().toLowerCase(Locale.ROOT);
        onView(withId(R.id.ivLogo)).check(matches(withId(R.drawable.cepsa)));*/
    }
}
