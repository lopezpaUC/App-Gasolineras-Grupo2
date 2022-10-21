package es.unican.is.appgasolineras.activities.info;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import es.unican.is.appgasolineras.R;

public class InfoView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_view);

        TextView tv = findViewById(R.id.tvInfoMessage);
        tv.setText("Aplicación creada para el Proyecto Integrado 2022");
    }
}