package it.unimib.enjoyn.util;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.ui.viewmodels.CategoryViewModel;

public class CategoryList {


    public int categoryPosition(List<Category> categoryList, Category category){
        for (int i = 0 ; i < categoryList.size(); i ++){

            if( category == categoryList.get(i)){
                return i;
            }


        }
        return 0;
    }

    public HashMap<String, Integer> categoryVectorDrawableHashMap() {
        HashMap<String, Integer> categoryVectorDrawableHashMap = new HashMap<>();

        categoryVectorDrawableHashMap.put("Passeggiata", R.drawable.passeggiata);
        categoryVectorDrawableHashMap.put("Viaggi", R.drawable.viaggi);
        categoryVectorDrawableHashMap.put("Pranzo", R.drawable.pranzo);
        categoryVectorDrawableHashMap.put("Videogiochi", R.drawable.videogiochi);
        categoryVectorDrawableHashMap.put("Shopping", R.drawable.shopping);
        categoryVectorDrawableHashMap.put("Cinema", R.drawable.cinema);
        categoryVectorDrawableHashMap.put("Sport", R.drawable.sport);

        return categoryVectorDrawableHashMap;
    }
}
