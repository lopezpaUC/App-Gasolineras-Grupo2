package es.unican.is.appgasolineras.activities.main;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.activities.detail.IGasolineraDetailContract;
import es.unican.is.appgasolineras.model.Gasolinera;



public class MainPresenterTest extends TestCase {

    @Mock
    IMainContract.View mockMainPresenter;



    @Test
    public void testFiltrarPorMarca() {
    }
    @Test
    public void filterByCombustibleTest() {
        MainPresenter m = new MainPresenter(null);

    }

}
