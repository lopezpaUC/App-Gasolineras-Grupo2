package es.unican.is.appgasolineras.activities.promotion;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

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
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.repository.rest.GasolinerasAPI;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(AndroidJUnit4.class)

public class EliminarPromotionUITest {
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
    public void testEliminar() {
        onView(withId(R.id.menuPromotion)).perform(click());
        onView(withId(R.id.menuListaPromociones)).perform(click());
        //onView((withId(R.id.ivBin))).perform(click());
    }
}
