package es.unican.is.appgasolineras.common.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Spinner personalizado que permite la seleccion de multiples opciones.
 */
public class MultipleSpinner extends androidx.appcompat.widget.AppCompatSpinner implements
        OnMultiChoiceClickListener, OnCancelListener {

    // Atributos
    private List<String> elementos; // Strings que muestra el spinner
    private boolean[] seleccionados; // Array que indica strings seleccionados (true)
    private String textoPorDefectoVacio; // A mostrar si nada se selecciona
    private String textoPorDefectoVarias; // A mostrar si se selecciona mas de una

    /**
     * Constuctor de un Spinner de seleccion multiple.
     *
     * @param contexto Contexto en el que se utiliza el spinner.
     */
    public MultipleSpinner(Context contexto) {
        // Basa principalmente su comportamiento en spinner clasico
        super(contexto);
    }

    /**
     * Otros constructores necesarios para evitar errores con XML.
     */
    public MultipleSpinner(Context contexto, AttributeSet arg1) {
        super(contexto, arg1);
    }

    public MultipleSpinner(Context contexto, AttributeSet arg1, int arg2) {
        super(contexto, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialogo, int pos, boolean isChecked) {
        // Invierte el estado de seleccion de un elemento despues de ser pulsado
        seleccionados[pos] = isChecked;
    }

    @Override
    public void onCancel(DialogInterface dialogo) {
        List<String> seleccionadosStr = new ArrayList<>();
        int i = 0;

        while (i < elementos.size() && seleccionadosStr.size() < 2) {
            if (seleccionados[i]) {
                seleccionadosStr.add(elementos.get(i));
            }
            i++;
        }

        // Si hay 1 seleccionada = String seleccionado
        // Si hay > 1 seleccionadas = String indicando varias
        // Si hay 0 = String indicando que no hay seleccion
        String textoSpinner = configuraTextoSpinner(seleccionadosStr);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { textoSpinner });

        setAdapter(adapter);
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(elementos.toArray(new CharSequence[elementos.size()]),
                seleccionados, this);
        builder.setPositiveButton(android.R.string.ok,
                (dialogInterface, pos) -> dialogInterface.cancel());
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    /**
     * Establece los elementos que tendra el Spinner Multiple.
     * @param elementosArray Strings a mostrar en el Spinner.
     * @param allText String a mostrar si se seleccionan varias.
     * @param zeroText String a mostrar si no se selecciona ninguna.
     */
    public void setElementos(String[] elementosArray, String allText, String zeroText) {
        elementos = Arrays.asList(elementosArray);
        seleccionados = new boolean[elementos.size()];
        this.textoPorDefectoVarias = allText;
        this.textoPorDefectoVacio = zeroText;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[] {zeroText});
        setAdapter(adapter);
    }

    /**
     * Marca los checkbox de los strings indicados como parametro.
     * @param strings Strings cuyo checkbox se quiere marcar.
     */
    public void setSelectedStrings(List<String> strings) {
        List<String> seleccionadosStr = new ArrayList<>();

        // Determina los checkbox a marcar
        for (int i = 0; i < elementos.size(); i++) {
            for (int j = 0; j < strings.size(); j++) {
                if (elementos.get(i).equals(strings.get(j))) {
                    seleccionados[i] = true;
                    seleccionadosStr.add(elementos.get(i));
                }
            }
        }

        // Si hay 1 seleccionada = String seleccionado
        // Si hay > 1 seleccionadas = String indicando varias
        // Si hay 0 = String indicando que no hay seleccion
        String textoSpinner = configuraTextoSpinner(seleccionadosStr);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { textoSpinner });

        setAdapter(adapter);
    }

    /**
     * Obtiene los strings cuyos checkbox estan seleccionados.
     * @return lista de strings cuyos checkbox estan seleccionados.
     */
    public List<String> getSelectedStrings() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < elementos.size(); i++) {
            if (seleccionados[i]) {
                list.add(elementos.get(i));
            }
        }

        return list;
    }

    /**
     * Determina el texto que deberia mostrar el Spinner como resumen.
     * @param strings Strings seleccionados actualmente.
     * @return texto que deberia mostrar el Spinner como resumen.
     */
    private String configuraTextoSpinner(List<String> strings) {
        String textoSpinner;

        switch (strings.size()) {
            case 0:
                textoSpinner = textoPorDefectoVacio;
                break;
            case 1:
                textoSpinner = strings.get(0);
                break;
            default:
                textoSpinner = textoPorDefectoVarias;
        }
        return textoSpinner;
    }
}
