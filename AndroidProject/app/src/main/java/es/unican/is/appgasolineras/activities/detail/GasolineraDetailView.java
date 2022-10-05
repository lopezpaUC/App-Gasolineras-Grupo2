package es.unican.is.appgasolineras.activities.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.IMainContract;
import es.unican.is.appgasolineras.activities.main.MainPresenter;
import es.unican.is.appgasolineras.model.Gasolinera;

public class GasolineraDetailView extends AppCompatActivity implements IGasolineraDetailContract.View {

    public static final String INTENT_GASOLINERA = "INTENT_GASOLINERA";
    private IGasolineraDetailContract.Presenter presenter;

    private ImageView ivRotulo;
    private TextView tvRotulo;
    private TextView tvMunicipio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasolinera_detail_view);

        presenter = new GasolineraDetailPresenter(this);
        this.init();
        presenter.init();
        /** Link to view elements
        ImageView ivRotulo = findViewById(R.id.ivRotulo);
        TextView tvRotulo = findViewById(R.id.tvRotulo);
        TextView tvMunicipio = findViewById(R.id.tvMunicipio);

        // Get Gas Station from the intent that triggered this activity
        Gasolinera gasolinera = getIntent().getExtras().getParcelable(INTENT_GASOLINERA);

        // Set logo
        int imageID = getResources().getIdentifier("generic", "drawable", getPackageName());
        ivRotulo.setImageResource(imageID);

        // Set Texts
        tvRotulo.setText(gasolinera.getRotulo());
        tvMunicipio.setText(gasolinera.getMunicipio());*/
    }

    @Override
    public void init() {
        ivRotulo = findViewById(R.id.ivRotulo);
        tvRotulo = findViewById(R.id.tvRotulo);
        tvMunicipio = findViewById(R.id.tvMunicipio);
    }

    @Override
    public Gasolinera getSelectedGasolinera() {
        Gasolinera gasolinera = getIntent().getExtras().getParcelable(INTENT_GASOLINERA);
        return gasolinera;
    }

    @Override
    public Context getContext(){
        return this;
    }

    @Override
    public void showLogo(int imageID) {
        ivRotulo.setImageResource(imageID);
    }

    @Override
    public void showName(String rotulo) {
        tvRotulo.setText(rotulo);
    }

    @Override
    public void showMunicipality(String municipio) {
        tvMunicipio.setText(municipio);
    }
}