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

public class PromocionesArrayAdapter extends ArrayAdapter<Promocion> {

    public PromocionesArrayAdapter(@NonNull Context context, @NonNull List<Promocion> objects) {
        super(context, 0, objects);
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

        // name
        name(promocion, convertView);

        // address
        gasolinera(promocion, convertView);

        // 95 octanes price
        descuento(promocion, convertView);

        // diesel A price
        combustible(promocion, convertView);

        return convertView;
    }

    private void logo(Promocion promocion, View convertView){

    }

    private void name(Promocion promocion, View convertView){
        TextView tv = convertView.findViewById(R.id.tvNamePromocion);
        tv.setText(promocion.getId());
    }

    private void gasolinera(Promocion promocion, View convertView){
        /**TextView tv = convertView.findViewById(R.id.tvNameGasolinera);
        if(promocion.getListaGasolineras().size()==1){
            tv.setText(promocion.getListaGasolineras().get(0).getRotulo());
        }
        else {
            tv.setText("Todas");
        }*/
    }

    private void descuento(Promocion promocion, View convertView){
        TextView tv = convertView.findViewById(R.id.tvDescuento);
        //tv.setText(Double.toString(promocion.getValor()));
    }

    private void combustible(Promocion promocion, View convertView){
        TextView tv = convertView.findViewById(R.id.tvCombustible);
        /**if (promocion.getCombustibles().size()==1){
            tv.setText(promocion.getCombustibles().get(0).name());
        } else if (promocion.getCombustibles().size() < CombustibleType.values().length) {
            tv.setText("Varios");
        } else {
            tv.setText("Todos");
        }*/
    }
}
