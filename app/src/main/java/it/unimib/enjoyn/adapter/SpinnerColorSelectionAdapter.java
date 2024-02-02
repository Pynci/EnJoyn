package it.unimib.enjoyn.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.util.ColorObject;

public class SpinnerColorSelectionAdapter extends ArrayAdapter<ColorObject> {



        private LayoutInflater layoutInflater;

        public SpinnerColorSelectionAdapter(Context context, List<ColorObject> list) {
            super(context, 0, list);
            layoutInflater = LayoutInflater.from(context);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = layoutInflater.inflate(R.layout.color_spinner_bg, parent, false);
            return getViewInternal(view, position);
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
            ColorObject colorObject =  getItem(position);
            if (colorObject == null) {
                return view;
            }

            TextView colorNameItem = view.findViewById(R.id.colorName);
            TextView colorHexItem = view.findViewById(R.id.colorHex);
            TextView colorNameBG = view.findViewById(R.id.colorNameBG);
            View colorBlob = view.findViewById(R.id.colorBlob);

            if (colorNameBG != null) {
                colorNameBG.setText(colorObject.getName());
                colorNameBG.setTextColor(Color.parseColor(colorObject.getContrastHexHash()));
            }

            if (colorNameItem != null) {
                colorNameItem.setText(colorObject.getName());
            }

            if (colorHexItem != null) {
                colorHexItem.setText(colorObject.getHex());
            }

            if (colorBlob != null && colorBlob.getBackground() != null) {
                colorBlob.getBackground().setTint(Color.parseColor(colorObject.getHex()));
            }

            return view;
        }
    }

