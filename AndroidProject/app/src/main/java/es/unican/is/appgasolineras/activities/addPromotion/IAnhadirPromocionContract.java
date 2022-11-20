package es.unican.is.appgasolineras.activities.addPromotion;

import java.util.List;
import java.util.Map;

import es.unican.is.appgasolineras.activities.addPromotion.EstadoOperacionAnhadirPromocion;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

public interface IAnhadirPromocionContract {

    interface Presenter {
        /**
         * Initialization method
         */
        void init();

        /**
         * Añade una promocion a la base de datos, en funcion de la informacion introducida por
         * el usuario.
         *
         * @param infoList Informacion introducida compuesta por listas de strings.
         * @param infoString Informacion introducida compuesta por strings.
         * @return el tipo de estado de la operacion: exito o un error determinado.
         */
        void onAnhadirClicked(Map<String, List<String>> infoList, Map<String, String> infoString);

        /**
         * Obtiene strings que representen a las gasolineras del repositorio, con el formato:
         * nombre // direccion // localidad
         * @return Array de strings que representa a las gasolineras del repositorio.
         */
        String[] getArrayGasolinerasParsed();
    }

    interface View {

        /**
         * Initialization method
         */
        void init();

        /**
         * Returns the Gasolineras Repository object.
         * This object can be used to access gas stations from
         * different sources (REST API, persisted DB, etc.)
         * This method is in the View because it generally requires access to the
         * Android Context, and this is available in the View
         * @return the Repository object to access gas stations
         */
        IGasolinerasRepository getGasolineraRepository();

        /**
         * Retorna el objeto Promociones Repository.
         * Este objeto puede usarse para acceder a las promociones persistidas en Base de Datos.
         * Se encuentra en la Vista, debido a que hace uso del Contexto Android, disponible en
         * la Vista.
         *
         * @return el objeto Repositorio que permite acceder a promociones.
         */
        IPromocionesRepository getPromocionRepository();

        /**
         * Muestra estado de la operacion de anhadir promocion.
         *
         * @param status Estado.
         */
        void showStatus(EstadoOperacionAnhadirPromocion status);
    }
}
