package it.unimib.enjoyn.util;



import java.util.List;
import it.unimib.enjoyn.model.Category;

public class CategoryList {


    public int categoryPosition(List<Category> categoryList, Category category){
        for (int i = 0 ; i < categoryList.size(); i ++){

            if( category == categoryList.get(i)){
                return i;
            }


        }
        return 0;
    }

}
