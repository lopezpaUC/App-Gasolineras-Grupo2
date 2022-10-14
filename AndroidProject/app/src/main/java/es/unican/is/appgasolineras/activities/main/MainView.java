package es.unican.is.appgasolineras.activities.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.activities.detail.GasolineraDetailView;
import es.unican.is.appgasolineras.activities.info.InfoView;

public class MainView extends AppCompatActivity implements IMainContract.View {
    private static final int ALL_COMB = 0;
    private static final int DIESEL = 1;
    private static final int GASOLINA = 2;
    private IMainContract.Presenter presenter;

    /*
    Activity lifecycle methods
     */

    /**
     * This method is automatically called when the activity is created
     * It fills the activity with the widgets (buttons, lists, etc.)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);
        presenter.init();
        this.init();
    }

    /**
     * Create a menu in this activity (three dot menu on the top left)
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * This is the listener to the three-dot menu on the top left
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuInfo:
                presenter.onInfoClicked();
                return true;
            case R.id.menuRefresh:
                presenter.onRefreshClicked();
                return true;
            case R.id.menuFilter:
                presenter.onFilterClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    IMainContract.View methods
     */

    @Override
    public void init() {
        // init UI listeners
        ListView lvGasolineras = findViewById(R.id.lvGasolineras);
        lvGasolineras.setOnItemClickListener((parent, view, position, id) -> {
            presenter.onGasolineraClicked(position);
        });
    }

    @Override
    public IGasolinerasRepository getGasolineraRepository() {
        return new GasolinerasRepository(this);
    }

    @Override
    public void showGasolineras(List<Gasolinera> gasolineras) {
        GasolinerasArrayAdapter adapter = new GasolinerasArrayAdapter(this, gasolineras);
        ListView list = findViewById(R.id.lvGasolineras);
        list.setAdapter(adapter);
    }

    @Override
    public void showLoadCorrectOnline(int gasolinerasCount) {
        String text = getResources().getString(R.string.loadCorrectOn);
        Toast.makeText(this, String.format(text, gasolinerasCount),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadCorrectOffline(int gasolinerasCount) {
        String text = getResources().getString(R.string.loadCorrectOff);
        Instant lastDownloaded = Prefs.from(this).getInstant("KEY_LAST_SAVED");
        Date fecha = Date.from(lastDownloaded);
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
        String fechaStr = formateador.format(fecha);

        Toast.makeText(this, String.format(text, gasolinerasCount, fechaStr),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadError() {
        String text = getResources().getString(R.string.loadError);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openGasolineraDetails(Gasolinera gasolinera) {
        Intent intent = new Intent(this, GasolineraDetailView.class);
        intent.putExtra(GasolineraDetailView.INTENT_GASOLINERA, gasolinera);
        startActivity(intent);
    }

    @Override
    public void openInfoView() {
        Intent intent = new Intent(this, InfoView.class);
        startActivity(intent);
    }

    @Override
    public void openFilterDialog() {
        final Dialog dialogFilter = new Dialog(MainView.this);

        // Deshabilitar titulo (ya asignado en layout)
        dialogFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogFilter.setContentView(R.layout.activity_main_filter);

        // Inicializar elementos
        final TextView tvCancelar = dialogFilter.findViewById(R.id.tvCancel);
        final TextView tvAplicar = dialogFilter.findViewById(R.id.tvApply);

        // Inicializacion spinner tipo combustible
        final Spinner spinnerCombustible = dialogFilter.findViewById(R.id.spnTipoCombustible);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(dialogFilter.getContext(),
                R.array.combustible_types_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCombustible.setAdapter(arrayAdapter);

        // Inicializacion spinner tipo marca
        //TODO

        // Listener para aplicar
        tvAplicar.setOnClickListener(view -> {
            // Tipo combustible
            switch (spinnerCombustible.getSelectedItemPosition()) {
                case DIESEL:
                    presenter.filterByCombustible(DIESEL);
                case GASOLINA:
                    presenter.filterByCombustible(GASOLINA);
                    break;
                default:
                    presenter.filterByCombustible(ALL_COMB);
                    break;
            }

            GasolinerasArrayAdapter adapter = new GasolinerasArrayAdapter(this, presenter.getShownGasolineras());
            ListView list = findViewById(R.id.lvGasolineras);
            list.setAdapter(adapter);
            dialogFilter.dismiss();
        });

        // Listener para cancelar
        tvCancelar.setOnClickListener(view -> {
            dialogFilter.dismiss();
        });
        dialogFilter.show();
    }

}
