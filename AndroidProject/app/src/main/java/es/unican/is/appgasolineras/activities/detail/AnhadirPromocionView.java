package es.unican.is.appgasolineras.activities.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.common.utils.MultipleSpinner;

import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

/**
 * Vista abierta para anhadir una promocion.
 */
public class AnhadirPromocionView extends AppCompatActivity implements IAnhadirPromocionContract.View, View.OnClickListener {
    private IAnhadirPromocionContract.Presenter presenter;

    // Elementos de la vista
    private EditText nombreEditText;
    private Spinner combustiblesSp;
    private Spinner selectorSp;
    private Spinner selector2Sp;
    private EditText descuentoEditText;
    private Spinner tipoDescuentoSp;
    private Button backBtn;
    private Button acceptBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Establecer layout
        setContentView(R.layout.activity_crear_promocion);

        // Presenter
        presenter = new AnhadirPromocionPresenter(this);
        presenter.init();

        // Enlazar elementos de la vista
        nombreEditText = findViewById(R.id.nombreEditText);
        combustiblesSp = findViewById(R.id.combustiblesSp);
        selectorSp = findViewById(R.id.selectorSp);
        selector2Sp = findViewById(R.id.selector2Sp);
        descuentoEditText = findViewById(R.id.descuentoEditText);
        tipoDescuentoSp = findViewById(R.id.tipoDescuentoSp);
        backBtn = findViewById(R.id.backBtn);
        acceptBtn = findViewById(R.id.acceptBtn);


        // Inicializar spinners
        initializeSpinnersSelector(selectorSp, selector2Sp);
        initializeSpinnerTipoDescuento(tipoDescuentoSp);
        initializeSpinnerCombustibles(combustiblesSp);


        this.init();
    }


    public void init() {
        /*MultipleSpinner spinnerMarcas = findViewById(R.id.marcasSp);
        initializeMultipleSpinnerMarcas(spinnerMarcas);*/


    }

    @Override
    public IGasolinerasRepository getGasolineraRepository() {
        /*TODO*/
        return null;
    }


    /**
     * Inicializador de spinner
     */
    private void initializeSpinnerTipoDescuento(Spinner spinnerTipoDescuento) {
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this.getApplicationContext(), R.array.type_descuento_array,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerTipoDescuento.setAdapter(arrayAdapter);
    }


    /**
     * Inicializador de spinner
     */
    private void initializeSpinnersSelector(Spinner spinnerInicial, Spinner spinnerFinal) {
        ArrayAdapter<CharSequence> arrayAdapter1 = ArrayAdapter.createFromResource(this.getApplicationContext(), R.array.selectors_types_array,
                android.R.layout.simple_spinner_item);
        arrayAdapter1.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerInicial.setAdapter(arrayAdapter1);

        ArrayAdapter<CharSequence> arrayAdapter2 = ArrayAdapter.createFromResource(this.getApplicationContext(), R.array.brands_types_array2,
                android.R.layout.simple_spinner_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerFinal.setAdapter(arrayAdapter2);

    }


    /**
     * Inicializador de MultiSpinner
     */
    private void initializeMultipleSpinnerMarcas(MultipleSpinner spinnerMarcas) {
        String[] marcas = getResources().getStringArray(R.array.brands_types_array);
        spinnerMarcas.setElementos(marcas, "Varias", "-");

    }


    /**
     * Inicializador de spinner
     */
    private void initializeSpinnerCombustibles(Spinner spinnerCombustibles) {
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this.getApplicationContext(), R.array.combustible_types_array,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerCombustibles.setAdapter(arrayAdapter);
    }

    


    @Override
    public void onClick(View view) {
        /* CELSO
        if(view.getId() == R.id.acceptBtn) {

            // Convierto datos
            String n = nombreEditText.getText().toString();
            String c= combustiblesSp.getSelectedItem().toString();
            String m = selectorSp.getSelectedItem().toString();
            String m2 = selector2Sp.getSelectedItem().toString();
            *//* TODO mismo proceso para la lista de gasolineras *//*
            Double d = Double.parseDouble(descuentoEditText.getText().toString());
            String e = tipoDescuentoSp.getSelectedItem().toString();

            // Anhado promocion
            presenter.anhadePromocion(n, c, m, m2, d, e, null);
        *//* TODO Salir de la view */
            
        }
    }

