package es.unican.is.appgasolineras.activities.promotion;

import android.content.Context;
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
import es.unican.is.appgasolineras.activities.main.CombustibleType;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.PromocionRepository;

public class PromocionesArrayAdapter extends ArrayAdapter<Promocion> {

    private List<String> listaGasolineras;

    public PromocionesArrayAdapter(@NonNull Context context, @NonNull List<Promocion> objects, @NonNull List<String> lista) {
        super(context, 0, objects);
        this.listaGasolineras = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Promocion promocion = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_promotions_list_item, parent, false);
        }

        // logo
        logo(promocion, convertView);

        // namePromocion
        name(promocion, convertView);

        // nameGasolinera
        gasolinera(promocion, convertView, listaGasolineras.get(position));

        // descuento
        descuento(promocion, convertView);

        // combustible
        combustible(promocion, convertView);

        return convertView;
    }

    private void logo(Promocion promocion, View convertView){
        ImageView iv = convertView.findViewById(R.id.ivLogoPromocion);
    }

    private void name(Promocion promocion, View convertView){
        TextView tv = convertView.findViewById(R.id.tvNamePromocion);
        tv.setText(promocion.getId());
    }

    private void gasolinera(Promocion promocion, View convertView, String gasolinera){
       TextView tv = convertView.findViewById(R.id.tvNameGasolinera);
       tv.setText(gasolinera);
    }

    private void descuento(Promocion promocion, View convertView){
        TextView tv = convertView.findViewById(R.id.tvDescuento);
        if (promocion.getDescuentoEurosLitro() > 0)
            tv.setText(Double.toString(promocion.getDescuentoEurosLitro()));
        else
            tv.setText(Double.toString(promocion.getDescuentoPorcentual()));
    }

    private void combustible(Promocion promocion, View convertView){
        TextView tv = convertView.findViewById(R.id.tvCombustible);
        tv.setText(promocion.getCombustible().toString());
    }
}
