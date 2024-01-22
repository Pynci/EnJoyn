package it.unimib.enjoyn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.EventLocation;

public class SuggestionListAdapter extends ArrayAdapter<EventLocation> {

    private final List<EventLocation> locationList;
    //private final int distance;

    private final int layout;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{

        void onSuggestionItemClick(EventLocation eventLocation,int position);

    }



    public SuggestionListAdapter(@NonNull Context context, int layout, List<EventLocation> locationList,  OnItemClickListener onItemClickListener) {
        super(context, layout, locationList);
        this.locationList = locationList;
        this.layout = layout;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
        }

        TextView locationName = convertView.findViewById(R.id.suggestionListItem_textView_locationName);
        TextView distance = convertView.findViewById(R.id.suggestionListItem_textView_distance);

        locationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onSuggestionItemClick(locationList.get(position),position);
            }
        });

        locationName.setText(locationList.get(position).getName());
        //distance.setText(distance+"km");

        return convertView;
    }
}
