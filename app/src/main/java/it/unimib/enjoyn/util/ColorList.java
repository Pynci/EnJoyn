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

        colorObjectList.add(new ColorObject( "champagnePink", "#EDDCD2", "FFFFFF"));
        colorObjectList.add(new ColorObject( "mistyRose", "#FDE2E4","FFFFFF"));
        colorObjectList.add(new ColorObject( "mimiPink", "#FAD2E1", "FFFFFF"));
        colorObjectList.add(new ColorObject( "ligthCyan", "#C5DEDD", "FFFFFF"));
        colorObjectList.add(new ColorObject( "mintCream", "#DBE7E4", "FFFFFF"));
        colorObjectList.add(new ColorObject( "aliceBlue", "#D6E2E9", "FFFFFF"));
        colorObjectList.add(new ColorObject( "columbiaBlue", "#BCD4E6", "FFFFFF" ));
        colorObjectList.add(new ColorObject( "powderBlue", "#99C1DE", "FFFFFF"));

        return  colorObjectList;
    }
}
