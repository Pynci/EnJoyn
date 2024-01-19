package it.unimib.enjoyn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Category;

public class CategoriesSelectionAdapter extends BaseAdapter {

    private final List<Category> data; // Cambia il tipo di dati in base ai tuoi requisiti
    private final Context context;

    public CategoriesSelectionAdapter(Context context, List<Category> data) {
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

        MaterialCardView cardView1 = rowView.findViewById(R.id.customRawLayout_cardview1);
        MaterialCardView cardView2 = rowView.findViewById(R.id.customRawLayout_cardview2);
        TextView nameCategoryCard1 = rowView.findViewById(R.id.NameCategoryCard1);
        TextView nameCategoryCard2 = rowView.findViewById(R.id.NameCategoryCard2);

        nameCategoryCard1.setText(data.get(position).getNome());
        nameCategoryCard2.setText(data.get(position).getNome());

        cardView1.setOnClickListener(v -> {
            cardView1.setChecked(!cardView1.isChecked());
        });

        cardView2.setOnClickListener(v -> {
            cardView2.setChecked(!cardView2.isChecked());
        });

        return rowView;
    }
}
