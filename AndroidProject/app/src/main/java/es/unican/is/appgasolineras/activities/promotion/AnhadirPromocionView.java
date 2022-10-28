package es.unican.is.appgasolineras.activities.promotion;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.common.utils.MultipleSpinner;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

/**
 * Vista abierta para anhadir una promocion.
 */
public class AnhadirPromocionView extends AppCompatActivity implements IAnhadirPromocionContract.View {
    private IAnhadirPromocionContract.Presenter presenter;
    private List<String> checkedBrandBoxes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_promocion);


        presenter = new AnhadirPromocionPresenter(this);
        presenter.init();
        checkedBrandBoxes = new ArrayList<>();
        this.init();
    }


    public void init() {
        /*MultipleSpinner spinnerMarcas = findViewById(R.id.marcasSp);
        initializeMultipleSpinnerMarcas(spinnerMarcas);*/
        Spinner spinnerCombustibles = findViewById(R.id.combustiblesSp);
        initializeSpinnerCombustibles(spinnerCombustibles);
        Spinner spinnerInicial = findViewById(R.id.selectorSp);
        Spinner spinnerFinal = findViewById(R.id.selector2Sp);
        initializeSpinnersSelector(spinnerInicial, spinnerFinal);
        Spinner spinnerTipoDescuento = findViewById(R.id.tipoDescuentoSp);
        initializeSpinnerTipoDescuento(spinnerTipoDescuento);
    }


    private void initializeSpinnerTipoDescuento(Spinner spinnerTipoDescuento) {
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this.getApplicationContext(),R.array.type_descuento_array,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerTipoDescuento.setAdapter(arrayAdapter);
    }

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

    private void initializeMultipleSpinnerMarcas(MultipleSpinner spinnerMarcas) {
        String[] marcas = getResources().getStringArray(R.array.brands_types_array);
        spinnerMarcas.setElementos(marcas, "Varias", "-");
        spinnerMarcas.setSelectedStrings(checkedBrandBoxes);
    }

    private void initializeSpinnerCombustibles(Spinner spinnerCombustibles) {
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this.getApplicationContext(),R.array.combustible_types_array,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerCombustibles.setAdapter(arrayAdapter);
    }

    @Override
    public IGasolinerasRepository getGasolineraRepository() {
        return new GasolinerasRepository(this);
    }
}
