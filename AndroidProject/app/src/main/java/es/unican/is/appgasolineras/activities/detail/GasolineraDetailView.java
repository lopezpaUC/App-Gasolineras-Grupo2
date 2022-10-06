package es.unican.is.appgasolineras.activities.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.model.Gasolinera;

public class GasolineraDetailView extends AppCompatActivity implements IGasolineraDetailContract.View {

    public static final String INTENT_GASOLINERA = "INTENT_GASOLINERA";
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
        tvDireccion = findViewById(R.id.tvDireccion);
        tvMunicipio = findViewById(R.id.tvMunicipio);
        tvCP = findViewById(R.id.tvCP);
        tv95PrecioDet = findViewById(R.id.tv95PrecioDet);
        tvDieselAPrecioDet = findViewById(R.id.tvDieselAPrecioDet);
        tvHorarioDet = findViewById(R.id.tvHorarioDet);
        tvPrecioSumarioDet = findViewById(R.id.tvPrecioSumarioDet);
    }

    @Override
    public Gasolinera getSelectedGasolinera() {
        return getIntent().getExtras().getParcelable(INTENT_GASOLINERA);
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
    public void showDirection(String direccion) {
        tvDireccion.setText(direccion);
    }

    @Override
    public void showMunicipality(String municipio) {
        tvMunicipio.setText(municipio);
    }

    @Override
    public void showCP(String cp) {
        tvCP.setText(cp);
    }

    @Override
    public void showPrice95(String precio) {
        tv95PrecioDet.setText(precio);}

    @Override
    public void showPriceDieselA(String precio) {
        tvDieselAPrecioDet.setText(precio);
    }

    @Override
    public void showSchedule(String horario) {
        tvHorarioDet.setText(horario);
    }

    @Override
    public void showSummary(String sumario) {
        tvPrecioSumarioDet.setText(sumario);
    }
}