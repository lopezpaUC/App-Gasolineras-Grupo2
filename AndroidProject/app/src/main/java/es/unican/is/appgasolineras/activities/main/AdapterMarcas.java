package es.unican.is.appgasolineras.activities.main;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.R;

public class AdapterMarcas extends ArrayAdapter<MarcaSelectable> {
    private Context context;
    private List<MarcaSelectable> listState;
    private boolean isFromView = false;

    //Constructor del nuevo adapter
    public AdapterMarcas(Context context, int resource, List<MarcaSelectable> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listState = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    /**
     * Crear la vista con los checkboxs y las marcas.
     * @param convertView convertir la vista en la vista completa.
     * @param position posicion en la lista.
     * @param parent view group padre.
     * @return la vista completa.
     */
    public View getCustomView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(context);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listState.get(position).getTitle());

        // To check weather checked event fire from getview() or user input
        holder.mCheckBox.setChecked(listState.get(position).isSelected());

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.GONE);
            holder.mTextView.setVisibility(View.GONE);
        } else {
           holder.mTextView.setVisibility(View.VISIBLE);
           holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isFromView) {
                    listState.get(position).setSelected(isChecked);
                }
            }
        });

        return convertView;
    }




    /**
     * Comprueba que marcas estan seleccionadas en la lista.
     *
     * @return checked Array con las marcas seleccionadas
     */
    public ArrayList<String> sumChecked(){
        ArrayList<String> checked = new ArrayList<String>();
        for (MarcaSelectable l : listState){
            if(l.isSelected()){
                checked.add(l.getTitle());
            }
        }
        return checked;
    }

    //Clase para cada marca
    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}
