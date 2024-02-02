package it.unimib.enjoyn.util;

import java.util.ArrayList;
import java.util.List;

import it.unimib.enjoyn.R;

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

        colorObjectList.add(new ColorObject( "champagnePink", "#EDDCD2", "FFFFFF", R.color.champagnePink));
        colorObjectList.add(new ColorObject( "mistyRose", "#FDE2E4","FFFFFF", R.color.mistyRose));
        colorObjectList.add(new ColorObject( "mimiPink", "#FAD2E1", "FFFFFF", R.color.mimiPink));
        colorObjectList.add(new ColorObject( "ligthCyan", "#C5DEDD", "FFFFFF", R.color.ligthCyan));
        colorObjectList.add(new ColorObject( "mintCream", "#DBE7E4", "FFFFFF", R.color.mintCream));
        colorObjectList.add(new ColorObject( "aliceBlue", "#D6E2E9", "FFFFFF", R.color.aliceBlue));
        colorObjectList.add(new ColorObject( "columbiaBlue", "#BCD4E6", "FFFFFF", R.color.columbiaBlue));
        colorObjectList.add(new ColorObject( "powderBlue", "#99C1DE", "FFFFFF", R.color.powderBlue));

        return  colorObjectList;
    }
}