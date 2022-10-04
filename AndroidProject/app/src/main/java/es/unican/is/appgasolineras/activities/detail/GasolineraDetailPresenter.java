package es.unican.is.appgasolineras.activities.detail;

import android.content.Context;

import java.util.Locale;

import es.unican.is.appgasolineras.model.Gasolinera;

public class GasolineraDetailPresenter implements IGasolineraDetailContract.Presenter {

    private final IGasolineraDetailContract.View view;
    private Gasolinera gasolinera;

    public GasolineraDetailPresenter(IGasolineraDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void init() {
        gasolinera = view.getSelectedGasolinera();
        loadGasolineraDetails();
    }


    private void loadGasolineraDetails() {
        view.showName(gasolinera.getRotulo());
        view.showMunicipality(gasolinera.getMunicipio());
        view.showLogo(searchLogoID());

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
}
