package it.unimib.enjoyn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterCategoriesSelection extends BaseAdapter {

    private List<String> data; // Cambia il tipo di dati in base ai tuoi requisiti
    private Context context;

    public AdapterCategoriesSelection(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size() / 2; // Ogni riga contiene due elementi
    }

    @Override
    public Object getItem(int position) {
        return data.get(position * 2); // Ritorna il primo elemento della riga
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.custom_row_layout, parent, false);
        }

        return rowView;
    }
}
