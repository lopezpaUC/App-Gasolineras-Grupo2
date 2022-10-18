package es.unican.is.appgasolineras.activities.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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
    private Set<String> recordar = new HashSet<>();

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
        SharedPreferences filterPref = this.getSharedPreferences(getString(R.string.preference_filter_file_key_),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = filterPref.edit();
        editor.putInt(getString(R.string.saved_comb_type_filter), 0);
        editor.apply();
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
                SharedPreferences filterPref = this.getSharedPreferences(getString(R.string.preference_filter_file_key_),
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = filterPref.edit();
                editor.putInt(getString(R.string.saved_comb_type_filter), 0);
                editor.apply();
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
        Dialog dialogFilter = new Dialog(MainView.this);

        // Deshabilitar titulo (ya asignado en layout)
        dialogFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFilter.setContentView(R.layout.activity_main_filter);

        // Inicializar elementos
        TextView tvCancelar = dialogFilter.findViewById(R.id.tvCancel);
        TextView tvAplicar = dialogFilter.findViewById(R.id.tvApply);

        // Inicializacion spinner tipo combustible
        Spinner spinnerCombustible = dialogFilter.findViewById(R.id.spnTipoCombustible);
        initializeSpinnerCombType(spinnerCombustible, dialogFilter);

        // Inicializacion spinner tipo marca
        final String[] select_qualification = {
                "Marcas", "Avia", "Campsa", "Carrefour", "Cepsa", "Galp",
                "Petronor", "Repsol", "Shell"};


        final Spinner spinnerMulti = dialogFilter.findViewById(R.id.spnMarca);

        ArrayList<StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(select_qualification[i]);
            if (!recordar.isEmpty()){
                System.out.println("* entry");
                for (String s:recordar) {
                    if (s.equals(select_qualification[i])){
                        stateVO.setSelected(true);
                    }
                }

            }else {
                stateVO.setSelected(false);
            }

            listVOs.add(stateVO);
        }
        MyAdapter myAdapter = new MyAdapter(this, 0,
                listVOs);


        spinnerMulti.setAdapter(myAdapter);

        // Listener para aplicar
        tvAplicar.setOnClickListener(view -> {
            // Actualizar lista
            int itemPositionComb = spinnerCombustible.getSelectedItemPosition();

            CombustibleType combustibleSeleccionado = CombustibleType.getCombTypeFromInt(itemPositionComb);
            presenter.filter(combustibleSeleccionado, myAdapter.sumChecked());
            GasolinerasArrayAdapter adapter;

            switch (combustibleSeleccionado) {
                case ALL_COMB:
                    adapter = new GasolinerasArrayAdapter(this, presenter.getShownGasolineras());
                    break;
                case DIESEL:
                    adapter = new GasolinerasArrayAdapter(this, presenter.getShownGasolineras(),
                            getResources().getString(R.string.dieselAlabel));
                    break;
                default:
                    adapter = new GasolinerasArrayAdapter(this, presenter.getShownGasolineras(),
                            getResources().getString(R.string.gasolina95label));
                    break;
            }

            ListView list = findViewById(R.id.lvGasolineras);
            list.setAdapter(adapter);


            recordar = (Set<String>) myAdapter.sumChecked();
            System.out.println(recordar + "*");
            dialogFilter.dismiss();
        });






        // Listener para aplicar
        tvAplicar.setOnClickListener(view -> {
            // Actualizar lista
            int itemPositionComb = spinnerCombustible.getSelectedItemPosition();
            updateListByGasType(itemPositionComb, myAdapter.sumChecked());

            // Guardar el filtro
            saveIntPrefFilter(getString(R.string.saved_comb_type_filter), itemPositionComb);

            dialogFilter.dismiss();
        });




        // Listener para cancelar
        tvCancelar.setOnClickListener(view -> {
            dialogFilter.dismiss();
        });

        // Mostrar ventana de filtro
        dialogFilter.show();
    }

    /**
     * Inicializa el spinner del filtro por tipo de combustible.
     * @param spinnerCombustible Spinner con las opciones de tipos de combustibles.
     * @param dialogFilter Dialogo que contiene los elementos del filtro.
     */
    private void initializeSpinnerCombType(Spinner spinnerCombustible, Dialog dialogFilter) {
        // ArrayAdapter con los tipos de combustibles
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                dialogFilter.getContext(), R.array.combustible_types_array,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerCombustible.setAdapter(arrayAdapter);

        // Recupera la seleccion previa a cerrar la ventana
        SharedPreferences filterPref = this.getSharedPreferences(getString(R.string.preference_filter_file_key_),
                Context.MODE_PRIVATE);
        int savedCombValue = filterPref.getInt(getString(R.string.saved_comb_type_filter), 0);
        spinnerCombustible.setSelection(savedCombValue);
    }

    /**
     * Actualiza la lista de gasolineras en función  del tipo de combustible.
     *
     * @param itemPositionComb Posicion marcada en el filtro por tipo de combustible.
     */
    private void updateListByGasType(int itemPositionComb, ArrayList<String> sumChecked) {
        // Convierte la posicion a un tipo de combustible, para mayor claridad
        CombustibleType combustibleSeleccionado = CombustibleType.getCombTypeFromInt(
                itemPositionComb);

        // Solicita al presenter que realice el filtrado y actualice las gasolineras a mostrar
        presenter.filter(combustibleSeleccionado, sumChecked);

        // Prepara ArrayAdapter para la lista a mostrar
        GasolinerasArrayAdapter adapter;
        switch (combustibleSeleccionado) {
            case GASOLINA:
                adapter = new GasolinerasArrayAdapter(this, presenter.getShownGasolineras(),
                        getResources().getString(R.string.gasolina95label));
                break;
            case DIESEL:
                adapter = new GasolinerasArrayAdapter(this, presenter.getShownGasolineras(),
                        getResources().getString(R.string.dieselAlabel));
                break;
            default:
                adapter = new GasolinerasArrayAdapter(this, presenter.getShownGasolineras());
                break;
        }

        // Actualiza la lista
        ListView list = findViewById(R.id.lvGasolineras);
        list.setAdapter(adapter);
    }

    /**
     * Guarda un entero relacionado con los filtros.
     *
     * @param key Clave para realizar la persistencia.
     * @param value Valor entero a guardar.
     */
    private void saveIntPrefFilter(String key, int value) {
        // Obtiene Preference de los filtros
        SharedPreferences filterPref = this.getSharedPreferences(getString(R.string.preference_filter_file_key_),
                Context.MODE_PRIVATE);

        // Guarda el valor
        SharedPreferences.Editor editor = filterPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
