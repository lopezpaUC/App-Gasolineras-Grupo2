package es.unican.is.appgasolineras.activities.main;

public class MarcaSelectable {

    private String title;
    private boolean selected;

    /**
     * Getter del parametro title.
     *
     * @return nombre de la marca.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Guarda el nombre de la marca.
     *
     * @param title Nombre de la marca.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter del parametro selected.
     *
     * @return si esta seleccionada la marca o no.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Modificar el boton de seleccion.
     *
     * @param selected Saber si esta selccionado.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
