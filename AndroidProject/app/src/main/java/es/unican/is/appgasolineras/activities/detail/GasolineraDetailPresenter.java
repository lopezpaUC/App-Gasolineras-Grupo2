package es.unican.is.appgasolineras.activities.detail;

import android.widget.Toast;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.unican.is.appgasolineras.model.Gasolinera;

public class GasolineraDetailPresenter implements IGasolineraDetailContract.Presenter {

    private static final double ERROR_CONVERSION = -1.0;
    private final IGasolineraDetailContract.View view;
    private final Gasolinera gasolinera;
    private String precioSumarioStr;

    public GasolineraDetailPresenter(IGasolineraDetailContract.View view, Gasolinera gasolinera) {
        this.view = view;
        this.gasolinera = gasolinera;
    }

    @Override
    public void init() {
        double precioSumario = calculateSummaryPrice();

        if (precioSumario <= 0.0) {
            precioSumarioStr = "-";
        } else {
            precioSumarioStr = String.format(Locale.FRANCE, "%.3f", precioSumario);
        }

        loadGasolineraDetails();
    }

    private double calculateSummaryPrice() {
        String precioDieselStr = gasolinera.getDieselA();
        String precioGasolinaStr = gasolinera.getNormal95();
        double precioDiesel;
        double precioGasolina;
        double sumario;

        NumberFormat formato = NumberFormat.getInstance(Locale.FRANCE);

        try {
            Number numberDiesel = formato.parse(precioDieselStr);
            precioDiesel = numberDiesel.doubleValue();
        } catch (Exception e) {
            precioDiesel = ERROR_CONVERSION;
        }

        try {
            Number numberGasolina = formato.parse(precioGasolinaStr);
            precioGasolina = numberGasolina.doubleValue();
        } catch (Exception e) {
            precioGasolina = ERROR_CONVERSION;
        }

        if (precioDiesel <= 0.0) {
            sumario = precioGasolina;
        } else if(precioGasolina <= 0.0) {
            sumario = precioDiesel;
        } else {
            sumario = (precioDiesel + precioGasolina * 2.0) / 3.0;
        }

        return sumario;
    }

    @Override
    public void onAcceptClicked() {
            view.openMenuView();
    }

    private void loadGasolineraDetails() {
        if (gasolinera.toString().equals(gasolinera.getRotulo())) {
            view.showLoadError();
        } else {
            Map<String, String> info = new HashMap<>();
            info.put("summary", precioSumarioStr + " €/L");
            info.put("label", checkValid(gasolinera.getRotulo()));
            info.put("municipality", checkValid(gasolinera.getMunicipio()));
            info.put("direction", checkValid(gasolinera.getDireccion()));
            info.put("cp", checkValid(gasolinera.getCp()));
            info.put("price95", checkValidPrice(gasolinera.getNormal95()) + " €/L");
            info.put("priceDieselA", checkValidPrice(gasolinera.getDieselA()) + " €/L");
            info.put("schedule", checkValid(gasolinera.getHorario()));

            view.showInfo(info);
        }
    }

    private String checkValid(String texto) {
        String correccion = texto;
        if (texto.equals("")) {
            correccion = "-";
        }
        return correccion;
    }

    private String checkValidPrice(String texto) {
        String correccion = texto;
        if (texto.contains("-") || texto.equals("")) {
            correccion = "-";
        }
        return correccion;
    }

    public String getPrecioSumario() {
        return precioSumarioStr;
    }
}
