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

    public SpinnerCategorySelectionAdapter(@NonNull Context context, @NonNull List<String> categoryNomeList) {
        super(context,0, categoryNomeList);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View cv = convertView;
        if (cv == null) {
            cv = layoutInflater.inflate(R.layout.category_spinner_item, parent, false);
        }
        return getViewInternal(cv, position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.category_spinner_bg, parent, false);
        return getViewInternal(view, position);
    }

    private View getViewInternal(View view, int position) {
      //  HashMap<String, Integer> categoryVectorDrawableHashMap = new CategoryList().categoryVectorDrawableHashMap();

        String nome = (String) getItem(position);
        if (nome == null) {
            return view;
        }

        TextView categoryName = view.findViewById(R.id.textView_Category_Name);
        ImageView imageCategory = view.findViewById(R.id.imageView_Category_Image);
        TextView categoryNameBG = view.findViewById(R.id.textView_Category_Name_BG);
        ImageView imageCategoryBG = view.findViewById(R.id.imageView_Category_Image_BG);

        if (categoryName != null) {
            categoryName.setText(nome);
        }

        if(categoryNameBG!= null){
            categoryNameBG.setText(nome);

        }
        if (imageCategory != null) {
            setCategoryImage(imageCategory, nome);
            //imageCategory.setBackgroundResource(categoryVectorDrawableHashMap.get(nome));
        }
        if (imageCategoryBG != null) {
            setCategoryImage(imageCategoryBG, nome);
            //imageCategory.setBackgroundResource(categoryVectorDrawableHashMap.get(nome));
        }

        return view;
    }

    public void setCategoryImage(ImageView imageView, String nomeCategory){
        switch (nomeCategory){
            case "Passeggiata":
                imageView.setBackgroundResource(R.drawable.passeggiata);
                break;
            case "Viaggi":
                imageView.setBackgroundResource(R.drawable.viaggi);
                break;
            case "Pranzo":
                imageView.setBackgroundResource(R.drawable.pranzo);
                break;
            case "Videogiochi":
                imageView.setBackgroundResource(R.drawable.videogiochi);
                break;
            case "Shopping":
                imageView.setBackgroundResource(R.drawable.shopping);
                break;
            case "Cinema":
                imageView.setBackgroundResource(R.drawable.cinema);
                break;
            case "Sport":
                imageView.setBackgroundResource(R.drawable.sport);
                break;
        }

    }
}