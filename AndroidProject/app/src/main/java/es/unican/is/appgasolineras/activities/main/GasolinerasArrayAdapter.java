package es.unican.is.appgasolineras.activities.main;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
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
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;
import es.unican.is.appgasolineras.repository.PromocionesRepository;

public class GasolinerasArrayAdapter extends ArrayAdapter<Gasolinera> {
    private String precioDestacar;
    private boolean summaryPrice;

    private IGasolinerasRepository gasolinerasRepository = new GasolinerasRepository(getContext());
    private IPromocionesRepository promocionesRepository = new PromocionesRepository(getContext());
    private boolean order;

    public GasolinerasArrayAdapter(@NonNull Context context, @NonNull List<Gasolinera> objects) {
        super(context, 0, objects);
        precioDestacar = null;
        this.order = false;
    }

    public GasolinerasArrayAdapter(@NonNull Context context, @NonNull List<Gasolinera> objects,
                                   String precioDestacar, boolean order) {
        super(context, 0, objects);
        this.precioDestacar = precioDestacar;

        summaryPrice = false;
        this.order = order;
    }

    public GasolinerasArrayAdapter(@NonNull Context context, @NonNull List<Gasolinera> objects,
                                   boolean summaryPrice, boolean order) {
        super(context, 0, objects);
        this.summaryPrice = summaryPrice;
        this.order = order;
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

    private void address(Gasolinera gasolinera, View convertView){
        TextView tv = convertView.findViewById(R.id.tvAddress);
        tv.setText(gasolinera.getDireccion());
    }

    private void priceNinetyFive(Gasolinera gasolinera, View convertView){
        TextView tvLabel = convertView.findViewById(R.id.tv95Label);
        String label = getContext().getResources().getString(R.string.gasolina95label);
        tvLabel.setText(label + ":");

        TextView tv = convertView.findViewById(R.id.tv95);
        Double precioDouble = -1.0;
        String precioString = "-";

        double unleaded95 = gasolinerasRepository.precioToDouble(gasolinera.getNormal95(), NumberFormat.getInstance(Locale.FRANCE));
        double discountedUnleaded95 = unleaded95;
        List<Promocion> promociones1 = promocionesRepository.getPromocionesRelacionadasConGasolinera(gasolinera.getId());

        // If it is ordered, it shows the price with promotions
        if (order) {
            if (!promociones1.isEmpty()) {
                Promocion bestPromotion = gasolinerasRepository.bestPromotion(unleaded95, promociones1, "Gasolina");
                discountedUnleaded95 = gasolinerasRepository.calculateDiscountedPrice(unleaded95, bestPromotion);
            }
        } else { // Normal view or filter
            precioString = gasolinera.getNormal95();
        }
        precioDouble = discountedUnleaded95;

        // Comprueba si el precio no es valido
        if (precioDouble < 0.0) {
            tv.setText(precioString);
        } else {
            precioString = String.valueOf(precioDouble);
        }

        tv.setText(precioString);

        // Keeps filter persistency
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

    private void priceDiesel(Gasolinera gasolinera, View convertView){
        TextView tvLabel = convertView.findViewById(R.id.tvDieselALabel);
        String label = getContext().getResources().getString(R.string.dieselAlabel);
        tvLabel.setText(label + ":");

        TextView tv = convertView.findViewById(R.id.tvDieselA);
        Double precioDouble = -1.0;
        String precioString = "-";

        double diesel = gasolinerasRepository.precioToDouble(gasolinera.getDieselA(), NumberFormat.getInstance(Locale.FRANCE));
        double discountedDiesel = diesel;
        List<Promocion> promotions = promocionesRepository.getPromocionesRelacionadasConGasolinera(gasolinera.getId());

        // If it is ordered, it shows the price with promotions
        if (order) {
            if (!promotions.isEmpty()) {
                Promocion bestPromotion = gasolinerasRepository.bestPromotion(diesel, promotions, "Diésel");
                discountedDiesel = gasolinerasRepository.calculateDiscountedPrice(diesel, bestPromotion);
            }
        } else { // Normal view or filter
            precioString = gasolinera.getDieselA();
        }
       precioDouble = discountedDiesel;

        // Comprueba si el precio no es valido
        if (precioDouble < 0.0) {
            tv.setText(precioString);
        } else {
            precioString = String.valueOf(precioDouble);
        }

        tv.setText(precioString);

        // Keeps filter persistency
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

    private void summaryPrice(Gasolinera gasolinera, View convertView) {
        TextView tvLabel = convertView.findViewById(R.id.tvSummaryLabel);
        String label = getContext().getResources().getString(R.string.sumPrice);
        tvLabel.setText(label + ":");

        TextView tv = convertView.findViewById(R.id.tvSummary);
        Double precioDouble = -1.0;
        String precioString = "-";

        // Converts price to double
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());

        try {
            Number number = format.parse(getDiscountedSummaryPrice(gasolinera));
            precioDouble = number.doubleValue();
        } catch (Exception e) {
            precioString = "-";
        }

        // Checks if the price is valid
        if (precioDouble < 0.0) {
            tv.setText(precioString);
        } else {
            precioString = getDiscountedSummaryPrice(gasolinera);
        }

        tv.setText(precioString);

        if (precioDestacar != null && precioDestacar.equals(getContext().getResources().getString(R.string.gasolina95label))) {

            TextView tv95Label = convertView.findViewById(R.id.tv95Label);
            TextView tv95 = convertView.findViewById(R.id.tv95);
            tv95Label.setTypeface(tv95Label.getTypeface(), Typeface.BOLD);
            tv95.setTypeface(tv95.getTypeface(), Typeface.BOLD);
        } else if (precioDestacar != null && precioDestacar.equals(getContext().getResources().getString(R.string.dieselAlabel))) {
            TextView tvDieselLabel = convertView.findViewById(R.id.tvDieselALabel);
            TextView tvDiesel = convertView.findViewById(R.id.tvDieselA);
            tvDieselLabel.setTypeface(tvDieselLabel.getTypeface(), Typeface.BOLD);
            tvDiesel.setTypeface(tvDiesel.getTypeface(), Typeface.BOLD);
        }

        // Shows summary price
        tvLabel.setVisibility(View.VISIBLE);
        tv.setVisibility(View.VISIBLE);
    }

    public String getDiscountedSummaryPrice(Gasolinera gasStation) {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        IPromocionesRepository promocionesRepository = new PromocionesRepository(getContext());
        List<Promocion> promotions = promocionesRepository.
                getPromocionesRelacionadasConGasolinera(gasStation.getId());

        // Base prices (text)
        String dieselStr = gasStation.getDieselA();
        String unleaded95Str = gasStation.getNormal95();

        // Base prices (numeric format)
        double diesel = gasolinerasRepository.precioToDouble(dieselStr, format);
        double unleaded95 = gasolinerasRepository.precioToDouble(unleaded95Str, format);

        // Promotions
        Promocion promotionDiesel = gasolinerasRepository.bestPromotion(diesel, promotions, "Diésel");
        Promocion promotion95 = gasolinerasRepository.bestPromotion(unleaded95, promotions, "Gasolina");

        // Prices with applied promotions
        double discountedDiesel = gasolinerasRepository.calculateDiscountedPrice(diesel, promotionDiesel);
        double discounted95 = gasolinerasRepository.calculateDiscountedPrice(unleaded95, promotion95);
        double summaryPrice = gasolinerasRepository.calculateSummary(discountedDiesel, discounted95);

        return gasolinerasRepository.precioSumarioToStr(summaryPrice);
    }


}
