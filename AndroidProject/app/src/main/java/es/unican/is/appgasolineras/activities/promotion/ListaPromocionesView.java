package es.unican.is.appgasolineras.activities.promotion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.View;

import android.widget.ImageView;
import android.widget.ListView;

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

        // Inicializa
        presenter.init();
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


    @Override
    public void deletePromotionSelected(View v) {


        ImageView bin = (ImageView) v.findViewById(R.id.ivBin);

        bin.setOnClickListener((View view) -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton(R.string.accept, ((DialogInterface dialogInterface, int i) -> {
                    String nombre = (String) bin.getTag();

                    presenter.deletePromotion(nombre);

                    dialogInterface.cancel();

                    if(presenter.listaPromocionesVacia()){
                        setContentView(R.layout.activity_promotions_list);
                    }else{
                        presenter.init();
                    }
                }));

                builder.setNegativeButton(R.string.cancel, ((DialogInterface dialogInterface, int i) ->
                            dialogInterface.cancel()
                        ));

                builder.setTitle("Confirmación");
                builder.setMessage("¿Desea eliminar esta promoción?");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        });
    }


    
}
