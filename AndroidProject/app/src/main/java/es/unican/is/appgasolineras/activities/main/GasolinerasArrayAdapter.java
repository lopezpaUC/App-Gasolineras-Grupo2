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

import java.util.List;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.model.Gasolinera;

public class GasolinerasArrayAdapter extends ArrayAdapter<Gasolinera> {
    private String precioDestacar;

    public GasolinerasArrayAdapter(@NonNull Context context, @NonNull List<Gasolinera> objects) {
        super(context, 0, objects);
        precioDestacar = null;
    }

    public GasolinerasArrayAdapter(@NonNull Context context, @NonNull List<Gasolinera> objects,
                                   String precioDestacar) {
        super(context, 0, objects);
        this.precioDestacar = precioDestacar;
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
        adress(gasolinera, convertView);

        // 95 octanes price
        prizeNintyFive(gasolinera, convertView);

        // diesel A price
        prizeDiesel(gasolinera, convertView);

        return convertView;
    }

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

    private void name(Gasolinera gasolinera, View convertView){
        TextView tv = convertView.findViewById(R.id.tvName);
        tv.setText(gasolinera.getRotulo());
    }

    private void adress(Gasolinera gasolinera, View convertView){
        TextView tv = convertView.findViewById(R.id.tvAddress);
        tv.setText(gasolinera.getDireccion());
    }

    private void prizeNintyFive(Gasolinera gasolinera, View convertView){
        TextView tvLabel = convertView.findViewById(R.id.tv95Label);
        String label = getContext().getResources().getString(R.string.gasolina95label);
        tvLabel.setText(label + ":");

        TextView tv = convertView.findViewById(R.id.tv95);
        tv.setText(gasolinera.getNormal95());

        if (precioDestacar != null && precioDestacar.equals(getContext().getResources().getString(R.string.gasolina95label))) {
            tvLabel.setTypeface(tvLabel.getTypeface(), Typeface.BOLD);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        }
    }

    private void prizeDiesel(Gasolinera gasolinera, View convertView){
        TextView tvLabel = convertView.findViewById(R.id.tvDieselALabel);
        String label = getContext().getResources().getString(R.string.dieselAlabel);
        tvLabel.setText(label + ":");

        TextView tv = convertView.findViewById(R.id.tvDieselA);
        tv.setText(gasolinera.getDieselA());

        if (precioDestacar != null && precioDestacar.equals(getContext().getResources().getString(R.string.dieselAlabel))) {
            tvLabel.setTypeface(tvLabel.getTypeface(), Typeface.BOLD);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        }
    }
}
