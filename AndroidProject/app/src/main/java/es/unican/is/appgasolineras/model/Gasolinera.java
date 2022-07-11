package es.unican.is.appgasolineras.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Class that represents a Gas Station, with the attributes defined in the following REST API
 * https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/help
 *
 * The API can be easily tested with https://reqbin.com/
 *
 * The actual gas stations retrieved from the REST API have more properties than the ones currently
 * defined in this class.
 */
@Entity(tableName = "gasolineras")
public class Gasolinera implements Parcelable {

    @SerializedName("IDEESS") @NonNull @PrimaryKey  private String id;

    @SerializedName("Rótulo")                       private String rotulo;
    @SerializedName("C.P.")                         private String cp;
    @SerializedName("Dirección")                    private String direccion;
    @SerializedName("Municipio")                    private String municipio;

    @SerializedName("Precio Gasoleo A")             private String dieselA;
    @SerializedName("Precio Gasolina 95 E5")        private String normal95;  // 95 octanes

    public Gasolinera() {

    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDieselA() {
        return dieselA;
    }

    public void setDieselA(String dieselA) {
        this.dieselA = dieselA;
    }

    public String getNormal95() {
        return normal95;
    }

    public void setNormal95(String normal95) {
        this.normal95 = normal95;
    }

    /*
     * Methods for Parcelable interface. Needed to send this object in an Intent.
     *
     * IMPORTANT: if more properties are added, these methods must be modified accordingly,
     * otherwise those properties will not be correctly saved in an Intent.
     */

    protected Gasolinera(Parcel in) {
        id = in.readString();
        rotulo = in.readString();
        cp = in.readString();
        direccion = in.readString();
        municipio = in.readString();
        dieselA = in.readString();
        normal95 = in.readString();
    }

    public static final Creator<Gasolinera> CREATOR = new Creator<Gasolinera>() {
        @Override
        public Gasolinera createFromParcel(Parcel in) {
            return new Gasolinera(in);
        }

        @Override
        public Gasolinera[] newArray(int size) {
            return new Gasolinera[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(rotulo);
        dest.writeString(cp);
        dest.writeString(direccion);
        dest.writeString(municipio);
        dest.writeString(dieselA);
        dest.writeString(normal95);
    }
}
