package es.unican.is.appgasolineras.activities.main;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.common.utils.PriceUtilities;
import es.unican.is.appgasolineras.model.Gasolinera;

/**
 * Array Adapter para el listado de gasolineras principal de la aplicacion.
 *
 * @author Grupo 02-CarbuRed
 * @version 1.0
 */
public class GasolinerasArrayAdapter extends ArrayAdapter<Gasolinera> {
    private final PriceUtilities utilities;
    private final String precioDestacar;

    private boolean summaryPrice;

    /**
     * Construye un adapter basico por defecto.
     *
     * @param context Contexto.
     * @param objects Lista de gasolineras.
     */
    public GasolinerasArrayAdapter(@NonNull Context context, @NonNull List<Gasolinera> objects) {
        super(context, 0, objects);
        precioDestacar = null;

        utilities = new PriceUtilities();
    }

    /**
     * Construye un adapter con un precio destacado.
     *
     * @param context Contexto.
     * @param objects Lista de gasolineras.
     * @param precioDestacar Precio a destacar.
     */
    public GasolinerasArrayAdapter(@NonNull Context context, @NonNull List<Gasolinera> objects,
                                   String precioDestacar) {
        super(context, 0, objects);
        this.precioDestacar = precioDestacar;

        summaryPrice = false;

        utilities = new PriceUtilities();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Gasolinera gasolinera = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_main_item, parent, false);
        }

        // logo
        logo(gasolinera, convertView);

        // name
        name(gasolinera, convertView);

        // address
        address(gasolinera, convertView);

        // 95 octanes price
        priceNinetyFive(gasolinera, convertView);

        // diesel A price
        priceDiesel(gasolinera, convertView);

        if (summaryPrice) {
            // Summary price
            summaryPrice(gasolinera, convertView);
        }

        return convertView;
    }

    /**
     * Establece el logo.
     *
     * @param gasolinera Gasolinera.
     * @param convertView View.
     */
    private void logo(Gasolinera gasolinera, View convertView) {
        String rotulo = gasolinera.getRotulo().toLowerCase();

        int imageID = getContext().getResources()
                .getIdentifier(rotulo, "drawable", getContext().getPackageName());

        // Si el rotulo son sólo numeros, el método getIdentifier simplemente devuelve
        // como imageID esos números, pero eso va a fallar porque no tendré ningún recurso
        // que coincida con esos números
        if (imageID == 0 || TextUtils.isDigitsOnly(rotulo)) {
            imageID = getContext().getResources()
                    .getIdentifier("generic", "drawable", getContext().getPackageName());
        }

        if (imageID != 0) {
            ImageView view = convertView.findViewById(R.id.ivLogo);
            view.setImageResource(imageID);
        }
    }

    /**
     * Establece el nombre/rotulo.
     *
     * @param gasolinera Gasolinera.
     * @param convertView View.
     */
    private void name(Gasolinera gasolinera, View convertView){
        TextView tv = convertView.findViewById(R.id.tvName);
        tv.setText(gasolinera.getRotulo());
    }

    /**
     * Establece la direccion.
     *
     * @param gasolinera Gasolinera.
     * @param convertView View.
     */
    private void address(Gasolinera gasolinera, View convertView){
        TextView tv = convertView.findViewById(R.id.tvAddress);
        tv.setText(gasolinera.getDireccion());
    }

    /**
     * Establece precio Gasolina 95.
     *
     * @param gasolinera Gasolinera.
     * @param convertView View.
     */
    private void priceNinetyFive(Gasolinera gasolinera, View convertView){
        TextView tvLabel = convertView.findViewById(R.id.tv95Label);
        String label = getContext().getResources().getString(R.string.gasolina95label);
        tvLabel.setText(label + ":");

        TextView tv = convertView.findViewById(R.id.tv95);
        Double precioDouble = -1.0;
        String precioString = "-";

        // Convierte el precio a formato Double
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());

        try {
            Number number = format.parse(gasolinera.getNormal95());
            precioDouble = number.doubleValue();
        } catch (Exception e) {
            precioString = "-";
        }

        // Comprueba si el precio no es valido
        if (precioDouble < 0.0) {
            tv.setText(precioString);
        } else {
            precioString = gasolinera.getNormal95();
        }

        tv.setText(precioString);

        if (precioDestacar != null && precioDestacar.equals(getContext().getResources().getString(R.string.gasolina95label))) {
            tvLabel.setTypeface(tvLabel.getTypeface(), Typeface.BOLD);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        }

        // Hides summary price
        TextView summaryLabelTv = convertView.findViewById(R.id.tvSummaryLabel);
        TextView summaryTv = convertView.findViewById(R.id.tvSummary);

        summaryLabelTv.setVisibility(View.INVISIBLE);
        summaryTv.setVisibility(View.INVISIBLE);
    }

    /**
     * Establece precio Diesel.
     *
     * @param gasolinera Gasolinera.
     * @param convertView View.
     */
    private void priceDiesel(Gasolinera gasolinera, View convertView){
        TextView tvLabel = convertView.findViewById(R.id.tvDieselALabel);
        String label = getContext().getResources().getString(R.string.dieselAlabel);
        tvLabel.setText(label + ":");

        TextView tv = convertView.findViewById(R.id.tvDieselA);
        Double precioDouble = -1.0;
        String precioString = "-";

        // Convierte el precio a formato Double
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());

        try {
            Number number = format.parse(gasolinera.getDieselA());
            precioDouble = number.doubleValue();
        } catch (Exception e) {
            precioString = "-";
        }

        // Comprueba si el precio no es valido
        if (precioDouble < 0.0) {
            tv.setText(precioString);
        } else {
            precioString = gasolinera.getDieselA();
        }

        tv.setText(precioString);

        if (precioDestacar != null && precioDestacar.equals(getContext().getResources().getString(R.string.dieselAlabel))) {
            tvLabel.setTypeface(tvLabel.getTypeface(), Typeface.BOLD);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        }

        // Hides summary price
        TextView summaryLabelTv = convertView.findViewById(R.id.tvSummaryLabel);
        TextView summaryTv = convertView.findViewById(R.id.tvSummary);

        summaryLabelTv.setVisibility(View.INVISIBLE);
        summaryTv.setVisibility(View.INVISIBLE);
    }

    /**
     * Establece precio Sumario.
     *
     * @param gasolinera Gasolinera.
     * @param convertView View.
     */
    private void summaryPrice(Gasolinera gasolinera, View convertView) {
        TextView tvLabel = convertView.findViewById(R.id.tvSummaryLabel);
        String label = getContext().getResources().getString(R.string.sumPrice);
        tvLabel.setText(label + ":");

        TextView tv = convertView.findViewById(R.id.tvSummary);
        Double precioDouble = -1.0;
        String precioString = "-";

        String summaryStr = "-";

        // Converts price to double
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());

        try {
            double diesel = format.parse(gasolinera.getDieselA()).doubleValue();
            double unleaded95 = format.parse(gasolinera.getNormal95()).doubleValue();
            double summary = utilities.calculateSummary(diesel, unleaded95);
            summaryStr = utilities.precioSumarioToStr(summary);
            Number number = format.parse(summaryStr);
            precioDouble = number.doubleValue();
        } catch (Exception e) {
            precioString = "-";
        }

        // Checks if the price is valid
        if (precioDouble < 0.0) {
            tv.setText(precioString);
        } else {
            precioString = summaryStr;
        }

        tv.setText(precioString);

        if (precioDestacar != null && precioDestacar.equals(getContext().getResources().
                getString(R.string.gasolina95label))) {

            TextView tv95Label = convertView.findViewById(R.id.tv95Label);
            TextView tv95 = convertView.findViewById(R.id.tv95);
            tv95Label.setTypeface(tv95Label.getTypeface(), Typeface.BOLD);
            tv95.setTypeface(tv95.getTypeface(), Typeface.BOLD);
        } else if (precioDestacar != null && precioDestacar.equals(getContext().getResources().
                getString(R.string.dieselAlabel))) {
            TextView tvDieselLabel = convertView.findViewById(R.id.tvDieselALabel);
            TextView tvDiesel = convertView.findViewById(R.id.tvDieselA);
            tvDieselLabel.setTypeface(tvDieselLabel.getTypeface(), Typeface.BOLD);
            tvDiesel.setTypeface(tvDiesel.getTypeface(), Typeface.BOLD);
        }

        // Shows summary price
        tvLabel.setVisibility(View.VISIBLE);
        tv.setVisibility(View.VISIBLE);
    }
}
