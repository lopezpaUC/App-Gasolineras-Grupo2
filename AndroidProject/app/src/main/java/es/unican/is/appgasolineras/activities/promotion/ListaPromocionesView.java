package es.unican.is.appgasolineras.activities.promotion;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.GasolinerasArrayAdapter;
import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionRepository;
import es.unican.is.appgasolineras.repository.PromocionRepository;

public class ListaPromocionesView extends AppCompatActivity implements IListaPromocionesContract.View {

    private IListaPromocionesContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Crea la vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions_list);

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
    public IPromocionRepository getPromocionRepository() {
        return new PromocionRepository(this);
    }

    @Override
    public IGasolinerasRepository getGasolineraRepository() {
        return new GasolinerasRepository(this);
    }

    @Override
    public void showLoadCorrect(int promocionesCount) {
        String text = getResources().getString(R.string.loadCorrectPromociones);
        Instant lastDownloaded = Prefs.from(this).getInstant("KEY_LAST_SAVED");
        Toast.makeText(this, String.format(text, promocionesCount), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadError() {
        String text = getResources().getString(R.string.loadErrorPromociones);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPromociones(List<Promocion> promociones, List<String> lista) {
        PromocionesArrayAdapter adapter = new PromocionesArrayAdapter(this, promociones, lista);
        ListView list = findViewById(R.id.lvPromociones);
        list.setAdapter(adapter);
    }
}
