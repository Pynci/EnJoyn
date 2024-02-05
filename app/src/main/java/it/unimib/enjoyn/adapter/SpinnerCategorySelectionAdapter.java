package it.unimib.enjoyn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.util.CategoryList;
import it.unimib.enjoyn.util.ColorObject;

public class SpinnerCategorySelectionAdapter extends ArrayAdapter {

    private LayoutInflater layoutInflater;

    public SpinnerCategorySelectionAdapter(@NonNull Context context, int resource, @NonNull List<String> categoryNomeList) {
        super(context, resource, categoryNomeList);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View cv = convertView;
        if (cv == null) {
            cv = layoutInflater.inflate(R.layout.color_spinner_item, parent, false);
        }
        return getViewInternal(cv, position);
    }

    private View getViewInternal(View view, int position) {
        HashMap<String, Integer> categoryVectorDrawableHashMap = new CategoryList().categoryVectorDrawableHashMap();

        String nome = (String) getItem(position);
        if (nome == null) {
            return view;
        }

        TextView categoryName = view.findViewById(R.id.textView_Category_Name);
        ImageView imageCategory = view.findViewById(R.id.imageView_Category_Image);

        if (categoryName != null) {
            categoryName.setText(nome);
        }

        if (imageCategory != null) {
            imageCategory.setBackgroundResource(categoryVectorDrawableHashMap.get(nome));
        }

        return view;
    }
}