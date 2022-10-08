package es.unican.is.appgasolineras.activities.detail;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.unican.is.appgasolineras.model.Gasolinera;

public class GasolineraDetailPresenter implements IGasolineraDetailContract.Presenter {

    private final IGasolineraDetailContract.View view;
    private Gasolinera gasolinera;
    private String precioSumarioStr;

    public GasolineraDetailPresenter(IGasolineraDetailContract.View view, Gasolinera gasolinera) {
        this.view = view;
        this.gasolinera = gasolinera;
    }

    @Override
    public void init() {
        double precioSumario = calculateSummaryPrice();

        if (precioSumario == 0.0) {
            precioSumarioStr = "-";
        }
        precioSumarioStr = String.format("%.3f", precioSumario);

        loadGasolineraDetails();
    }

    private double calculateSummaryPrice() {
        String precioDieselStr = gasolinera.getDieselA();
        String precioGasolinaStr = gasolinera.getNormal95();
        Double precioDiesel = 0.0;
        Double precioGasolina = 0.0;
        Double sumario;

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

        if (precioDiesel == 0.0) {
            sumario = precioGasolina;
        } else if(precioGasolina == 0.0) {
            sumario = precioDiesel;
        } else {
            sumario = (precioDiesel + precioGasolina * 2.0) / 3.0;
        }

        return sumario;
    }

    private void loadGasolineraDetails() {
        Map<String, String> info = new HashMap<>();
        info.put("summary", precioSumarioStr + " €/L");
        info.put("label", checkValid(gasolinera.getRotulo()));
        info.put("municipality", checkValid(gasolinera.getMunicipio()));
        info.put("direction", checkValid(gasolinera.getDireccion()));
        info.put("cp", checkValid(gasolinera.getCp()));
        info.put("price95", checkValid(gasolinera.getNormal95()) + " €/L");
        info.put("priceDieselA", checkValid(gasolinera.getDieselA()) + " €/L");
        info.put("schedule", checkValid(gasolinera.getHorario()));

        view.showInfo(info);
    }

    private String checkValid(String texto) {
        String correccion = texto;
        if (texto.equals("")) {
            correccion = "-";
        }
        return correccion;
    }

    public String getPrecioSumario() {
        return precioSumarioStr;
    }
}
