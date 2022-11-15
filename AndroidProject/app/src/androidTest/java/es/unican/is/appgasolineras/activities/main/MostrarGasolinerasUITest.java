package es.unican.is.appgasolineras.activities.main;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static es.unican.is.appgasolineras.utils.Matchers.hasElements;

import android.Manifest;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;
import es.unican.is.appgasolineras.utils.ScreenshotTestRule;

public class MostrarGasolinerasUITest {

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
        GasolinerasServiceConstants.setStaticURL();
    }

    @AfterClass
    public static void clean() {
        GasolinerasService.resetAPI();
        GasolinerasServiceConstants.setMinecoURL();
    }

    @Test
    public void openDetailViewTest() {
        onView(withId(R.id.lvGasolineras)).check(matches(hasElements()));
    }

}
