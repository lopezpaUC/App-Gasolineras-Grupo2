package es.unican.is.appgasolineras.activities.promotion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.common.utils.MultipleSpinner;

import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;
import es.unican.is.appgasolineras.repository.PromocionesRepository;

/**
 * Vista abierta para anhadir una promocion.
 */
public class AnhadirPromocionView extends AppCompatActivity
        implements IAnhadirPromocionContract.View {

    private static final int TODAS = 0;
    private static final int MARCAS = 1;
    private static final int GASOLINERAS = 2;

    private IAnhadirPromocionContract.Presenter presenter; // Presenter de la vista

    private Spinner spCriterioGasolineras;
    private MultipleSpinner spGasolinerasAplicablesMp;
    private Spinner spGasolinerasAplicables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Crea la vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_promocion);

        // Crea el presenter
        presenter = new AnhadirPromocionPresenter(this);

        // Inicializa
        this.init();
        presenter.init();
    }

    @Override
    public void init() {
        // Identifica los diferentes elementos a utilizar de la vista
        EditText etNombre = findViewById(R.id.etNombre);
        MultipleSpinner spCombustiblesMp = findViewById(R.id.spMultipleCombustibles);
        spCriterioGasolineras = findViewById(R.id.spCriterioGasolineras);

        spGasolinerasAplicables = findViewById(R.id.spGasolinerasAplicables);
        spGasolinerasAplicables.setVisibility(View.GONE);

        spGasolinerasAplicablesMp = findViewById(R.id.spMultipleGasolinerasMarcas);
        spGasolinerasAplicablesMp.setVisibility(View.GONE);

        EditText etDescuento = findViewById(R.id.etDescuento);
        Spinner spTipoDescuento = findViewById(R.id.spTipoDescuento);
        Button btnCancelar = findViewById(R.id.btnCancelar);
        Button btnAnhadir = findViewById(R.id.btnAnhadir);

        // Inicializar spinner de combustibles
        String[] combustibles = getResources().getStringArray(R.array.combustible_types_array_nall);
        spCombustiblesMp.setElementos(combustibles, getResources().getString(R.string.varios),
                "-");

        // Inicializar spinner de criterio para aplicar promocion a gasolineras
        initializeSpinnerRadioButton(spCriterioGasolineras, R.array.selectors_types_array,
                android.R.layout.select_dialog_singlechoice);

        // Inicializar spinner de tipos de descuentos
        initializeSpinnerRadioButton(spTipoDescuento, R.array.type_descuento_array,
                android.R.layout.select_dialog_singlechoice);

        // Inicializar spinner de marcas
        String[] marcas = getResources().getStringArray(R.array.brands_types_array);
        spGasolinerasAplicablesMp.setElementos(marcas, getResources().getString(R.string.varias), "-");

        // Inicializa funcionalidad de boton cancelar
        btnCancelar.setOnClickListener(view -> launchMainActivity());

        // Inicializa funcionalidad de boton anhadir
        btnAnhadir.setOnClickListener(view -> {
            // Genera dos mapas con los parametros recogidos
            Map<String, String> stringInfo = new HashMap<>();
            Map<String, List<String>> listInfo = new HashMap<>();

            // Obtiene los datos seleccionados
            stringInfo.put("idPromocion", etNombre.getText().toString());
            listInfo.put("selectedCombustibles", spCombustiblesMp.getSelectedStrings());
            stringInfo.put("selectedCriterio", spCriterioGasolineras.getSelectedItem().toString());
            stringInfo.put("selectedGasolinera", spGasolinerasAplicables.getSelectedItem().toString());
            listInfo.put("selectedMarcas", spGasolinerasAplicablesMp.getSelectedStrings());
            stringInfo.put("descuento", etDescuento.getText().toString());
            stringInfo.put("selectedDescuentoTipo", spTipoDescuento.getSelectedItem().toString());

            // Anhade informacion auxiliar
            stringInfo.put("stringValueAllGas", getResources().getString(R.string.allA));

            // Determina el mensaje a mostrar segun el estado de la operacion
            presenter.onAnhadirClicked(listInfo, stringInfo);

        });

        // Listener para el "spinner compuesto"
        spCriterioGasolineras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> av, View view, int pos, long id) {
                switch (pos) {
                    case TODAS:
                        spGasolinerasAplicablesMp.setVisibility(View.GONE);
                        spGasolinerasAplicables.setVisibility(View.GONE);
                        initializeSpinnerRadioButton(spGasolinerasAplicables,
                                R.array.empty_array,
                                android.R.layout.select_dialog_singlechoice);
                        spCriterioGasolineras.setSelection(0);
                        break;

                    case MARCAS:
                        spGasolinerasAplicables.setVisibility(View.GONE);
                        spGasolinerasAplicablesMp.setVisibility(View.VISIBLE);
                        break;

                    case GASOLINERAS:
                        spGasolinerasAplicablesMp.setVisibility(View.GONE);
                        spGasolinerasAplicables.setVisibility(View.VISIBLE);
                        String[] gasolineras = presenter.getArrayGasolinerasParsed();
                        ArrayAdapter<String> adapterGas = new ArrayAdapter<>(getApplication(),
                                android.R.layout.simple_spinner_item, gasolineras);
                        adapterGas.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                        spGasolinerasAplicables.setAdapter(adapterGas);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> av) {
                //
            }
        });
    }

    @Override
    public IGasolinerasRepository getGasolineraRepository() {
        return new GasolinerasRepository(this);
    }

    @Override
    public IPromocionesRepository getPromocionRepository() {
        return new PromocionesRepository(this);
    }

    @Override
    public void showStatus(EstadoOperacionAnhadirPromocion status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getResources().getString(R.string.accept),
                ((DialogInterface dialogInterface, int i) -> {
                    dialogInterface.cancel();
                    if (status == EstadoOperacionAnhadirPromocion.EXITO) {
                        launchMainActivity();
                    }
                }));

        switch (status) {
            case EXITO:
                builder.setTitle(getResources().getString(R.string.informacion));
                builder.setMessage(getResources().getString(R.string.promoExito));
                break;
            case REPETIDA:
                builder.setTitle(getResources().getString(R.string.error));
                builder.setMessage(getResources().getString(R.string.promoRepe));
                break;
            case SIN_COMB:
                builder.setTitle(getResources().getString(R.string.error));
                builder.setMessage(getResources().getString(R.string.promoSinComb));
                break;
            case SIN_GASOLINERA:
                builder.setTitle(getResources().getString(R.string.error));
                builder.setMessage(getResources().getString(R.string.promoSinGas));
                break;
            case SIN_DESC:
                builder.setTitle(getResources().getString(R.string.error));
                builder.setMessage(getResources().getString(R.string.promoSinDesc));
                break;
            case PORC_NO_VALIDO:
                builder.setTitle(getResources().getString(R.string.error));
                builder.setMessage(getResources().getString(R.string.promoPorcErr));
                break;
            case EURO_L_NO_VALIDO:
                builder.setTitle(getResources().getString(R.string.error));
                builder.setMessage(getResources().getString(R.string.promoEuroLErr));
                break;
            default:
                builder.setTitle(getResources().getString(R.string.error));
                builder.setMessage(getResources().getString(R.string.promoBDErr));
                break;
        }

        AlertDialog ad = builder.create();
        ad.show();
    }


    /**
     * Inicializa un spinner de radio botones.
     *
     * @param sp Spinner a inicializar
     * @param resource Recurso con las opciones a mostrar
     * @param mode Modo de presentacion (desplegable o dialogo)
     */
    private void initializeSpinnerRadioButton(Spinner sp, int resource, int mode) {
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                resource, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(mode);
        sp.setAdapter(arrayAdapter);
    }

    /**
     * Lanza actividad principal.
     */
    private void launchMainActivity() {
        Intent intent = new Intent(this, MainView.class);
        startActivity(intent);
    }
}

