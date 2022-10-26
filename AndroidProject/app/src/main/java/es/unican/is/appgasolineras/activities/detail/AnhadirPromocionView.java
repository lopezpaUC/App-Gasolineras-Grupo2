package es.unican.is.appgasolineras.activities.detail;

import android.os.Bundle;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.common.utils.MultipleSpinner;

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
        checkedBrandBoxes = new ArrayList<>();
        this.init();
    }


    public void init() {
        MultipleSpinner spinnerCombustible = findViewById(R.id.combustiblesSp);
        initializeSpinnerMarcas(spinnerCombustible);
    }

    private void initializeSpinnerMarcas(MultipleSpinner spinnerMarcas) {
        String[] marcas = getResources().getStringArray(R.array.brands_types_array);
        spinnerMarcas.setElementos(marcas, "Varias", "-");
        spinnerMarcas.setSelectedStrings(checkedBrandBoxes);
    }

}
