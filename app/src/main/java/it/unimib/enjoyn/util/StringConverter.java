package it.unimib.enjoyn.util;

import androidx.room.TypeConverter;

import java.lang.*;
import java.util.Arrays;


public class StringConverter {
    @TypeConverter
    public String IntArrayToString(int[] array){
     return Arrays.toString(array);
}

    @TypeConverter
    public String DoubleArrayToString(double[] array){
        return Arrays.toString(array);
    }
    @TypeConverter
    public String StringArrayToString(String[] array){
        return Arrays.toString(array);
    }



}
