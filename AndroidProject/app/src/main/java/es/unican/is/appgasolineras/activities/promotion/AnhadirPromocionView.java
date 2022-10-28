package es.unican.is.appgasolineras.activities.promotion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.common.utils.MultipleSpinner;

import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

/**
 * Vista abierta para anhadir una promocion.
 */
public class AnhadirPromocionView extends AppCompatActivity
        implements IAnhadirPromocionContract.View {

    private static final int TODAS = 0;
    private static final int MARCAS = 1;
    private static final int GASOLINERAS = 2;

    private IAnhadirPromocionContract.Presenter presenter; // Presenter de la vista

    // Elementos de la vista
    private EditText etNombre;
    private Spinner spCombustibles;
    private Spinner spCriterioGasolineras;
    private MultipleSpinner spGasolinerasAplicablesMp;
    private Spinner spGasolinerasAplicables;
    private EditText etDescuento;
    private Spinner spTipoDescuento;
    private Button btnCancelar;
    private Button btnAnhadir;

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

    public void init() {
        /*MultipleSpinner spinnerMarcas = findViewById(R.id.marcasSp);
        initializeMultipleSpinnerMarcas(spinnerMarcas);*/

        // Identifica los diferentes elementos a utilizar de la vista
        etNombre = findViewById(R.id.etNombre);
        spCombustibles = findViewById(R.id.spCombustibles);
        spCriterioGasolineras = findViewById(R.id.spCriterioGasolineras);

        spGasolinerasAplicables = findViewById(R.id.spGasolinerasAplicables);
        spGasolinerasAplicables.setVisibility(View.GONE);

        spGasolinerasAplicablesMp = findViewById(R.id.spMultipleGasolinerasMarcas);
        spGasolinerasAplicablesMp.setVisibility(View.GONE);

        etDescuento = findViewById(R.id.etDescuento);
        spTipoDescuento = findViewById(R.id.spTipoDescuento);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnAnhadir = findViewById(R.id.btnAnhadir);


        // Inicializar spinner de combustibles
        initializeSpinnerRadioButton(spCombustibles, R.array.combustible_types_array,
                android.R.layout.select_dialog_singlechoice);

        // Inicializar spinner de criterio para aplicar promocion a gasolineras
        initializeSpinnerRadioButton(spCriterioGasolineras, R.array.selectors_types_array,
                android.R.layout.select_dialog_singlechoice);

        // Inicializar spinner de tipos de descuentos
        initializeSpinnerRadioButton(spTipoDescuento, R.array.type_descuento_array,
                android.R.layout.select_dialog_singlechoice);

        // Inicializa funcionalidad de botones
        btnCancelar.setOnClickListener(view -> {
            launchMainActivity();
        });

        btnAnhadir.setOnClickListener(view -> {
            launchMainActivity();
        });

        spCriterioGasolineras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> av, View view, int pos, long id) {
                switch (pos) {
                    case TODAS:
                        spGasolinerasAplicablesMp.setVisibility(View.GONE);
                        spGasolinerasAplicables.setVisibility(View.GONE);
                        break;

                    case MARCAS:
                        spGasolinerasAplicables.setVisibility(View.GONE);
                        spGasolinerasAplicablesMp.setVisibility(View.VISIBLE);

                        String[] marcas = getResources().getStringArray(R.array.brands_types_array);
                        spGasolinerasAplicablesMp.setElementos(marcas, getResources().getString(R.string.varias), "-");
                        break;

                    case GASOLINERAS:
                        spGasolinerasAplicablesMp.setVisibility(View.GONE);
                        spGasolinerasAplicables.setVisibility(View.VISIBLE);

                        // TODO: PONER LISTA DE GASOLINERAS
                        initializeSpinnerRadioButton(spGasolinerasAplicables, R.array.combustible_types_array,
                                android.R.layout.select_dialog_singlechoice);
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
        /*TODO*/
        return null;
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

