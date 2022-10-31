package es.unican.is.appgasolineras.activities.promotion;

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
import java.util.Locale;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.CombustibleType;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

public class PromocionesArrayAdapter extends ArrayAdapter<Promocion> {

    private List<String> listaGasolineras;

    private List<String> listaImagen;

    public PromocionesArrayAdapter(@NonNull Context context, @NonNull List<Promocion> objects, @NonNull List<String> lista, @NonNull List<String> imagenes) {
        super(context, 0, objects);
        this.listaGasolineras = lista;
        this.listaImagen = imagenes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        System.out.println("Prueba");

        Promocion promocion = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_promotions_list_item, parent, false);
        }

        // logo
        logo(promocion, convertView, listaImagen.get(position));

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

    private void logo(Promocion promocion, View convertView, String rotulo){
        ImageView iv = convertView.findViewById(R.id.ivLogoPromocion);
        
        int imageID = imageID = getContext().getResources()
                .getIdentifier(rotulo, "drawable", getContext().getPackageName());

        // Si el rotulo son sólo numeros, el método getIdentifier simplemente devuelve
        // como imageID esos números, pero eso va a fallar porque no tendré ningún recurso
        // que coincida con esos números
        if (imageID == 0 || TextUtils.isDigitsOnly(rotulo)) {
            imageID = getContext().getResources()
                    .getIdentifier("generic", "drawable", getContext().getPackageName());
        }

        if (imageID != 0) {
            iv.setImageResource(imageID);
        }
    }

    private void name(Promocion promocion, View convertView){
        TextView tv = convertView.findViewById(R.id.tvNamePromocion);
        tv.setText(promocion.getId().toUpperCase());
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
    }

    private void gasolinera(Promocion promocion, View convertView, String gasolinera){
       TextView tv = convertView.findViewById(R.id.tvNameGasolinera);
       tv.setText(gasolinera);
    }

    private void descuento(Promocion promocion, View convertView){
        TextView tv = convertView.findViewById(R.id.tvDescuento);
        if (promocion.getDescuentoEurosLitro() > 0)
            tv.setText(Double.toString(promocion.getDescuentoEurosLitro()) + "€/L");
        else
            tv.setText(Double.toString(promocion.getDescuentoPorcentual()) + "%");
    }

    private void combustible(Promocion promocion, View convertView){
        TextView tv = convertView.findViewById(R.id.tvCombustible);
        if(promocion.getCombustibles().contains("-")){
            tv.setText("Varios");
        } else {
            tv.setText(promocion.getCombustibles());
        }

    }
}
