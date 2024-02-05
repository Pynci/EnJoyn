package it.unimib.enjoyn.adapter;

import static android.view.View.GONE;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.ui.viewmodels.CategoriesHolder;

public class CategoriesSelectionAdapter extends BaseAdapter {

    private final List<Category> data;
    private final Context context;
    private final List<Uri> images;

    public CategoriesSelectionAdapter(Context context, List<Category> data, List<Uri> images) {
        this.context = context;
        this.data = data;
        this.images = images;
    }

    @Override
    public int getCount() {
        return data.size() / 2 + 1;}

    @Override
    public Object getItem(int position) {
        return data.get(position * 2);
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

        MaterialCardView cardViewLeft = rowView.findViewById(R.id.customRawLayout_cardview1);
        MaterialCardView cardViewRight = rowView.findViewById(R.id.customRawLayout_cardview2);

        TextView nameCategoryCardLeft = rowView.findViewById(R.id.NameCategoryCard1);
        TextView nameCategoryCardRight = rowView.findViewById(R.id.NameCategoryCard2);

        ShapeableImageView imageViewLeft = rowView.findViewById(R.id.headerImageCard1);
        ShapeableImageView imageViewRight = rowView.findViewById(R.id.headerImageCard2);

        if(position * 2 < images.size()){
            Glide.with(context).load(images.get(position * 2)).into(imageViewLeft);
            nameCategoryCardLeft.setText(data.get(position * 2).getNome());
        }
        else{
            cardViewLeft.setVisibility(GONE);
        }

        if(position * 2 + 1 < images.size()){
            Glide.with(context).load(images.get(position * 2 + 1)).into(imageViewRight);
            nameCategoryCardRight.setText(data.get(position * 2 + 1).getNome());
        }
        else {
            cardViewRight.setVisibility(GONE);
        }

        cardViewLeft.setOnClickListener(v -> {
            cardViewLeft.setChecked(!cardViewLeft.isChecked());

            if(cardViewLeft.isChecked()) {
                CategoriesHolder categoriesHolder = CategoriesHolder.getInstance();
                categoriesHolder.addCategory(data.get(position * 2));
            }
            else{
                CategoriesHolder categoriesHolder = CategoriesHolder.getInstance();
                categoriesHolder.removeCategory(data.get(position * 2).getNome());
            }
        });

        cardViewRight.setOnClickListener(v -> {
            cardViewRight.setChecked(!cardViewRight.isChecked());

            if(cardViewRight.isChecked()) {
                CategoriesHolder categoriesHolder = CategoriesHolder.getInstance();
                categoriesHolder.addCategory(data.get(position * 2 + 1));
            }
            else{
                CategoriesHolder categoriesHolder = CategoriesHolder.getInstance();
                categoriesHolder.removeCategory(data.get(position * 2 + 1).getNome());
            }
        });

        return rowView;
    }
}
