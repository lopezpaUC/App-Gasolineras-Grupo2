package es.unican.is.appgasolineras.activities.promotion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.Instant;
import java.util.List;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.model.Promocion;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;
import es.unican.is.appgasolineras.repository.PromocionesRepository;

public class ListaPromocionesView extends AppCompatActivity implements IListaPromocionesContract.View {

    private IListaPromocionesContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Crea la vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions_list);

        // Crea el presenter
        presenter = new ListaPromocionesPresenter(this);
        presenter.init();

        // Inicializa
        this.init();
        presenter.init();
    }

    @Override
    public void init() {

    }

    @Override
    public IPromocionesRepository getPromocionRepository() {
        return new PromocionesRepository(this);
    }

    @Override
    public IGasolinerasRepository getGasolineraRepository() {
        return new GasolinerasRepository(this);
    }

    @Override
    public void showLoadCorrect(int promocionesCount) {
        String text = getResources().getString(R.string.loadCorrectPromociones);
        Instant lastDownloaded = Prefs.from(this).getInstant("KEY_LAST_SAVED");
        Toast.makeText(this, String.format(text, promocionesCount), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadError() {
        String text = getResources().getString(R.string.loadErrorPromociones);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPromociones(List<Promocion> promociones, List<String> lista, List<String> rotulos) {
        PromocionesArrayAdapter adapter = new PromocionesArrayAdapter(this, promociones, lista, rotulos);
        ListView list = findViewById(R.id.lvPromociones);
        list.setAdapter(adapter);
    }

    
    public void deletePromocionSeleccionada(View v) {
        ImageView bin = (ImageView) findViewById(R.id.ivBin);
        bin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(ListaPromocionesView.this).create(); //Read Update
                alertDialog.setTitle("Confirmación");
                alertDialog.setMessage("¿Desea eliminar esta promoción?");

                alertDialog.setButton2("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        TextView campo = findViewById(R.id.tvNamePromocion);
                        String nombre = campo.getText().toString();
                        presenter.deletePromocion(nombre);

                        alertDialog.dismiss();
                    }
                });

                alertDialog.setButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }

        });
    }


    
}
