package it.unimib.enjoyn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.enjoyn.model.Event;
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
        private final Button joinButton;


        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.eventListItem_textView_eventTitle);
            textViewData = itemView.findViewById(R.id.eventListItem_textView_date);
            textViewTime = itemView.findViewById(R.id.eventListItem_textView_time);
            textViewPlace = itemView.findViewById(R.id.eventListItem_textView_place);
            textViewPeopleNumber = itemView.findViewById(R.id.eventListItem_textView_peopleNumber);
            textViewDistance = itemView.findViewById(R.id.eventListItem_textView_distance);

            joinButton = itemView.findViewById(R.id.eventListItem_button_joinButton);
            itemView.setOnClickListener(this);
            joinButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.eventListItem_button_joinButton){
                setTextButtonTodoEvent(!eventList.get(getAdapterPosition()).isTODO());
                onItemClickListener.onJoinButtonPressed(getAdapterPosition());

            }else{
                onItemClickListener.onEventItemClick(eventList.get(getAdapterPosition()));
                //Navigation.findNavController(v).navigate(R.id.action_discover_to_discoverSingleEvent);
            }
        }

        public void bind(Event event) {

            textViewTitle.setText(event.getTitle());
            textViewData.setText(event.getDate());
            textViewTime.setText(event.getTime());
            textViewPlace.setText(event.getPlace());
            textViewPeopleNumber.setText(event.getPeopleNumberString());
            textViewDistance.setText(event.getDistanceString());
            setTextButtonTodoEvent(!eventList.get(getAdapterPosition()).isTODO());
        }


        private void setTextButtonTodoEvent(boolean isTodo){
            if(isTodo){
                joinButton.setText(R.string.Join);
            }
            else{
                joinButton.setText(R.string.remove);
            }
        }


    }


}