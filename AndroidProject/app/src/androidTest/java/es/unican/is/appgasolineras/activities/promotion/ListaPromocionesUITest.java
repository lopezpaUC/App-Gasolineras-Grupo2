package es.unican.is.appgasolineras.activities.promotion;

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

import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.repository.PromocionesRepository;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

public class ListaPromocionesUITest {

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
    public void testListaPromociones() {
        // Caso valido: Carga de lista de promociones completa
        /* Se hace click sobre el boton de ver lista promociones en el menu de promociones.*/
        onView(withId(R.id.menuPromotion)).perform(click());
        //onData(withId(R.id.menuListaPromociones)).perform(click());

        /* Comprobamos los datos de una promocion, los cuáles deberían ser los esperados.*/
        /**DataInteraction promocion = onData(anything()).inAdapterView(withId(R.id.lvPromociones)).atPosition(0);
        promocion.onChildView(withId(R.id.tvNamePromocion)).check(matches(withText("Promocion")));
        promocion.onChildView(withId(R.id.tvNameGasolinera)).check(matches(withText("Cepsa")));
        promocion.onChildView(withId(R.id.tvDescuento)).check(matches(withText("0.2")));
        promocion.onChildView(withId(R.id.tvCombustible)).check(matches(withText("Diesel")));*/

    }

}
