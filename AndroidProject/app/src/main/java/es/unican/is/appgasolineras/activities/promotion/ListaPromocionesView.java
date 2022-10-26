package es.unican.is.appgasolineras.activities.promotion;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.GasolinerasArrayAdapter;
import es.unican.is.appgasolineras.model.Promocion;

public class ListaPromocionesView extends AppCompatActivity implements IListaPromocionesContract.View {

    private IListaPromocionesContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Crea la vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasolinera_detail_view);

        // Crea el presenter
        presenter = new ListaPromocionesPresenter(this);
        presenter.init();

        // Inicializa
        this.init();
        presenter.init();
    }

    @Override
    public void init() {}

    @Override
    public IPromocionesRepository getPromocionRepository() {
        return new PromocionRepository(this);
    }

    @Override
    public void showPromociones(List<Promocion> promociones) {

    }

    @Override
    public void showLoadCorrectOnline(int promocionesCount) {

    }

    @Override
    public void showLoadCorrectOffline(int promocionesCount) {

    }

    @Override
    public void showLoadError() {

    }

    @Override
    public void showPromotions(List<Promocion> promociones) {
        PromocionesArrayAdapter adapter = new PromocionesArrayAdapter(this, promociones);
        ListView list = findViewById(R.id.lvPromociones);
        list.setAdapter(adapter);
    }
}
