package it.unimib.enjoyn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import it.unimib.enjoyn.Event;
import it.unimib.enjoyn.R;

public class EventReclyclerViewAdapter extends
        RecyclerView.Adapter<EventReclyclerViewAdapter.NewViewHolder> {


    public interface OnItemClickListener{

        void onEventItemClick(Event event);

        void onJoinButtonPressed(int position);
    }

    private final List<Event> eventList;

    private final OnItemClickListener onItemClickListener;

    public EventReclyclerViewAdapter(List<Event> eventList, OnItemClickListener onItemClickListener){
        this.eventList = eventList;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public EventReclyclerViewAdapter.NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);

        return new NewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventReclyclerViewAdapter.NewViewHolder holder, int position) {
        holder.bind(eventList.get(position));
    }

    @Override
    public int getItemCount() {
        if (eventList != null) {
            return eventList.size();
        }
        return 0;
    }


    public class NewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewTitle;

        private final TextView textViewData;

        private final TextView textViewTime;

        private final TextView textViewPlace;

        private final TextView textViewPeopleNumber;

        private final TextView textViewDistance;


        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.eventTitle);
            textViewData = itemView.findViewById(R.id.date);
            textViewTime = itemView.findViewById(R.id.time);
            textViewPlace = itemView.findViewById(R.id.place);
            textViewPeopleNumber = itemView.findViewById(R.id.peopleNumber);
            textViewDistance = itemView.findViewById(R.id.distance);

            Button joinButton = itemView.findViewById(R.id.joinButton);
            itemView.setOnClickListener(this);
            joinButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.joinButton){

                /**TODO
                 *
                 * eventList.joinEvent(getAdapterPosition());
                 *
                 * notify()
                 */

            }else{
                onItemClickListener.onEventItemClick(eventList.get(getAdapterPosition()));
            }
        }

        public void bind(Event event) {

            textViewTitle.setText(event.getTitle());
            textViewData.setText(event.getDate());
            textViewTime.setText(event.getTime());
            textViewPlace.setText(event.getPlace());
            textViewPeopleNumber.setText(event.getPeopleNumberString());
            textViewDistance.setText(event.getDistanceString());

        }
    }

}