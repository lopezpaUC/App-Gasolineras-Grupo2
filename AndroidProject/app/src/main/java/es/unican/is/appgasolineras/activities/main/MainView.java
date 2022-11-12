package es.unican.is.appgasolineras.activities.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.detail.GasolineraDetailView;
import es.unican.is.appgasolineras.activities.info.InfoView;
import es.unican.is.appgasolineras.activities.promotion.AnhadirPromocionView;
import es.unican.is.appgasolineras.activities.promotion.ListaPromocionesView;
import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.common.utils.MultipleSpinner;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

/**
 * Vista principal abierta al iniciar la aplicacion.
 */
public class MainView extends AppCompatActivity implements IMainContract.View {
    private IMainContract.Presenter presenter;
    private List<String> checkedBrandBoxes;

    /*
    Activity lifecycle methods
     */

    /**
     * This method is automatically called when the activity is created
     * It fills the activity with the widgets (buttons, lists, etc.)
     * @param savedInstanceState Bundle que permite restaurar estado previo.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);
        presenter.init();

        checkedBrandBoxes = new ArrayList<>();

        this.init();

        SharedPreferences filterPref = this.getSharedPreferences(getString(R.string.preference_filter_file_key_),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = filterPref.edit();
        editor.putInt(getString(R.string.saved_comb_type_filter), 0);
        editor.apply();
    }

    /**
     * Create a menu in this activity (three dot menu on the top left)
     * @param menu Menu de la actividad principal.
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * This is the listener to the three-dot menu on the top left
     * @param item Item seleccionado del menu.
     * @return true
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

                checkedBrandBoxes = new ArrayList<>();
                return true;
            case R.id.menuFilter:
                presenter.onFilterClicked();
                return true;
            case R.id.menuAnahdirPromocion:
                presenter.onAddPromotionClicked();
                return true;
            case R.id.menuListaPromociones:
                presenter.onListPromotionsClicked();
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
        lvGasolineras.setOnItemClickListener((parent, view, position, id)
                -> presenter.onGasolineraClicked(position));
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
    public void showGasolinerasAdvanced(List<Gasolinera> shownGasolineras,
                                        CombustibleType combustibleDestacado) {
        GasolinerasArrayAdapter adapter;
        switch (combustibleDestacado) {
            case GASOLINA:
                adapter = new GasolinerasArrayAdapter(this, shownGasolineras,
                        getResources().getString(R.string.gasolina95label));
                break;
            case DIESEL:
                adapter = new GasolinerasArrayAdapter(this, shownGasolineras,
                        getResources().getString(R.string.dieselAlabel));
                break;
            default:
                adapter = new GasolinerasArrayAdapter(this, shownGasolineras);
                break;
        }

        // Actualiza la lista
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
    public void showLoadEmpty() {
        String text = getResources().getString(R.string.loadErrorEmpty);
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
    public void openAnhadirPromocionView() {
        Intent intent = new Intent(this, AnhadirPromocionView.class);
        startActivity(intent);
    }

    @Override
    public void openListaPromocionesView() {
        Intent intent = new Intent(this, ListaPromocionesView.class);
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

        // Inicializacion spinner marcas
        MultipleSpinner spinnerMarcas = dialogFilter.findViewById(R.id.spnMarca);
        initializeSpinnerMarcas(spinnerMarcas);

        // Inicializacion checkbox
        CheckBox chckLowcost = dialogFilter.findViewById(R.id.chckLowcost);

        // Listener para boton de informacion
        ImageButton btnInfo = dialogFilter.findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton(getResources().getString(R.string.accept),
                    ((DialogInterface dialogInterface, int i) -> {
                        dialogInterface.cancel();
                    }));
            builder.setTitle(getResources().getString(R.string.marcasLowcost));

            StringBuilder sb = new StringBuilder();
            sb.append(getResources().getString(R.string.instLowcost));
            sb.append("\n\n");

            for (String s:getResources().getStringArray(R.array.lowcost_array)) {
                sb.append("-> ");
                sb.append(s);
                sb.append("\n");
            }
            builder.setMessage(sb.toString());
            AlertDialog ad = builder.create();
            ad.show();
        });

        // Listener para aplicar
        tvAplicar.setOnClickListener(view -> {
            // Obtener estado del checkbox para gasolineras lowcost
            boolean mostrarSoloLowcost = chckLowcost.isChecked();

            // Guardar en el atributo las marcas seleccionadas
            checkedBrandBoxes = spinnerMarcas.getSelectedStrings();

            // Actualizar lista
            int itemPositionComb = spinnerCombustible.getSelectedItemPosition();
            updateList(itemPositionComb, checkedBrandBoxes, mostrarSoloLowcost);

            // Guardar el filtro por tipo de combustible
            saveIntPrefFilter(getString(R.string.saved_comb_type_filter), itemPositionComb);

            dialogFilter.dismiss();
        });

        // Listener para cancelar
        tvCancelar.setOnClickListener(view -> dialogFilter.dismiss());

        // Mostrar ventana de filtro
        dialogFilter.show();
    }

    /**
     * Inicializa el spinner del filtro por tipo de combustible.
     * @param spinnerCombustible Spinner con las opciones de tipos de combustibles.
     * @param dialogFilter Dialogo que contiene los elementos del filtro.
     * @return Adapter generado en la inicializacion
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
     * Inicializa el spinner del filtro por marcas.
     * @param spinnerMarcas Spinner con las opciones de marcas.
     */
    private void initializeSpinnerMarcas(MultipleSpinner spinnerMarcas) {
        String[] marcas = getResources().getStringArray(R.array.brands_types_array);
        spinnerMarcas.setElementos(marcas, getResources().getString(R.string.varias), "-");
        spinnerMarcas.setSelectedStrings(checkedBrandBoxes);
    }

    /**
     * Actualiza la lista de gasolineras en función  del tipo de combustible.
     *
     * @param itemPositionComb Posicion marcada en el filtro por tipo de combustible.
     * @param marcasSeleccionadas Gasolineras marcadas para ser mostradas en el filtro por marcas.
     * @param lowcost Booleano que indica si solo se deben mostrar gasolineras lowcost (true).
     */
    private void updateList(int itemPositionComb, List<String> marcasSeleccionadas, boolean lowcost) {
        // Convierte la posicion a un tipo de combustible, para mayor claridad
        CombustibleType combustibleSeleccionado = CombustibleType.getCombTypeFromInt(
                itemPositionComb);

        // Solicita al presenter que realice el filtrado y actualice las gasolineras a mostrar
        presenter.filter(combustibleSeleccionado, marcasSeleccionadas, lowcost);
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
