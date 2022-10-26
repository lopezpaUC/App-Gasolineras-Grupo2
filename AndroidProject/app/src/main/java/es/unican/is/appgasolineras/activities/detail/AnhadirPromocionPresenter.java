package es.unican.is.appgasolineras.activities.detail;

public class AnhadirPromocionPresenter implements IAnhadirPromocionContract.Presenter{

    // Vista
    private final IAnhadirPromocionContract.View view;
    public AnhadirPromocionPresenter(IAnhadirPromocionContract.View view) {
        this.view = view;
    }
}
