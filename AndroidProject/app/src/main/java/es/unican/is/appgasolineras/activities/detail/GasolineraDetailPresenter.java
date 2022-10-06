package es.unican.is.appgasolineras.activities.detail;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import es.unican.is.appgasolineras.model.Gasolinera;

public class GasolineraDetailPresenter implements IGasolineraDetailContract.Presenter {

    private final IGasolineraDetailContract.View view;
    private Gasolinera gasolinera;
    private String precioSumario;

    public GasolineraDetailPresenter(IGasolineraDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void init() {
        gasolinera = view.getSelectedGasolinera();
        calculateSummaryPrice();
        loadGasolineraDetails();
    }

    private void calculateSummaryPrice() {
        String precioDieselStr = gasolinera.getDieselA();
        String precioGasolinaStr = gasolinera.getNormal95();
        Double precioDiesel = 0.0;
        Double precioGasolina = 0.0;

        NumberFormat formato = NumberFormat.getInstance(Locale.FRANCE);

        try {
            Number numberDiesel = formato.parse(precioDieselStr);
            precioDiesel = numberDiesel.doubleValue();
        } catch (Exception e) {
            precioDiesel = 0.0;
        }

        try {
            Number numberGasolina = formato.parse(precioGasolinaStr);
            precioGasolina = numberGasolina.doubleValue();
        } catch (Exception e) {
            precioGasolina = 0.0;
        }

        Double sumario = (precioDiesel + precioGasolina * 2.0) / 3.0;
        precioSumario = String.format("%.3f", sumario);
    }

    private void loadGasolineraDetails() {
        view.showSummary(precioSumario);
        view.showName(checkValid(gasolinera.getRotulo()));
        view.showMunicipality(checkValid(gasolinera.getMunicipio()));
        view.showLogo(searchLogoID());
        view.showDirection(checkValid(gasolinera.getDireccion()));
        view.showCP(checkValid(gasolinera.getCp()));
        view.showPrice95(checkValid(gasolinera.getNormal95()) + " €/L");
        view.showPriceDieselA(checkValid(gasolinera.getDieselA())  + " €/L");
        view.showSchedule(checkValid(gasolinera.getHorario()));
    }

    private int searchLogoID() {
        Context context = view.getContext();
        String rotulo = gasolinera.getRotulo().toLowerCase(Locale.ROOT);

        int imageID = context.getResources().getIdentifier(rotulo, "drawable",
                context.getPackageName());

        if (imageID == 0) {
            imageID = context.getResources().getIdentifier("generic", "drawable",
                    context.getPackageName());
        }
        return imageID;
    }

    private String checkValid(String texto) {
        String correccion = texto;
        if (texto.equals("")) {
            correccion = "-";
        }
        return correccion;
    }
}
