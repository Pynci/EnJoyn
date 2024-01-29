package it.unimib.enjoyn.adapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.R;

public class EventReclyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EVENT_VIEW_TYPE = 0;
    private static final int LOADING_VIEW_TYPE = 1;

    public interface OnItemClickListener{
        void onEventItemClick(Event event);

        void onJoinButtonPressed(int position);
    }

    private final List<Event> eventList;

    private final Application application;
    private final OnItemClickListener onItemClickListener;

    public EventReclyclerViewAdapter(List<Event> eventList, Application application, OnItemClickListener onItemClickListener){
        this.eventList = eventList;
        this.onItemClickListener = onItemClickListener;
        this.application = application;
    }

    public int getItemViewType(int position){
        if(eventList.get(position) == null){
            return LOADING_VIEW_TYPE;
        } else {
            return EVENT_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == EVENT_VIEW_TYPE){
            view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
            return new EventViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_loading_item, parent, false);
            return new LoadingEventViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventViewHolder){
            ((EventViewHolder) holder).bind(eventList.get(position));
        } else if (holder instanceof LoadingEventViewHolder) {
            ((LoadingEventViewHolder) holder).activate();
        }
    }


    @Override
    public int getItemCount() {
        if (eventList != null) {
            return eventList.size();
        }
        return 0;
    }


    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewTitle;
        private final TextView textViewData;
        private final TextView textViewTime;
        private final TextView textViewPlace;
        private final TextView textViewPeopleNumber;
        private final TextView textViewDistance;
        private final Button joinButton;
        private final ImageView eventImageView;


        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.eventListItem_textView_eventTitle);
            textViewData = itemView.findViewById(R.id.eventListItem_textView_date);
            textViewTime = itemView.findViewById(R.id.eventListItem_textView_time);
            textViewPlace = itemView.findViewById(R.id.eventListItem_textView_place);
            textViewPeopleNumber = itemView.findViewById(R.id.eventListItem_textView_peopleNumber);
            textViewDistance = itemView.findViewById(R.id.eventListItem_textView_distance);
            eventImageView = itemView.findViewById(R.id.eventListItem_imageView_eventImage);
            joinButton = itemView.findViewById(R.id.eventListItem_button_joinButton);
            itemView.setOnClickListener(this);
            joinButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.eventListItem_button_joinButton){
                //setTextButtonTodoEvent(!eventList.get(getBindingAdapterPosition()).isTODO());
                onItemClickListener.onJoinButtonPressed(getBindingAdapterPosition());

            }else{
                onItemClickListener.onEventItemClick(eventList.get(getBindingAdapterPosition()));
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
            //setTextButtonTodoEvent(!eventList.get(getAdapterPosition()).isTODO());
            Glide.with(application).load(event.getImageUrl()).placeholder(R.drawable.baseline_downloading_24).into(eventImageView);
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

    public static class LoadingEventViewHolder extends RecyclerView.ViewHolder {

        private final ProgressBar progressBar;
        public LoadingEventViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.eventLoadingItem_progressBar);
        }

        public void activate(){
            progressBar.setIndeterminate(true);
        }
    }


}