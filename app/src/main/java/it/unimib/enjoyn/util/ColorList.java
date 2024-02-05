package it.unimib.enjoyn.util;

import java.util.ArrayList;
import java.util.List;



public class ColorList {

    ColorObject defaultColor = basicColors().get(0);

    public ColorObject getDefaultColor() {
        return defaultColor;
    }

    public int colorPosition(ColorObject colorObject){
        for (int i = 0 ; i < basicColors().size(); i ++){

            if( colorObject == basicColors().get(i)){
                return i;
            }


        }
        return 0;
    }

    public List<ColorObject> basicColors(){
        List<ColorObject> colorObjectList = new ArrayList<>();


        colorObjectList.add(new ColorObject( "Violet", "#805D93", "FFFFFF"));
        colorObjectList.add(new ColorObject( "Purple", "#CE6A85", "FFFFFF"));
        colorObjectList.add(new ColorObject( "Coral", "#FF8C61", "FFFFFF"));
        colorObjectList.add(new ColorObject( "Olivine", "#9EBD6E", "FFFFFF"));
        colorObjectList.add(new ColorObject( "Green", "#169873", "FFFFFF"));
        colorObjectList.add(new ColorObject( "light blue", "#348AA7", "FFFFFF"));
        colorObjectList.add(new ColorObject( "Blue", "#355691", "FFFFFF"));

        return  colorObjectList;
    }
}
