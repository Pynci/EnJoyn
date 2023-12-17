package it.unimib.enjoyn.util;

import androidx.room.TypeConverter;

import java.lang.*;
import java.util.Arrays;


public class StringConverter {

    //converter per int
    @TypeConverter
    public String IntArrayToString(int[] array) {
        return toString(array);
    }

    @TypeConverter
    public int[] StringToIntArray(String string) {
        return toIntArray(string);
    }

    //converter per double
    @TypeConverter
    public String DoubleArrayToString(double[] array) {
        return toString(array);
    }

    @TypeConverter
    public double[] StringArrayToString(String string) {
        return toDoubleArray(string);
    }

    //converter per String
    @TypeConverter
    public String StringArrayToString(String[] array) {
        return toString(array);
    }

    @TypeConverter
    public String[] StringTOStringArray(String string) {
        return toStringArray(string);
    }

    public String toString(int[] array) {
        String string = "";

        for (int i = 0; i < array.length; i++) {

            string += array[i]+" ";


        }
        return string;
    }

    public String toString(double[] array) {
        String string = "";

        for (int i = 0; i < array.length; i++) {

            string += array[i]+" ";


        }
        return string;
    }

    public String toString(String[] array) {
        String string = "";

        for (int i = 0; i < array.length; i++) {

            string += array[i]+" ";


        }
        return string;
    }

    private int[] toIntArray(String string) {
        String[] stringArray = string.split(" ");
        int[] intArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {

            intArray[i] = Integer.parseInt(stringArray[i]);


        }
        return intArray;
    }

    private double[] toDoubleArray(String string) {
        String[] stringArray = string.split(" ");
        double[] intArray = new double[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {

            intArray[i] = Integer.parseInt(stringArray[i]);


        }
        return intArray;
    }

    private String[] toStringArray(String string) {
        String[] stringArray = string.split(" ");
       /* double[] intArray = new double[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {

            intArray[i] = Integer.parseInt(stringArray[i]);


        }*/
        return stringArray;
    }

}
