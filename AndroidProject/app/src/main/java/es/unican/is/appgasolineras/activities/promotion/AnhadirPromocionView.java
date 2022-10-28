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

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.common.utils.MultipleSpinner;

import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

/**
 * Vista abierta para anhadir una promocion.
 */
public class AnhadirPromocionView extends AppCompatActivity implements IAnhadirPromocionContract.View {
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

    // Elementos necesarios para hacer los spinners
    List<String> listSelector2;
    ArrayAdapter<CharSequence> arrayAdapterCombustibles, arrayAdapterSelector1, arrayAdapterTipoDescuento, arrayAdapterSelector2a;
    ArrayAdapter<String> arrayAdapterSelector2b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Establecer layout
        setContentView(R.layout.activity_crear_promocion);

        // Presenter
        presenter = new AnhadirPromocionPresenter(this);
        presenter.init();

        this.init();
    }


    public void init() {
        /*MultipleSpinner spinnerMarcas = findViewById(R.id.marcasSp);
        initializeMultipleSpinnerMarcas(spinnerMarcas);*/

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
        loadSpinnerSelector1(selectorSp);
        loadSpinnerSelector2(selector2Sp, null, false);  // Esta inicializado así pero el ultimo parametro deberia de ser true
        loadSpinnerTipoDescuento(tipoDescuentoSp);
        loadSpinnerCombustibles(combustiblesSp);


        // Listeners
        backBtn.setOnClickListener(view -> {
            /* TODO hacer que mantenga los filtros*/
            Intent intent = new Intent(this, MainView.class);
            startActivity(intent);

        });

        acceptBtn.setOnClickListener(view -> {
            /* TODO guardar promocion y aplicar la promocion*/
            /* TODO yo pondría que fuese a la vista con la lista de promociones*/
        });


//        selectorSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                int position = (int)adapterView.getItemAtPosition(i);
//                switch(position) {
//                    case 0:
//                        // Lista vacía
//
//                        loadSpinnerSelector2(selector2Sp, null, true);
//                        break;
//                    case 1:
//                        // Lista con las marcas
//
//                        loadSpinnerSelector2(selector2Sp, null, false);
//                        break;
//                    case 2:
//
//                        /* TODO hay que cambiar esto, esta para probar que el metodo funciona */
//                        // Lista con todas las gasolineras
//                        listSelector2 = new ArrayList<>();
//                        listSelector2.add("Gasolinera 1");
//                        listSelector2.add("Gasolinrea 2");
//                        loadSpinnerSelector2(selector2Sp, listSelector2, false);
//                        break;
//                }
//            }
//
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });




    }

    @Override
    public IGasolinerasRepository getGasolineraRepository() {
        /*TODO*/
        return null;
    }




    /**
     * Inicializador de spinner que selecciona el tipo de descuento:
     *      - Euros por litro
     *      - Porcentaje
     */
    private void loadSpinnerTipoDescuento(Spinner spinnerTipoDescuento) {
        arrayAdapterTipoDescuento = ArrayAdapter.createFromResource(this.getApplicationContext(), R.array.type_descuento_array,
                android.R.layout.simple_spinner_item);
        arrayAdapterTipoDescuento.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerTipoDescuento.setAdapter(arrayAdapterTipoDescuento);
    }


    /**
     * Inicializador del spinner en el que se selecciona de que forma se va a aplicar la promocion:
     *      - A todas las gasolineras
     *      - A una serie de gasolineras
     *      - A una marca en concreto
     */
    private void loadSpinnerSelector1(Spinner spinnerInicial) {
        arrayAdapterSelector1 = ArrayAdapter.createFromResource(this.getApplicationContext(), R.array.selectors_types_array,
                android.R.layout.simple_spinner_item);
        arrayAdapterSelector1.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerInicial.setAdapter(arrayAdapterSelector1);



    }

    /**
     * Inicializador del spinner o multispinner que es dependiente del spinner que selecciona a que gasolineras se aplica
     * la promocion
     * @param spinnerFinal
     * @param lista_gasolineras
     * @param todas
     */
    private void loadSpinnerSelector2(Spinner spinnerFinal, List<String> lista_gasolineras, boolean todas) {

        // Caso en el que se selecciona por marca
        if(!todas && lista_gasolineras == null) {
            arrayAdapterSelector2a = ArrayAdapter.createFromResource(this.getApplicationContext(), R.array.brands_types_array,
                    android.R.layout.simple_spinner_item);
            arrayAdapterSelector2a.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            spinnerFinal.setAdapter(arrayAdapterSelector2a);


            // Caso en el que se selecciona por todas
        } else if (todas) {
            listSelector2 = new ArrayList<String>();
            lista_gasolineras.add("");
            arrayAdapterSelector2b = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lista_gasolineras);
            spinnerFinal.setAdapter(arrayAdapterSelector2b);

            // Caso en el que se selecciona por gasolinera
        } else if(!todas && lista_gasolineras != null) {
            /* TODO pasar la lista al multispinner. Hay que cambiar el spinner por el multispinner*/
            listSelector2 = new ArrayList<String>();
            lista_gasolineras.add("");
            arrayAdapterSelector2b = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lista_gasolineras);
            spinnerFinal.setAdapter(arrayAdapterSelector2b);
        }
    }

    /**
     * Inicializador de MultiSpinner
     */
    private void initializeMultipleSpinnerMarcas(MultipleSpinner spinnerMarcas) {
        String[] marcas = getResources().getStringArray(R.array.brands_types_array);
        spinnerMarcas.setElementos(marcas, "Varias", "-");

    }


    /**
     * Inicializador de spinner de combustibles.
     */
    private void loadSpinnerCombustibles(Spinner spinnerCombustibles) {
        arrayAdapterCombustibles = ArrayAdapter.createFromResource(this.getApplicationContext(), R.array.combustible_types_array,
                android.R.layout.simple_spinner_item);
        arrayAdapterCombustibles.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerCombustibles.setAdapter(arrayAdapterCombustibles);
    }






    }

