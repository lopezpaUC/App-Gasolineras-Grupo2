package es.unican.is.appgasolineras.activities.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.Map;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.model.Gasolinera;

public class GasolineraDetailView extends AppCompatActivity implements IGasolineraDetailContract.View {

    public static final String INTENT_GASOLINERA = "INTENT_GASOLINERA";
    private static final int NO_ECONTRADO = 0;

    private IGasolineraDetailContract.Presenter presenter;

    private ImageView ivRotulo;
    private TextView tvRotulo;
    private TextView tvMunicipio;
    private TextView tvDireccion;
    private TextView tvCP;
    private TextView tv95PrecioDet;
    private TextView tvDieselAPrecioDet;
    private TextView tvHorarioDet;
    private TextView tvPrecioSumarioDet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasolinera_detail_view);

        presenter = new GasolineraDetailPresenter(this,
                getIntent().getExtras().getParcelable(INTENT_GASOLINERA));
        this.init();
        presenter.init();
    }

    @Override
    public void init() {
        ivRotulo = findViewById(R.id.ivRotulo);
        tvRotulo = findViewById(R.id.tvRotulo);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvMunicipio = findViewById(R.id.tvMunicipio);
        tvCP = findViewById(R.id.tvCP);
        tv95PrecioDet = findViewById(R.id.tv95PrecioDet);
        tvDieselAPrecioDet = findViewById(R.id.tvDieselAPrecioDet);
        tvHorarioDet = findViewById(R.id.tvHorarioDet);
        tvPrecioSumarioDet = findViewById(R.id.tvPrecioSumarioDet);
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
        ivRotulo.setImageResource(loadLogoID(label));
    }

    private int loadLogoID(String label) {
        int imageID = getResources().getIdentifier(label.toLowerCase(Locale.ROOT),
                "drawable", getPackageName());

        if (imageID == NO_ECONTRADO) {
            imageID = getResources().getIdentifier("generic", "drawable",
                    getPackageName());
        }
        return imageID;
    }
}