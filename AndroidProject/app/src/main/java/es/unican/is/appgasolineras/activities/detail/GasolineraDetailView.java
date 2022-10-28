package es.unican.is.appgasolineras.activities.detail;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.CombustibleType;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;

/**
 * Vista para la actividad relacionada con la muestra de información detallada de una gasolinera.
 *
 * @author Grupo 02-CarbuRed
 * @version 1.0
 */
public class GasolineraDetailView extends AppCompatActivity
        implements IGasolineraDetailContract.View {

    public static final String INTENT_GASOLINERA = "INTENT_GASOLINERA";
    private static final int NO_ECONTRADO = 0; // Logo no encontrado

    private IGasolineraDetailContract.Presenter presenter; // Presenter de la vista detallada

    // Elementos a actualizar de la vista
    private ImageView ivRotulo;
    private TextView tvRotulo;
    private TextView tvMunicipio;
    private TextView tvDireccion;
    private TextView tvCP;
    private TextView tv95PrecioDet;
    private TextView tvDieselAPrecioDet;
    private TextView tvHorarioDet;
    private TextView tvPrecioSumarioDet;
    private TextView tvDiscounted95Price;
    private TextView tvDiscountedDieselPrice;
    private TextView tvDiscountedPrecioSumarioDet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Crea la vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasolinera_detail_view);

        // Crea el presenter
        presenter = new GasolineraDetailPresenter(this,
                getIntent().getExtras().getParcelable(INTENT_GASOLINERA));

        // Inicializa
        this.init();
        presenter.init();
    }

    @Override
    public void init() {
        // Identifica los diferentes elementos a utilizar de la vista
        ivRotulo = findViewById(R.id.ivRotulo);
        tvRotulo = findViewById(R.id.tvRotulo);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvMunicipio = findViewById(R.id.tvMunicipio);
        tvCP = findViewById(R.id.tvCP);
        tv95PrecioDet = findViewById(R.id.tv95PrecioDet);
        tvDieselAPrecioDet = findViewById(R.id.tvDieselAPrecioDet);
        tvHorarioDet = findViewById(R.id.tvHorarioDet);
        tvPrecioSumarioDet = findViewById(R.id.tvPrecioSumarioDet);
        tvDiscounted95Price = findViewById(R.id.tvDiscounted95Price);
        tvDiscountedDieselPrice = findViewById(R.id.tvDiscountedDieselPrice);
        tvDiscountedPrecioSumarioDet = findViewById(R.id.tvDiscountedPrecioSumarioDet);
    }

    @Override
    public void showInfo(Map<String, String> info) {
        // Muestra los campos de texto
        String label = info.get("label");
        tvPrecioSumarioDet.setText(info.get("summary"));
        tvRotulo.setText(label);
        tvDireccion.setText(info.get("direction"));
        tvMunicipio.setText(info.get("municipality"));
        tvCP.setText(info.get("cp"));
        tv95PrecioDet.setText(info.get("price95"));
        tvDieselAPrecioDet.setText(info.get("priceDieselA"));
        tvHorarioDet.setText(info.get("schedule"));

        // Muestra el logotipo de la gasolinera
        if (label == null) {
            label = "generic";
        }
        ivRotulo.setImageResource(loadLogoID(label));
    }

    @Override
    public void showLoadError() {
        // Define el contenido de la alerta
        String textTitle = getResources().getString(R.string.error);
        String textMessage = getResources().getString(R.string.no_detail_info);

        // Determina como crear la alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(textMessage);
        builder.setTitle(textTitle);
        builder.setIcon(android.R.drawable.ic_menu_info_details);
        builder.setPositiveButton(getResources().getString(R.string.accept),
                (dialog, id) -> presenter.onAcceptClicked());

        // Muestra la alerta
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void openMainView() {
        Intent intent = new Intent(this, MainView.class);
        startActivity(intent);
    }

    /**
     * Obtiene el identificiador del logo a cargar adecuado, en funcion del rotulo de la gasolinera
     * indicado.
     *
     * @param label Rotulo de la gasolinera.
     * @return Identificador del logo que se corresponde con la gasolinera indicada. Si no existe
     * uno especifico, se retorna el ID de un logo generico.
     */
    private int loadLogoID(String label) {
        // Intenta obtener el ID de un logo especifico de la gasolinera
        int imageID = getResources().getIdentifier(label.toLowerCase(Locale.ROOT),
                "drawable", getPackageName());

        // Si la gasolinera no tiene un logo propio
        if (imageID == NO_ECONTRADO) {
            // Obtiene el ID de un logo generico
            imageID = getResources().getIdentifier("generic", "drawable",
                    getPackageName());
        }

        return imageID;
    }

    private void applyDiscount(Gasolinera g, List<Promocion> promotions) {
        double unleaded95Discount, dieselDiscount, discountedSummaryPrice;
        Promocion appliedPromotion = null;
        Iterator<Gasolinera> iterator;

        // Iterates the promotions
        for (Promocion promotion : promotions) {
            List<Gasolinera> gasolinerasAttachedToPromotion = promotion.getListaGasolineras();

            // The promotion has to be applied to, at least, one gas station
            if (!gasolinerasAttachedToPromotion.isEmpty()) {
                // Loops all gas stations included in the promotion
                for (Gasolinera gasolinera : gasolinerasAttachedToPromotion) {
                    if (gasolinera.equals(g)) {
                        appliedPromotion = promotion;
                        break; // TODO: iterator
                    }
                }
            }
        }

        // Promotion exists
        if (appliedPromotion != null) {
            List<CombustibleType> fuels = appliedPromotion.getCombustibles();

            // The promotion must have, at least, one fuel to discount from
            if (!fuels.isEmpty()) {
                for (CombustibleType ct: fuels) {
                    if (ct.name().equals("DIESEL") || ct.name().equals("ALL_COMB")) {
                        dieselDiscount = appliedPromotion.getValor();
                    }
                    else if (ct.name().equals("GASOLINA") || ct.name().equals("ALL_COMB")) {
                        unleaded95Discount = appliedPromotion.getValor();
                    }
                }
            }
        }

        // TODO: apply discount in tv




    }
}