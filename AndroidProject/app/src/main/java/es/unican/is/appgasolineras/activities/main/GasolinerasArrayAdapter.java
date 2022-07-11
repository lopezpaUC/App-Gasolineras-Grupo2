package es.unican.is.appgasolineras.activities.main;

import android.content.Context;
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

    public GasolinerasArrayAdapter(@NonNull Context context, @NonNull List<Gasolinera> objects) {
        super(context, 0, objects);
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
        {
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

        // name
        {
            TextView tv = convertView.findViewById(R.id.tvName);
            tv.setText(gasolinera.getRotulo());
        }

        // address
        {
            TextView tv = convertView.findViewById(R.id.tvAddress);
            tv.setText(gasolinera.getDireccion());
        }

        // 95 octanes price
        {
            TextView tvLabel = convertView.findViewById(R.id.tv95Label);
            String label = getContext().getResources().getString(R.string.gasolina95label);
            tvLabel.setText(label + ":");

            TextView tv = convertView.findViewById(R.id.tv95);
            tv.setText(gasolinera.getNormal95());
        }

        // diesel A price
        {
            TextView tvLabel = convertView.findViewById(R.id.tvDieselALabel);
            String label = getContext().getResources().getString(R.string.dieselAlabel);
            tvLabel.setText(label + ":");

            TextView tv = convertView.findViewById(R.id.tvDieselA);
            tv.setText(gasolinera.getDieselA());
        }

        return convertView;
    }
}
