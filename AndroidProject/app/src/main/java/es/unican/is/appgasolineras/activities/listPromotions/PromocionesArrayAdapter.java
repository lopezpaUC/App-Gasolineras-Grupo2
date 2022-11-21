package es.unican.is.appgasolineras.activities.listPromotions;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.model.Promocion;

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
        Promocion promocion = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_promotions_list_item, parent, false);
        }

        // logo
        logo(convertView, listaImagen.get(position));

        // nombrePromocion
        name(promocion, convertView);

        // nombreGasolinera
        gasolinera(convertView, listaGasolineras.get(position));

        // descuento
        descuento(promocion, convertView);

        // combustible
        combustible(promocion, convertView);

        //bin
        bin(promocion,convertView);

        return convertView;
    }


    /**
     * Papelera para eliminar
     * @param convertView Vista
     * @param promocion Promocion
     */
    private void bin(Promocion promocion, View convertView){
        ImageView bin = convertView.findViewById(R.id.ivBin);
        bin.setTag(promocion.getId());
    }

    /**
     * Logo de la gasolinera que tiene aplicada la promocion
     * @param convertView Vista
     * @param rotulo Rotulo
     */
    private void logo(View convertView, String rotulo){
        ImageView iv = convertView.findViewById(R.id.ivLogoPromocion);
        
        int imageID = getContext().getResources()
                .getIdentifier(rotulo, "drawable", getContext().getPackageName());

        if (imageID == 0 || TextUtils.isDigitsOnly(rotulo)) {
            imageID = getContext().getResources()
                    .getIdentifier("generic", "drawable", getContext().getPackageName());
        }

        if (imageID != 0) {
            iv.setImageResource(imageID);
        }
    }

    /**
     * Nombre de la promocion
     * @param promocion Promocion
     * @param convertView Vista
     */
    private void name(Promocion promocion, View convertView){
        TextView tv = convertView.findViewById(R.id.tvNamePromocion);
        tv.setText(promocion.getId());
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
    }


    /**
     * Nombre de la gasolinera que tiene la promocion aplicada
     * @param convertView Vista
     * @param gasolinera Gasolinera
     */
    private void gasolinera(View convertView, String gasolinera){

        TextView tv = convertView.findViewById(R.id.tvGasolinerasAsociadas);
        tv.setText("Gasolineras:");
        tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
        tv = convertView.findViewById(R.id.tvNameGasolinera);
        StringBuffer strbf = new StringBuffer();
        Matcher match = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(gasolinera);
        while(match.find())
        {
            match.appendReplacement(strbf, match.group(1).toUpperCase() + match.group(2).toLowerCase());
        }
        tv.setText(match.appendTail(strbf).toString());

    }

    /**
     * Descuento que aplica la promoción
     * @param promocion Promocion
     * @param convertView Vista
     */
    private void descuento(Promocion promocion, View convertView){
        TextView tv = convertView.findViewById(R.id.tvDescuento);
        if (promocion.getDescuentoEurosLitro() > 0)
            tv.setText(Double.toString(promocion.getDescuentoEurosLitro()) + "€/L");
        else
            tv.setText(Double.toString(promocion.getDescuentoPorcentual()) + "%");

        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
    }

    /**
     * Combustible al cual aplica la promocion
     * @param promocion Promocion
     * @param convertView Vista
     */
    private void combustible(Promocion promocion, View convertView){

        TextView tv = convertView.findViewById(R.id.tvCombustiblesAsociados);
        tv.setText("Combustibles:");
        tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
        tv = convertView.findViewById(R.id.tvCombustible);

        if(promocion.getCombustibles().contains("-")){
            tv.setText("Varios");
        } else {
            tv.setText(promocion.getCombustibles());
        }
    }
}
